/*
 * Copyright 2017-present Walmart, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oneops.boo.shell.commands;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.oneops.api.exception.OneOpsClientAPIException;
import com.oneops.boo.ClientConfig;
import com.oneops.boo.utils.BooUtils;
import com.oneops.boo.workflow.BuildAllPlatforms;
import com.oneops.boo.yaml.Constants;
import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;
import com.planet57.gshell.util.io.IO;
import com.planet57.gshell.util.cli2.Argument;
import com.planet57.gshell.util.i18n.I18N;
import com.planet57.gshell.util.i18n.MessageBundle;

import java.util.List;
import java.util.Map;

/**
 * Display IP addresses of deployments.
 */
@Command(name="boo/get-ips", description = "Display IP addresses")
public class GetIpsAction
  extends TemplateActionSupport
{
  private interface Messages
    extends MessageBundle
  {
    @DefaultMessage("Environment name: %s")
    String environmentName(String environment);

    @DefaultMessage("Platform name: %s")
    String platformName(String environment);

    @DefaultMessage("Compute name: %s")
    String computeName(String environment);
  }

  private static final Messages messages = I18N.create(Messages.class);

  // FIXME: unsure what the names of these are for seems like environment is also platform?
  // FIXME: and component is also refereed to as a compute erroneously?

  @Nullable
  @Argument(index = 0, description = "Environment name", token = "ENV")
  private String environment;

  @Nullable
  @Argument(index = 1, description = "Compute class", token = "CLASS")
  private String component;
  
  private BooUtils booUtils = new BooUtils();
  
  @Override
  public Object execute(@Nonnull final CommandContext context) throws Exception {
    ClientConfig config = createClientConfig();
    BuildAllPlatforms flow = createFlow(config);

    ensureAssemblyExists(flow);

    IO io = context.getIo();

    if (environment != null && component != null) {
      displayIps(io, flow, environment, component);
    }
    else if (environment != null) {
      displayIps(io, flow, environment);
    }
    else {
      displayIps(io, flow);
    }

    return null;
  }

  // FIXME: more or less duplicating the logic with BooCli but this could be cleaned up
  // FIXME: should also allow for just emitting the ips directly w/o other ancillary information

  private void displayIps(final IO io, final BuildAllPlatforms flow) {
    Map<String, Object> platforms = flow.getConfig().getYaml().getPlatforms();
    List<String> computes = booUtils.getComponentOfCompute(flow);
    io.println(messages.environmentName(flow.getConfig().getYaml().getBoo().getEnvName()));

    for (String pname : platforms.keySet()) {
      io.println(messages.platformName(pname));

      for (String cname : computes) {
        io.println(messages.computeName(cname));
        getIps(io, flow, pname, cname);
      }
    }
  }

  private void displayIps(final IO io, final BuildAllPlatforms flow, final String environment) {
    String yamlEnv = flow.getConfig().getYaml().getBoo().getEnvName();

    if (yamlEnv.equals(environment)) {
      displayIps(io, flow);
    }
    else {
      // TODO: this may be better of a log.warm
      io.println(Constants.NO_ENVIRONMENT);
    }
  }

  private void displayIps(final IO io, final BuildAllPlatforms flow, final String environment, final String component) {
    String yamlEnv = flow.getConfig().getYaml().getBoo().getEnvName();

    if (environment.equals("*") || yamlEnv.equals(environment)) {
      Map<String, Object> platforms = flow.getConfig().getYaml().getPlatforms();
      List<String> computes = booUtils.getComponentOfCompute(flow);

      for (String s : computes) {
        if (s.equals(component)) {
          io.println(messages.environmentName(flow.getConfig().getYaml().getBoo().getEnvName()));

          for (String pname : platforms.keySet()) {
            io.println(messages.platformName(pname));
            io.println(messages.computeName(component));
            getIps(io, flow, pname, component);
          }

          return;
        }
      }

      // TODO: this may be better of a log.warm
      io.println("No such component: " + component);
    }
    else {
      // TODO: this may be better of a log.warm
      io.println("No such environment: " + environment);
    }
  }

  private void getIps(final IO io, final BuildAllPlatforms flow, final String platformName, final String componentName) {
    log.debug("Fetching IP addresses; platform={}, component={}", platformName, componentName);

    try {
      // TODO: would be better to extract what printIps does so we can format the output
      String result = flow.printIps(platformName, componentName);
      io.print(result); // ^^^ includes newline
    }
    catch (OneOpsClientAPIException e) {
      log.warn("Failed to fetch IP addresses", e);
    }
  }
}

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

import com.oneops.api.resource.model.Deployment;
import com.oneops.boo.ClientConfig;
import com.oneops.boo.workflow.BuildAllPlatforms;
import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;
import com.planet57.gshell.util.cli2.Option;

import java.util.List;

/**
 * Update assemblies.
 */
@Command(name="boo/update", description = "Update assemblies")
public class UpdateAction
  extends TemplateActionSupport
{
  @Option(longName = "no-deploy", description = "Disable deployment")
  private boolean disableDeploy;

  @Override
  public Object execute(final @Nonnull CommandContext context) throws Exception {
    ClientConfig clientConfig = createClientConfig();
    BuildAllPlatforms flow = createFlow(clientConfig);

    if (!clientConfig.getYaml().getAssembly().getAutoGen()) {
      ensureAssemblyExists(flow);

      log.debug("Updating assembly: {}", clientConfig.getYaml().getAssembly().getName());
      Deployment deployment = flow.process(true, disableDeploy);

      // TODO: explain deployment
    }
    else {
      List<String> assemblies = flow.getAllAutoGenAssemblies(clientConfig.getYaml().getAssembly().getName());
      for (String assembly : assemblies) {
        // TODO: not terribly ideal to adjust existing config & re-create flow, but following BooCli impl
        clientConfig.getYaml().getAssembly().setName(assembly);
        flow = createFlow(clientConfig);

        log.debug("Updating assembly: {}", assembly);
        Deployment deployment = flow.process(true, disableDeploy);

        // TODO: explain deployment
      }
    }

    return null;
  }
}

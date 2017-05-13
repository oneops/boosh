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

import java.util.List;

import javax.annotation.Nonnull;

import com.oneops.boo.ClientConfig;
import com.oneops.boo.workflow.BuildAllPlatforms;
import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;
import com.planet57.gshell.util.cli2.Argument;
import com.planet57.gshell.util.io.IO;

/**
 * Display instances.
 */
@Command(name="boo/instances", description = "Display instances")
public class InstancesAction
  extends TemplateActionSupport
{
  @Argument(index = 0, required = true, description = "Platform name", token = "PLATFORM")
  private String platform;

  @Argument(index = 1, required = true, description = "Component name", token = "COMPONENT")
  private String component;

  @Override
  public Object execute(final @Nonnull CommandContext context) throws Exception {
    ClientConfig clientConfig = createClientConfig();
    BuildAllPlatforms flow = createFlow(clientConfig);

    ensureAssemblyExists(flow);

    IO io = context.getIo();

    log.debug("Listing instances; platform={}, component={}", platform, component);
    List<String> actions = flow.listInstances(platform, component);
    actions.forEach(io::println);

    return null;
  }
}

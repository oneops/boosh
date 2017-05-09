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

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.oneops.boo.ClientConfig;
import com.oneops.boo.workflow.BuildAllPlatforms;
import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;

import static com.google.common.base.Preconditions.checkState;

/**
 * Remove deployed configurations.
 */
@Command(name="boo/remove", description = "Remove deployed configurations")
public class RemoveAction
  extends BooActionSupport
{
  @Override
  public Object execute(@Nonnull final CommandContext context) throws Exception {
    ClientConfig config = createConfig();
    BuildAllPlatforms flow = createFlow(config);

    List<String> assemblies = Collections.emptyList();
    if (config.getYaml().getAssembly().getAutoGen()) {
      assemblies = listAutogenAssemblies(flow, config.getYaml().getAssembly().getName());
    }
    else {
      String assembly = config.getYaml().getAssembly().getName();
      if (flow.isAssemblyExist(assembly)) {
        assemblies = Collections.singletonList(assembly);
      }
    }

    checkState(!assemblies.isEmpty(), "Nothing to remove");

    // TODO: query user if not forced

    for (String assembly : assemblies) {
      log.debug("Removing assembly: {}", assembly);

      if (flow.isAssemblyExist(assembly)) {
        flow.removeAllEnvs();
        flow.removeAllPlatforms();
      }
    }

    return null;
  }

  private List<String> listAutogenAssemblies(final BuildAllPlatforms flow, final String prefix) {
    checkState(prefix != null && !prefix.trim().isEmpty(), "Prefix of assembly must not be empty");
    return flow.getAllAutoGenAssemblies(prefix);
  }
}

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
import com.oneops.boo.yaml.AssemblyBean;
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

    List<String> assemblies = collectAssemblies(config, flow);
    checkState(!assemblies.isEmpty(), "Nothing to remove");

    // TODO: ask user if not forced; with LineReader

    for (String assembly : assemblies) {
      log.debug("Removing assembly: {}", assembly);

      if (flow.isAssemblyExist(assembly)) {
        // TODO: sort out return values for these and actions to take BooCli impl unclear
        flow.removeAllEnvs();
        flow.removeAllPlatforms();
      }
    }

    return null;
  }

  /**
   * Collect all assembly names to remove.
   */
  private List<String> collectAssemblies(final ClientConfig config, final BuildAllPlatforms flow) {
    AssemblyBean assembly = config.getYaml().getAssembly();

    if (assembly.getAutoGen()) {
      String prefix = assembly.getName();
      checkState(prefix != null && !prefix.trim().isEmpty(), "Prefix of assembly must not be empty");
      return flow.getAllAutoGenAssemblies(prefix);
    }
    else {
      String name = assembly.getName();
      if (flow.isAssemblyExist(name)) {
        return Collections.singletonList(name);
      }
    }

    return Collections.emptyList();
  }
}

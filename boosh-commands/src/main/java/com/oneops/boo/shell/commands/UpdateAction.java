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

import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;

/**
 * Update assemblies.
 */
@Command(name="boo/update", description = "Update assemblies")
public class UpdateAction
  extends TemplateActionSupport
{
  @Override
  public Object execute(final @Nonnull CommandContext context) throws Exception {
    // TODO
    return null;
  }

  /*
        } else if (cmd.hasOption("u")) {
        if (!config.getYaml().getAssembly().getAutoGen()) {
          if (flow.isAssemblyExist()) {
            this.createPacks(Boolean.TRUE, isNoDeploy);
          } else {
            System.err.printf(Constants.NOTFOUND_ERROR, config.getYaml().getAssembly().getName());
          }
        } else {
          List<String> assemblies = this.listFiles(this.config.getYaml().getAssembly().getName());
          for (String asm : assemblies) {
            this.initOo(config, asm, comment);
            this.createPacks(Boolean.TRUE, isNoDeploy);
          }
        }
   */
}

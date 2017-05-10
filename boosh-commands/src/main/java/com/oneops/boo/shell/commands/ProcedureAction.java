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

import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;

import javax.annotation.Nonnull;

/**
 * Invoke a procedure.
 */
@Command(name="boo/procedure", description = "Invoke a procedure")
public class ProcedureAction
  extends TemplateActionSupport
{
  @Override
  public Object execute(final @Nonnull CommandContext context) throws Exception {
    // TODO
    return null;
  }

  /*
  } else if (cmd.hasOption("procedure")) {
        if (cmd.getOptionValues("procedure").length != 3) {
          System.err
              .println("Wrong parameters! --prodedure <platformName> <componentName> <actionName>");
          return Constants.EXIT_WRONG_PRAMETER;
        } else {
          String[] args = cmd.getOptionValues("procedure");
          String arglist = "";
          int rollAt = 100;
          if (cmd.hasOption("procedure-arguments")) {
            arglist = cmd.getOptionValue("procedure-arguments");
          }
          if (cmd.hasOption("procedure-step-size")) {
            rollAt = Integer.parseInt(cmd.getOptionValue("procedure-step-size"));
          }
          List<String> instances = null;
          if (cmd.hasOption("procedure-instances")) {
            String ins = cmd.getOptionValue("procedure-instances");
            if (ins != null && ins.trim().length() > 0) {
              if (ins.equalsIgnoreCase("list")) {
                List<String> list = flow.listInstances(args[0], args[1]);
                if (list != null) {
                  for (String instance : list) {
                    System.out.println(instance);
                  }
                }
                return Constants.EXIT_NORMAL;
              }
              instances = Arrays.asList(ins.split(","));
            }
          }
          if ("list".equalsIgnoreCase(args[2])) {
            List<String> list = flow.listActions(args[0], args[1]);
            if (list != null) {
              for (String instance : list) {
                System.out.println(instance);
              }
            }
          } else {
            exit = this.executeAction(args[0], args[1], args[2], arglist, instances, rollAt);
          }

        }
      } else {
        System.err.println("Wrong parameters!");
        return Constants.EXIT_WRONG_PRAMETER;
      }
   */
}

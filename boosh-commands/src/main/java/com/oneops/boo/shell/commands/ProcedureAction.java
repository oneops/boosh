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

import com.google.common.base.Splitter;
import com.oneops.api.exception.OneOpsClientAPIException;
import com.oneops.boo.ClientConfig;
import com.oneops.boo.workflow.BuildAllPlatforms;
import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;
import com.planet57.gshell.util.cli2.Argument;
import com.planet57.gshell.util.cli2.Option;
import com.planet57.gshell.util.io.IO;
import org.sonatype.goodies.common.Time;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Invoke a procedure.
 */
@Command(name="boo/procedure", description = "Invoke a procedure")
public class ProcedureAction
  extends TemplateActionSupport
{
  @Argument(index = 0, required = true, description = "Platform name", token = "PLATFORM")
  private String platform;

  @Argument(index = 1, required = true, description = "Component name", token = "COMPONENT")
  private String component;

  @Argument(index = 2, required = true, description = "Action name", token = "ACTION")
  private String action;

  @Option(name="s", longName = "step-size", description = "Step size", token = "SIZE")
  private int stepSize = 100;

  // FIXME: cli2 should be able to cope with List<String> values

  @Nullable
  @Option(name="i", longName = "instances", description = "Invoke on specific instances", token = "INSTANCE[,INSTANCE]*")
  private String instances;

  @Override
  public Object execute(final @Nonnull CommandContext context) throws Exception {
    ClientConfig clientConfig = createClientConfig();
    BuildAllPlatforms flow = createFlow(clientConfig);
    IO io = context.getIo();

    // TODO: add arg-list

    // HACK: convert String to List<String>
    List<String> instances = null;
    if (this.instances != null) {
      instances = Splitter.on(',').trimResults().splitToList(this.instances);
    }

    log.debug("Invoking procedure; platform={}, component={}, action={}, instances={}", platform, component, action, instances);
    Long procedureId = flow.executeAction(platform, component, action, "", instances, stepSize);

    log.debug("Procedure ID: {}", procedureId);
    if (procedureId != null) {
      String status = "active";
      try {
        while (status != null && (status.equalsIgnoreCase("active") || status.equalsIgnoreCase("pending"))) {
          status = flow.getProcedureStatus(procedureId);
          log.debug("Status: {}", status);
          try {
            Time.seconds(3).sleep();
          }
          catch (InterruptedException e) {
            log.debug("Interrupted", e);
          }
        }
      }
      catch (OneOpsClientAPIException e) {
        log.debug("Failed to fetch status", e);
      }

      io.println(status);
    }

    return null;
  }
}

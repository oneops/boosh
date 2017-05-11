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

import com.oneops.boo.ClientConfig;
import com.oneops.boo.workflow.BuildAllPlatforms;
import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;
import com.planet57.gshell.util.io.IO;
import com.planet57.gshell.util.cli2.Argument;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * List assemblies.
 */
@Command(name="boo/list", description = "List assemblies")
public class ListAction
  extends TemplateActionSupport
{
  // FIXME: name here is confusing in BooCli; appears to be an assembly name or prefix?

  @Nullable
  @Argument(required = false, description = "Identifier", token = "IDENTIFIER")
  private String prefix;

  @Override
  public Object execute(final @Nonnull CommandContext context) throws Exception {
    ClientConfig clientConfig = createClientConfig();
    BuildAllPlatforms flow = createFlow(clientConfig);
    IO io = context.getIo();

    String query = prefix != null ? prefix : clientConfig.getYaml().getAssembly().getName();
    List<String> assemblies = flow.getAllAutoGenAssemblies(query);
    checkState(!assemblies.isEmpty(), "Nothing matched query: %s", query);
    assemblies.forEach(io::println);

    return null;
  }
}

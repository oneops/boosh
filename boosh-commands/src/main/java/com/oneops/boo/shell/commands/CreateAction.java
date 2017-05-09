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

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.oneops.boo.ClientConfig;
import com.oneops.boo.workflow.BuildAllPlatforms;
import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;
import com.planet57.gshell.util.cli2.Option;

/**
 * Create a new assembly.
 */
@Command(name="boo/create", description = "Create a new assembly")
public class CreateAction
  extends BooActionSupport
{
  @Option(longName = "no-deploy", description = "Disable deployment")
  private boolean disableDeploy;

  @Override
  public Object execute(@Nonnull final CommandContext context) throws Exception {
    ClientConfig config = createConfig();

    // auto-generate assembly name if configured
    if (config.getYaml().getAssembly().getAutoGen()) {
      assembly = randomName(assembly);
    }

    if (assembly != null) {
      config.getYaml().getAssembly().setName(assembly);
    }

    log.debug("Creating assembly: {}", config.getYaml().getAssembly().getName());

    BuildAllPlatforms flow = createFlow(config);

    flow.process(false, disableDeploy);

    return null;
  }

    private static String randomName(@Nullable final String basis) {
    if (basis == null) {
      return randomString("");
    }
    else {
      return String.format("%s-%s", basis, randomString(basis));
    }
  }

  private static String randomString(final String basis) {
    StringBuilder name = new StringBuilder();
    int rand = 32 - basis.length() - 1;
    rand = rand > 8 ? 8 : rand;
    name.append(UUID.randomUUID().toString().substring(0, rand));
    return name.toString();
  }
}

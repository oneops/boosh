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

import static com.google.common.base.Preconditions.checkState;

import java.io.File;

import javax.annotation.Nonnull;

import com.oneops.boo.ClientConfig;
import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandActionSupport;
import com.planet57.gshell.command.CommandContext;
import com.planet57.gshell.command.IO;
import org.ini4j.Ini;
import org.ini4j.Wini;

/**
 * Display configuration.
 */
@Command(name="boo/config", description = "Display configuration")
public class ConfigAction
  extends CommandActionSupport
{
  // TODO: consider adding options to get a specific value, as well as set a specific value

  // TODO: consider refactoring to all user to change the location of ~/.boo/config

  @Override
  public Object execute(@Nonnull final CommandContext context) throws Exception {
    File file = ClientConfig.ONEOPS_CONFIG;
    checkState(file.exists(), "Missing: %s", file);

    IO io = context.getIo();
    Ini ini = new Wini(file);
    log.debug("Loaded INI: {}", ini);

    ini.forEach((name, section) -> {
      io.format("[@|green %s|@]%n", name);
      section.forEach((key, value) -> {
        io.format("  @|bold %s|@: %s%n", key, value);
      });
    });

    return null;
  }
}

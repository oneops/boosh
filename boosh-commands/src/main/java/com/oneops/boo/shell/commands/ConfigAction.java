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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.annotations.VisibleForTesting;
import com.oneops.boo.ClientConfig;
import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandActionSupport;
import com.planet57.gshell.command.CommandContext;
import com.planet57.gshell.util.NameValue;
import com.planet57.gshell.util.cli2.Option;
import com.planet57.gshell.util.io.IO;
import org.ini4j.Ini;

/**
 * Get and set configuration options.
 */
@Command(name="boo/config", description = "Get and set configuration options")
public class ConfigAction
  extends CommandActionSupport
{
  // TODO: consider refactoring to all user to change the location of ~/.boo/config

  @Nullable
  @Option(name="g", longName = "get", description = "Get configuration value", token = "[SECTION/]KEY")
  private String getPath;

  @Nullable
  @Option(name="s", longName = "set", description = "Set configuration value", token = "[SECTION/]KEY=VALUE")
  private String setPathValue;

  @Nullable
  @Option(name="r", longName = "remove", description = "Remove configuration value", token = "[SECTION/]KEY")
  private String removePath;

  @Override
  public Object execute(@Nonnull final CommandContext context) throws Exception {
    File file = ClientConfig.ONEOPS_CONFIG;
    checkState(file.exists(), "Missing: %s", file);

    int count = 0;
    if (getPath != null) {
      count++;
    }
    if (setPathValue != null) {
      count++;
    }
    if (removePath != null) {
      count++;
    }
    checkState(count <= 1, "Only one of --set, --get or --remove is allowed");

    Ini ini = new Ini(file);
    log.debug("Loaded INI: {}", ini);

    IO io = context.getIo();

    if (getPath != null) {
      SectionKey sectionKey = SectionKey.parse(getPath);
      log.debug("Getting value of: {}", sectionKey);
      String value = ini.get(sectionKey.section, sectionKey.key);
      io.println(value);
    }
    else if (setPathValue != null) {
      NameValue nameValue = NameValue.parse(setPathValue);
      SectionKey sectionKey = SectionKey.parse(nameValue.name);
      log.debug("Setting value of: {} -> {}", sectionKey, nameValue.value);
      ini.put(sectionKey.section, sectionKey.key, nameValue.value);
      ini.store();
    }
    else if (removePath != null) {
      SectionKey sectionKey = SectionKey.parse(removePath);
      log.debug("Removing value of: {}", sectionKey);
      ini.remove(sectionKey.section, sectionKey.key);
      ini.store();
    }
    else {
      ini.forEach((name, section) -> {
        io.format("[@|green %s|@]%n", name);
        section.forEach((key, value) -> {
          io.format("  @|bold %s|@: %s%n", key, value);
        });
      });
    }

    return null;
  }

  /**
   * Helper to parse our "section/key" pattern from input.
   */
  @VisibleForTesting
  static class SectionKey
  {
    public static final String DEFAULT_SECTION = "default";

    public final String section;

    public final String key;

    private SectionKey(final String section, final String key) {
      this.section = checkNotNull(section);
      this.key = checkNotNull(key);
    }

    public static SectionKey parse(final String input) {
      int sep = input.indexOf('/');
      String section;
      String key;

      if (sep != -1) {
        section = input.substring(0, sep);
        key = input.substring(sep + 1, input.length());
      }
      else {
        section = DEFAULT_SECTION;
        key = input;
      }

      return new SectionKey(section, key);
    }

    @Override
    public String toString() {
      return "SectionKey{" +
        "section='" + section + '\'' +
        ", key='" + key + '\'' +
        '}';
    }
  }
}

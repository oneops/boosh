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

import com.oneops.boo.ClientConfig;
import com.oneops.boo.ClientConfigIniReader;
import com.oneops.boo.ClientConfigInterpolator;
import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;

import java.io.File;

import static com.google.common.base.Preconditions.checkState;

/**
 * Display template.
 */
@Command(name="boo/template", description = "Display template")
public class TemplateAction
  extends TemplateActionSupport
{
  @Override
  public Object execute(@Nonnull final CommandContext context) throws Exception {
    File iniFile = ClientConfig.ONEOPS_CONFIG;
    checkState(iniFile.exists(), "Missing: %s", iniFile);

    ClientConfigIniReader iniReader = new ClientConfigIniReader();
    checkState(iniReader.read(iniFile, profile) != null,
      "Invalid profile: %s", profile);

    ClientConfigInterpolator interpolator = new ClientConfigInterpolator();
    String yaml = interpolator.interpolate(template, iniFile, profile);

    context.getIo().println(yaml);

    return null;
  }
}

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
package com.oneops.boo.shell;

import static com.planet57.gshell.variables.VariableNames.SHELL_GROUP;
import static com.planet57.gshell.variables.VariableNames.SHELL_USER_DIR;

import java.io.File;

import javax.annotation.Nullable;

import com.planet57.gshell.branding.Asl2License;
import com.planet57.gshell.branding.BrandingSupport;
import com.planet57.gshell.branding.License;
import com.planet57.gshell.util.io.PrintBuffer;

/**
 * Branding for {@code boosh}.
 */
public class BrandingImpl
    extends BrandingSupport
{
  @Override
  public String getDisplayName() {
    return "@|bold,cyan Boo|@ @|bold Shell|@";
  }

  @Override
  public String getWelcomeMessage() {
    PrintBuffer buff = new PrintBuffer();
    buff.format("%s (%s)%n%n", getDisplayName(), getVersion());
    buff.println("Type '@|bold help|@' for more information.");
    buff.format("@|intensity_faint %s|@", LINE_TOKEN);
    return buff.toString();
  }

  @Override
  public String getGoodbyeMessage() {
    return "@|green Goodbye!|@";
  }

  @Override
  public String getPrompt() {
    // FIXME: may need to adjust ansi-renderer syntax or pre-render before expanding to avoid needing escapes
    return String.format("\\@\\|bold %s\\|\\@\\(${%s}\\):${%s}> ", getProgramName(), SHELL_GROUP, SHELL_USER_DIR);
  }

  @Nullable
  @Override
  public String getRightPrompt() {
    // FIXME: may need to adjust ansi-renderer syntax or pre-render before expanding to avoid needing escapes
    return "\\@\\|intensity_faint $(date)\\|\\@";
  }

  @Override
  public File getUserContextDir() {
    return new File(getUserHomeDir(), ".boo");
  }

  @Override
  public License getLicense() {
    return new Asl2License();
  }
}

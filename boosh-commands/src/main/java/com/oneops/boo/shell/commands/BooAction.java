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

import com.oneops.boo.BooCli;
import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandActionSupport;
import com.planet57.gshell.command.CommandContext;
import com.planet57.gshell.util.cli2.OpaqueArguments;

import javax.annotation.Nonnull;
import java.util.List;

// FIXME: this is a basic test of integration with BooCli; certainly not optimal for shell integration

/**
 * Execute Boo.
 */
@Command(name="boo/boo", description = "Execute Boo")
public class BooAction
  extends CommandActionSupport
  implements OpaqueArguments
{
  @Override
  public Object execute(final @Nonnull CommandContext context) throws Exception {
    BooCli boo = new BooCli();
    try {
      return boo.parse(strings(context.getArguments()));
    }
    finally {
      context.getIo().flush();
    }
  }

  private static String[] strings(final List<?> input) {
    String[] result = new String[input.size()];
    for (int i=0; i<result.length; i++) {
      result[i] = String.valueOf(input.get(i));
    }
    return result;
  }
}

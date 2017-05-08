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
package com.oneops.boo.shell.commands

import com.planet57.gshell.testharness.CommandTestSupport
import org.junit.Test

/**
 * Tests for {@link BooAction}.
 */
class BooActionTest
    extends CommandTestSupport
{
  BooActionTest() {
    super(BooAction.class)
  }

  /**
   * Customized help test as this isn't using the default help support by gshell.
   */
  @Override
  @Test
  void testHelp() {
    assert executeCommand('--help') == 0
    assert executeCommand('-h') == 0
  }
}

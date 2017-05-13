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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.oneops.api.resource.model.Deployment;
import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandActionSupport;
import com.planet57.gshell.command.CommandContext;
import com.planet57.gshell.util.io.IO;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

/**
 * Test action.
 */
@Command(name="boo/test")
public class TestAction
  extends CommandActionSupport
{
  @Override
  public Object execute(final @Nonnull CommandContext context) throws Exception {
    IO io = context.getIo();
    Terminal terminal = io.terminal;

    // TODO: how can we control the actual values written? PrettyPrinter only gets us surrounding syntax

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer().with(new PrettyPrinter()
    {
      protected DefaultPrettyPrinter.Indenter arrayIndenter = DefaultPrettyPrinter.FixedSpaceIndenter.instance;

      protected DefaultPrettyPrinter.Indenter objectIndenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;

      protected transient int nesting = 0;

      private String ansi(final AttributedStyle style, final String text) {
        AttributedStringBuilder buff = new AttributedStringBuilder();
        buff.style(style);
        buff.append(text);
        buff.style(AttributedStyle.DEFAULT);
        return buff.toAnsi(terminal);
      }

      @Override
      public void writeRootValueSeparator(JsonGenerator jsonGenerator) throws IOException {

      }

      @Override
      public void writeStartObject(JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeRaw(ansi(AttributedStyle.BOLD.foreground(AttributedStyle.CYAN), "{"));

        if (!objectIndenter.isInline()) {
          ++nesting;
        }
      }

      @Override
      public void beforeObjectEntries(JsonGenerator jsonGenerator) throws IOException {
        objectIndenter.writeIndentation(jsonGenerator, nesting);
      }

      @Override
      public void writeEndObject(JsonGenerator jsonGenerator, int nrOfEntries) throws IOException {
        if (!objectIndenter.isInline()) {
          --nesting;
        }
        if (nrOfEntries > 0) {
          objectIndenter.writeIndentation(jsonGenerator, nesting);
        }
        else {
          jsonGenerator.writeRaw(' ');
        }

        jsonGenerator.writeRaw(ansi(AttributedStyle.BOLD.foreground(AttributedStyle.CYAN), "}"));
      }

      @Override
      public void writeObjectEntrySeparator(JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeRaw(ansi(AttributedStyle.DEFAULT.faint(), ","));

        objectIndenter.writeIndentation(jsonGenerator, nesting);
      }

      @Override
      public void writeObjectFieldValueSeparator(JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeRaw(ansi(AttributedStyle.DEFAULT.faint(), ": "));
      }

      @Override
      public void writeStartArray(JsonGenerator jsonGenerator) throws IOException {
        if (!arrayIndenter.isInline()) {
          ++nesting;
        }

        jsonGenerator.writeRaw(ansi(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN), "["));
      }

      @Override
      public void beforeArrayValues(JsonGenerator jsonGenerator) throws IOException {
        arrayIndenter.writeIndentation(jsonGenerator, nesting);
      }

      @Override
      public void writeArrayValueSeparator(JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeRaw(ansi(AttributedStyle.DEFAULT.faint(), ","));

        arrayIndenter.writeIndentation(jsonGenerator, nesting);
      }

      @Override
      public void writeEndArray(JsonGenerator jsonGenerator, int nrOfValues) throws IOException {
        if (!arrayIndenter.isInline()) {
          --nesting;
        }
        if (nrOfValues > 0) {
          arrayIndenter.writeIndentation(jsonGenerator, nesting);
        }
        else {
          jsonGenerator.writeRaw(' ');
        }

        jsonGenerator.writeRaw(ansi(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN), "]"));
      }
    });

    Envelope envelope = new Envelope();
    envelope.comment = "fun!";
    envelope.map.put("foo", true);
    envelope.map.put("bar", 1234);
    envelope.map.put("baz", "qux");
    envelope.list.add(1);
    envelope.list.add(2);
    envelope.list.add(false);
    envelope.list.add("foo");

    io.println("@|intensity_faint -----8<-----|@");

    Writer writer = new Writer() {
      @Override
      public void write(char[] cbuf, int off, int len) throws IOException {
        io.out.write(cbuf, off, len);
      }

      @Override
      public void flush() throws IOException {
        io.out.flush();
      }

      @Override
      public void close() throws IOException {
        // ignore
      }
    };
    objectWriter.writeValue(writer, envelope);
    io.println();

    io.println("@|intensity_faint ----->8-----|@");

    return null;
  }

  public static class Envelope
  {
    public String comment;

    public Map<String,Object> map = new HashMap<>();

    public List<Object> list = new ArrayList<>();
  }
}

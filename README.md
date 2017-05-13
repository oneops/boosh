<!--

    Copyright 2017-present Walmart, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
# Description

[Boo](https://github.com/oneops/boo) Shell (`boosh`)

Based on [GShell 3](https://github.com/jdillon/gshell).

**WARNING** Work-in-progress; GShell 3 and Boosh are undergoing rapid changes.

# License

[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

# Building

## Requirements

* [Maven](http://maven.apache.org) 3.5+ (prefer included maven-wrapper)
* [Java](http://java.oracle.com/) 8+

Check out and build:

    git clone git://github.com/oneops/boosh.git
    cd boosh
    ./build rebuild

After this completes, you can unzip the assembly and launch the shell:

    ./build run

# Running

The `build run` here is just a simple helper to run the previously built assembly, and is equivalent to:

    ./target/boosh-*/bin/boosh

Use the 'help' command for further assistance.

    help boo

Should produce something like:

    Help pages in group boo:
      config      Get and set configuration options
      create      Create a new assembly
      get-ips     Display IP addresses
      list        List assemblies
      procedure   Invoke a procedure
      remove      Remove deployed configurations
      retry       Retry deployments
      status      Get status of deployments
      template    Display template
      update      Update assemblies

Commands can be executed w/o an interactive shell as well:

    ./build run boo/config

Produces something similar to:

    [default]
      cloud: prod-cdc6
      username: jdillo3
      email: jdillon@walmartlabs.com
      host: https://oneops.prod.walmart.com
      api_key: XXXX
      organization: devtools

# Known Issues

## TERM=ansi

  `TERM=ansi` is known to cause strange behavior.  Workaround is to use `TERM=xterm-256color` or similar.
  
  ie. `export TERM=xterm-256color` in the native shell to configure the TERM.
  
  https://github.com/jline/jline3/issues/123

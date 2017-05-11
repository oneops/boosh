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

Use the 'help' command for further assistance.

    help boo

Should produce something like:

    Help pages in group boo:
      config      Display configuration
      create      Create a new assembly
      get-ips     Display IP addresses
      list        List assemblies
      procedure   Invoke a procedure
      remove      Remove deployed configurations
      retry       Retry deployments
      status      Get status of deployments
      template    Display template
      update      Update assemblies

# Known Issues

## Unfinished Commands

The following commands are yet to be finished:

* boo/update
* boo/procedure

## TERM=ansi

  `TERM=ansi` is known to cause strange behavior.  Workaround is to use `TERM=xterm-256color` or similar.
  
  https://github.com/jline/jline3/issues/123

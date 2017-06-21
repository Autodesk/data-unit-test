<!---
Copyright 2016 Autodesk,Inc.

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

# Contributing to data-unit-test

Contributions to this project are encouraged! All you need is a Github account to contribute.

## Filing Issues

**Suggestions**

The data-unit-test project is meant to evolve with feedback - the project and its users greatly appreciate any thoughts on ways to improve the design or features. Please use the `enhancement` tag to specifically denote issues that are suggestions - this helps us triage and respond appropriately.

**Bugs**

As with all pieces of software, you may end up running into bugs. Please submit bugs as regular issues on GitHub - data-unit-test developers are regularly monitoring issues and will try to fix open bugs quickly.

The best bug reports include a detailed way to predictably reproduce the issue, and possibly even a working example that demonstrates the issue.


### Contributing Code

The data-unit-test project accepts and greatly appreciates contributions. The project follows the [fork & pull](https://help.github.com/articles/using-pull-requests/#fork--pull) model for accepting contributions.

When contributing code, please also include appropriate tests as part of the pull request, and follow the same comment and coding style as the rest of the project. Take a look through the existing code for examples of the testing and style practices the project follows.

### Contributing Features

All pull requests for new features must go through the following process:
* Start an Intent-to-implement GitHub issue for discussion of the new feature.
* LGTM from Tech Lead and one other core committer is required
* Development occurs on a separate branch of a separate fork, noted in the intent-to-implement issue
* A pull request is created, referencing the issue.
* data-unit-test developers will provide feedback on pull requests, looking at code quality, style, tests, performance, and directional alignment with the goals of the project. That feedback should be discussed and incorporated
* LGTM from Tech Lead and one other core committer, who confirm engineering quality and direction.

### See Also

* The [GOVERNANCE](GOVERNANCE.md) model

## Quickstart

**What Can I Do?**

Here are some ideas:


**Contribute unit tests** - test that the framework is working in all scenarios as expected. Build Unit tests for the same and report/fix in case its not the case.

**Bug fixes:** Found a typo in the code? Found that a function fails under certain conditions? Know how to fix it? Great! Go for it. Please [open an issue](https://git.autodesk.com/cloudplatform-bigdata/DataUnitTest/issues/new) so that we know you're working on it, and submit a pull request when you're ready.

**Feature Enhancements:** Want a new feature to be added? Know something super cool can be a part of the project? Go for it. Please [open an issue](https://git.autodesk.com/cloudplatform-bigdata/DataUnitTest/issues/new) so that we know you're working on it, and submit a pull request when you're ready.

**Whatever:** There's ALWAYS something to do.

### Pull requests are always welcome

All PRs should be documented as [GitHub issues](https://git.autodesk.com/cloudplatform-bigdata/DataUnitTest/issues), ideally BEFORE you start working on them.

## Submission Guidelines

### Project Roles

Ashwini Kumar - Engineer

Yann Landrin-Schweitzer - Architect

Sarang Anajwala - Product Owner

### Timing

We will attempt to address all issues and pull requests within one week. It may a bit longer before pull requests are actually merged, as they must be inspected and tested.

### Issues

If your issue appears to be a bug, and hasn't been reported, open a new issue.
Help us to maximize the effort we can spend fixing issues and adding new features, by not reporting duplicate issues. A template is provided when submitting an issue, and providing the following information will increase the chances of your issue being dealt with quickly:

* **Overview of the Issue** - if an error is being thrown a non-minified stack trace helps
* **Motivation for or Use Case** - explain why this is a bug for you
* **Database** - is this a problem with all databases or with any specific one?
* **Related Issues** - has a similar issue been reported before?
* **Suggest a Fix** - if you can't fix the bug yourself, perhaps you can point to what might be
  causing the problem (line of code or commit)

### Pull Requests

Before you submit your pull request consider the following guidelines:

* Search GitHub for an open or closed Pull Request that relates to your submission. You don't want to duplicate effort.
* Make your changes in a new git branch:

     ```shell
     git checkout -b my-fix-branch master
     ```

* Create your patch.
* Commit your changes using a descriptive commit message.

     ```shell
     git commit -a
     ```
  Note: the optional commit `-a` command line option will automatically "add" and "rm" edited files.

* Push your branch to GitHub:

    ```shell
    git push origin my-fix-branch
    ```

* In GitHub, send a pull request to `DataUnitTest:master`.

### Coding Rules

* Please maintain a code style similar to that of the rest of the project.
* Please document all public methods using JSDoc format comments.

### Tests

[Testing Documentation](tests/README.md)

Tests will be run to ensure no regressions are introduced in new code.

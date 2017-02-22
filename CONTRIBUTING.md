# Contributing to DataUnitTest

Contributions to this project are encouraged! All you need is a Github account to contribute.

Please ask questions by [opening an issue](https://git.autodesk.com/cloudplatform-bigdata/DataUnitTest/issues/new).

## Quickstart

To get started, as always: [open an issue](https://git.autodesk.com/cloudplatform-bigdata/DataUnitTest/issues/new).

Make your code changes, and submit a pull request. Add tests and update documentation when appropriate. Fill out the pull request template entirely on GitHub.

You must sign our Contributor License Agreement (CLA) before pull requests can be accepted, which will pop up as a comment after submitting your pull request.

Admins will respond quickly, and may request changes, on the pull request.

## What Can I Do?

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

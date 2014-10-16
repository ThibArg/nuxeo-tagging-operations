# nuxeo-tagging-operations
===

This plug-in adds operations to add/remove tags in a Document.

* [Usage](#usage)
* [Installation](#installation)
* [Building the Plugin](#building-the-plugin)
* [License](#license)
* [About Nuxeo](#about-nuxeo)

## Usage

Once installed, the plug-in adds the following operations, that can be used in Automation Chains (or direct REST call)

* **Document > Tag Document** (Operation ID: `Document.Tag`)
  * This operation expects one required parameter: `labels`.
    * `labels` is a list of tags, separated by a comma.
    * The `TagService` cleans up the labels (removes spaces and punctuation, ...)
  * It adds each tag to the document (does nothing for a tag which already exist for the document)
    * Example: `tag1,TAg2, and tag3` will add the tags `tag1`, `tag2` and `andtag3`removes

* **Document > Untag Document** (Operation ID: `Document.Untag`)
  * This operation expects one required parameter: `labels`.
    * `labels` is a list of tags, separated by a comma.
    * The `TagService` cleans up the labels (removes spaces and punctuation, ...)
  * It adds each tag from the document (does nothing if the tag is not already in the document)
    * Example: `tag1,TAg2, and tag3` will add the tags `tag1`, `tag2` and `andtag3`

* **Document > Remove Tags from Document** (Operation ID: `Document.RemoveTags`)
  * This operation has no parameters and removes all tags from the document

## Installation

The easiest way to install is to download the `nuxeo-tagging-operations-{version}.zip` file from the "Releases" tab of this repository and to install it from the Admin Center (or via the `nuxeoctl` command line):

Download this .zip Marketplace Package and install it on your server:
* Either from the Admin. Center:
  * As administrator (Administrator/Administrator by default), in the Admin Center, click on the `Update Center` left tab.
  * Click on the `Local packages` tab.
  * Click on the `Upload a package` button.
    * An upload form is displayed just below the tabs.
    * Click on the `Browse button` to select the package .zip package file.
    * Click on the `Upload` button.
    * Once the .zip is uploaded, install it by clicking on the `Install` link
    * A confirmation page is displayed, click the `Start` button, and follow the instruction if needed
* Or from the `nuxeoctl` command line:
  * Make sure the server is not running, stop it if needed
  * Then you can `nuxeoctl mp-install /path/to/the/zip/package` and answer "yes" to the confirmation.
  * (then, restart the server)

## Building the Plugin
You can also download the source code and compile the plug-in. Which is what you will do if you want to change, adapt, etc.
Assuming [`maven`](http://maven.apache.org) (min. 3.2.1) is installed on your computer:
```
# Clone the GitHub repository
cd /path/to/where/you/want/to/clone/this/repository
git clone https://github.com/ThibArg/nuxeo-tagging-operations
# Compile
cd nuxeo-tagging-operations
mvn clean install
```

* The plug-in is in `nuxeo-tagging-operations/nuxeo-tagging-operations-plugin/target/`, its name is `nuxeo-tagging-operations-plugin-{version}.jar`.
* The Marketplace Package is in `nuxeo-tagging-operations/nuxeo-tagging-operations-mp/target`, its name is `nuxeo-tagging-operations-mp-{version}.zip`.

If you want to import the source code in Eclipse, then after the first build, `cd nuxeo-tagging-operations-plugin` and `mvn eclipse:eclipse`. Then, in Eclipse, choose "File" > "Import...", select "Existing Projects into Workspace" navigate to the `nuxeo-tagging-operations-plugins` folder and select this folder.


## License
(C) Copyright 2014 Nuxeo SA (http://nuxeo.com/) and others.

All rights reserved. This program and the accompanying materials
are made available under the terms of the GNU Lesser General Public License
(LGPL) version 2.1 which accompanies this distribution, and is available at
http://www.gnu.org/licenses/lgpl-2.1.html

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

Contributors:
Thibaud Arguillere (https://github.com/ThibArg)

## About Nuxeo

Nuxeo provides a modular, extensible Java-based [open source software platform for enterprise content management](http://www.nuxeo.com) and packaged applications for Document Management, Digital Asset Management and Case Management. Designed by developers for developers, the Nuxeo platform offers a modern architecture, a powerful plug-in model and extensive packaging capabilities for building content applications.

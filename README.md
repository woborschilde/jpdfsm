# Java PDF Select &amp; Merge
This is the Java brother of the little [utility](https://github.com/woborschilde/pdfsm) useful for summarizing a lot of PDF documents:

![How it works info graphic](https://raw.githubusercontent.com/woborschilde/pdfsm/master/web-img/pdfsm_how-it-works.png "")

jpdfsm is written in Java and requires at least Java 10. We've tested cross-platform support with Windows 10 (1607), Linux Mint (19.1 Cinnamon) and Mac OS X (El Capitan 10.11) using OpenJDK 10.0.2.

The program uses the iText PDF library (this also works with non-Adobe-standard documents) and also includes ini4j in order to read/write INI config and selector files. Dependencies can be automatically fetched via Maven. Java PDF Merge is distributed under the GPLv3 license.

**For this program to work you need to create a selection file first!** This is described in the linked wiki article below.

## Wiki
*Note:* Some of these instructions refer to the original pdfsm as they are the same for both editions.
 - [How selection files are created](https://github.com/woborschilde/pdfsm/wiki/INI-Selection-File)
 - [Troubleshooting](https://github.com/woborschilde/jpdfsm/wiki/Troubleshooting)

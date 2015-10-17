WebServer
==============

A simple webserver for java developers.

Features
==========

* Full control of response header
* Full control of request by user
* Ability to have static or "dynamic" pages
* "Dynamic" pages are made by making a class extend Page
* Custom 404 page
* Config Support (WIP)
* Password manager

How it works
==========
1. Place jar in folder
2. Put config.txt in root folder
3. Place HTML files in the www folder
4. Place Classes that extend Page in the www folder
5. Run the jar

Versions
===========
* v1.0.5
  * Classes are now read when requested not on startup
  * Removed all commands
* v1.0.4
 * No longer uses json for config
 * Added commands
 * Can use classes for pages
* v1.0.3
  * Added config
  * Renamed all the packages
* v1.0.1
  * Moved 404 
  * Added Content-Length to header 
  * Page class no longer needs Server instance
* v1.0
  * Had a fairly working webserver.

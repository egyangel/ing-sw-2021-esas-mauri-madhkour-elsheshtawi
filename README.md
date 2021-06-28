# ing-sw-2021-esas-mauri-madhkour-elsheshtawi

##INIT PROJECT

git clone https://github.com/egyangel/ing-sw-2021-esas-mauri-madhkour-elsheshtawi.git .

# Prova Finale Ingegneria del Software 2020

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**

- [Features implemented](#features-implemented)
- [Building](#building)
    - [Building with Maven](#building-with-maven)
    - [Using provided jars](#using-provided-jars)
- [Server](#server)
    - [Use our server](#use-our-server)
    - [Run your server](#run-your-server)
- [Client](#client)
    - [Compatibility notes for cli](#compatibility-notes-for-cli)
- [Documentation](#documentation)
    - [Javadoc](#javadoc)
    - [UML diagrams](#uml-diagrams)
    - [Coverage report](#coverage-report)
- [Gameplay screenshots](#gameplay-screenshots)
- [External libraries used](#external-libraries-used)
- [Authors (alphabetical order)](#authors-alphabetical-order)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Features implemented
| Functionality | State |
|:--------------------------------------|:------------------------------------:|
| Basic rules                           | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Complete rules                        | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI                                   | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |


## Building
### Building with Maven
To build the project with Maven we offer the following profiles
* server : ` mvn package -Pserver `
* client : ` mvn package -Pclient `

For a faster build you can skip the unit tests activating also the profile `-Pnotest `

The output directory for the jars is set to [/deliveries/final/jar](/deliveries/final/jar)

### Using provided jars

If you want you can use the jars we already built for you, you can find them [here](/deliveries/final/jar)


## Server

### Run your server
If you want to run the server on your machine you can use the following command, this is the easiest way to run it
```bash
java -jar server.jar
```

## Client
By default, the client jar loads the GUI, if you wanna use the command line interface please add `cli` as argument. Otherwise, you can also double-click the jar file
```bash
java -jar client.jar
```
### Compatibility notes for cli
The command line version of the game is optimized for unix-like terminals.


## Documentation

### Javadoc
Javadoc is available at this link: [http://santorini40.xyz/javadoc](http://santorini40.xyz/javadoc)  
Javadoc is also in this repo [here](/deliveries/final/javadoc)
### UML diagrams
UML diagrams are [here](/deliveries/final/uml)

### Coverage report
The final report of the coverage is [here](/deliveries/final/report)

## External libraries used

| Library | Link
| ----------| --------------------------------------- |
| GSON      | https://github.com/google/gson          |

## Authors (alphabetical order)
* [ Tiberio Galbiati](https://github.com/TiberioG)
* [ Andrea Lampis ](https://github.com/sup3rgiu)
* [ Vito Alessandro Monaco](https://github.com/Vito96)



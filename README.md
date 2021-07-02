# ing-sw-2021-esas-mauri-madhkour-elsheshtawi

##INIT PROJECT

git clone https://github.com/egyangel/ing-sw-2021-esas-mauri-madhkour-elsheshtawi.git .

# Prova Finale Ingegneria del Software 2021

**Table of Contents**

- [Features implemented](#features-implemented)
- [How to play the game](#how-to-play-the-game)
- [Building](#building)
    - [Using provided jars](#using-provided-jars)
- [Server](#server)
    - [Run your server](#run-your-server)
- [Client](#client)    
- [Documentation](#documentation)  
    - [UML diagrams](#uml-diagrams)
- [External libraries used](#external-libraries-used)
- [Authors (alphabetical order)](#authors-alphabetical-order)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Features implemented
| Functionality |  
|:--------------------------------------|
| Basic rules                           | 
| Complete rules                        | 
| CLI                                   | 


## How to play the game

1) First step : run the server. The port number is 30000,and it is hardcoded in the server class.


2) Second Step : run the first client. If you play: 
                                                   
   - locally the ip of the router is "localhost" while port number as written before.
   - on a different network you should use as ip the public of your PC.
  

3) The view of the first player should ask name and number of the player than wait till there are enough people.    


4) (for example if there are 2 player) after the connection of the second player and set up of the name, randomly the server assign a turn order of the player


5) Then the server send to the player 4 leader card and ask to choose two of them. After the choice the first player received the menu of the action while the second player choose a free resource w.r.t. game's rule   

## Building
### Using provided jars

If you want you can use the jars we already built for you, you can find them [here](/deliveries/final/Jar)


## Server

### Run your server
To run the server on your machine you can use the following command, this is the easiest way to run it
```bash
java -jar Server.jar
```

## Client
To run the client you can use the following command, this is the easiest way to run it
```bash
java -jar Client.jar
```

## Documentation

### UML diagrams
UML diagrams are [here](/deliveries/final/UML)

### Sequence Diagrams
The final report of the coverage is [here](/deliveries/final/UML/sequenceDiagram)

## Gameplay screenshots
Gameplay screenshots are in this folder [here](/deliveries/final/screenShotOfTheGame)

## External libraries used

| Library | Link
| ----------| --------------------------------------- |
| GSON      | https://github.com/google/gson          |

## Authors (alphabetical order)
* [ Mohamed	Elsheshtwai ]
* [ Omer Esas ]
* [ Amor Madhkour ]
* [ Lorenzo	Mauri]



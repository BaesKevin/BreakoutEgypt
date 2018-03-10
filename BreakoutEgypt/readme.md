# Breakout 

## Installation instructions

Clone this repo in a folder, using netbeans works.
Open the project in netbeans, ignore the error about the deployment descriptor.
"Project problems" is about missing jars. Download them and add them to libraries.
Add the missing jar files for the JBox2d and gson libraries. 
* https://mvnrepository.com/artifact/org.jbox2d/jbox2d-library/2.1.2.2
* https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.2

To generate the database for this project, run breakout-database-groep25.sql in mysql workbench. The appliction expects a user with username usrbreakout and password TIbreakout2017 with select, insert update and delete privileges on database dbbreakout.

Finally, set the default browser for the project and you should be able to surf to localhost:8080/contextpath where contextpath is 
specified right-click project > properties > run.

This project requires GlassFish server 4.1 to run.

## Java

These are the core classes for the project: 

* RegularBody: the base class for all objects in levels (Brick, Ball,...). 
* BreakoutWorld: responsible for adding or removing RegularBody objects to the physics engine. The step function is what runs the simulation.
* LevelState: keeps track of all objects in a level
* Level: coordinates LevelState and BreakoutWorld and keeps of scores. Levels are created by levelfactories. Level is also in charge of creating TimerTasks to run the gamesimulation.
* SessionManager: keeps track of player connections.
* Game: entrypoint for everything, holds a level and sessionmanager
* GameManager: keeps track of all games on the server and creates and removes games.

Other important classes and interfaces:

* PlayerConnection: interface to create real or fake connections.
* BreakoutContactListener: eventlistener so we can listen for contacts in the physics engine.
  * Contact: our interface for all contacts we want to listen for.
* ServerClientMessageRepository: holds all messages that should be sent to clients after handling one iteration of the simulation, is emptied after each iteration. Message is the interface for all messages that can be added to this repository.
* Effect: interface for all effects that bricks can have (explosive,toggle,...). Effects are handled by BreakoutEffectHandler.
* PowerUp, PowerDown are the interfaces for powers and are handled by BreakoutPowerUpHandler and BreakoutPowerDownHandler.
* LevelFactory: interface for all levelfactories for different levelpacks (arcade, multiplayer, test).

BreakoutTestSuite.java tests for close to everything in the application. Testing uses DummyConnections and doesn't create timertasks so everything can be tested. Testing doesn't load levels from the database. A lot of the tests are integration tests, there are not that much pure unit tests.




# Breakout 

## installation instructions

Clone this repo in a folder, we didn't test cloning via netbeans, use cli.
Open the project in netbeans, ignore the error about the deployment descriptor.
"Project problems" is about missing jars. Download them and add them to libraries.
Add the missing jar files for the JBox2d and gson libraries. 
* https://mvnrepository.com/artifact/org.jbox2d/jbox2d-library/2.1.2.2
* https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.2

Under 'Web Pages', add a folder 'WEB-INF'. It should be on the same level as index.html. It doesn't contain anything, so git doesn't pick it up, but netbeans won't run the project without it.

Finally, set the default browser for the project and you should be able to surf to localhost:8080/contextpath where contextpath is 
specified right-click project > properties > run.

## Overview

All breakout gamelogic happens in a java backend that runs on GlassFish server.
Java backend uses JBox2d (a port of Box2d, written in c++). A Websocket servlet keeps the client up to. The client connects to the websocketservlet to get the gamestate and to send the position of his paddle. Client uses canvas to draw the gamestate. User controlls the paddle with the mouse.

## Java

### Websocket

**BreakoutGameServlet** Starts the simulation. At the moment a new simulation is started for each client that connects. This starts a *Timer* that runs 60 times per second. This uses **MoveCommandDecoder** and **JsonMoveCommand** to receive commands from the client. The servlet then tells the world to move the paddle to the specified coordinates.

**GameSimulater** is a TimerTask that has a *run* method that is called by the *Timer* in BreakoutGameServlet.

**ClientUpdater** is responsible for sending gamestate to connected clients. State is sent in json format. 

### Box2d world
The GameSimulator holds a reference to  BreakoutWorld and ClientUpdater objects. 

BreakoutWorld is responsible for initializing and managing the Box2d world, so adding walls, game objects, moving objects,... should be done here (or delegated to Box2dObjects).

World is the Box2d superobject. The constructor takes a vector that describes the force of gravity (0 in our case). It also allows objects to be added to the world.

#### Box2d body creation###

Creating physics enabled objects in the world has a couple of steps:

* create a BodyDefinition
* Create a shape
* Create a fixture
* Create a body, add the fixture to the body and set user data for the body (important for collision event handling)

A shape defines the dimensions of an object. At this point it doesn't have mass, velocity,restitution,...

A Fixture defines physical properties for a shape, and uses the shape to set physical dimensions.

world.createBody takes the bodydefinition to make a body. You then set the fixture on the body to complete the creation. If you don't need collision event handling you can stop here, if you do need it, set the UserData to something relevent. 

#### Box2d collision event handling####

**BreakoutContactListener** is called on each collision event in the world. At the moment the only relevant collision is between ball and brick. Listener then asks the BreakoutWorld to remove the brick.

#### Breakout box2d objects####

Brick, ball and paddle are objects that know how to create a Box2d body. They all implement **Box2dObject**, an interface that says these objects should be able to give a json representation of themselves and return a Body object (interface is not from Box2d library). The BreakoutWorld holds a reference to all the objects and ClientUpdater uses these to easily send a json representation of all objects to the client.

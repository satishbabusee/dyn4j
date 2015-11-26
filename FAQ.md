

## What is dyn4j? ##
dyn4j is a 2D collision detection and rigid body physics engine written 100% in the Java programming language.  The library has zero dependencies and is primarily developed for game development.

## What does the license allow? ##
dyn4j uses the New BSD license.  You may use the library in commercial applications without releasing your source, modifications to the library, or citing attribution.

## How do I get started using dyn4j? ##
See the GettingStarted wiki page first.  This will help you setup your project and give you a few tips to get going.  From there I recommend that you read the [Advanced](Advanced.md) wiki page to learn more about key aspects of the engine.  Then you can check out the ExampleListing page to see the list of examples (all the examples are checked into source control and can be found by browsing the source).  Last, I would take a look at the TestBed and Sandbox applications.

After reading the documentation, I would suggest you copy either ExampleGraphics2D or ExampleJOGL and attempt to get that working first, then modify from there.

## What platforms are suppored? ##
Any platform that has a Java virtual machine.  It has been tested on Windows XP/Vista/7 and Mac OS (Snow Leopard).

## I need help, where do I go? ##
First you should read through the wiki pages to make sure you are not missing some critical information.  Next you should take a look at the latest [Javadoc](http://docs.dyn4j.org).  If you still don't find an answer to your problem, don't hesitate to post a topic on the [forum](http://forum.dyn4j.org).

## What should I know to be able to use the library? ##
You should, at a minimum, be very familiar with the Java programming language and the tools used to compile and run java applications (command line or IDE).

I would recommend that you have some basic knowledge of physical properties like, force, torque, velocity, mass, etc.  In addition, it would be beneficial if you have some experience with Java2D or OpenGL.

## Why don't I see anything when I use the library? ##
The library leaves the rendering/drawing up to you.  Java has a few options for rendering, standard Java2D that is packaged with the runtime or 3rd party libraries like JOGL.  You can see the ExampleListing page for a Graphic2D example and a JOGL example.  The choice of rendering API is totally up to you.  The Sandbox application uses JOGL.

## The simulation isn't behaving correctly, what should I do? ##
  1. Make sure you are using the MKS (meter-kilogram-second) system of measure (not Imperial or pixels) for length and mass.  See the UnitConversion class.
  1. Make sure you understand that dyn4j does not produce pixel perfect results.  See the Settings class.
  1. Be very careful when reusing Shape instances among bodies.
  1. Read the [Javadoc](http://docs.dyn4j.org) carefully.  One of the main focuses of the project was documentation, especially within the source.
  1. Take a look at the ExampleListing wiki page and the Sandbox application for examples of how to properly use features of the library.
  1. Post a question on the [forum](http://forum.dyn4j.org).
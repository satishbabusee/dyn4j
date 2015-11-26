You can post any questions on the <a href='http://forum.dyn4j.org/'>forums</a>, but please read this page first before posting.



# Download #
**Google Code no longer allows downloads to be uploaded to projects.**

Latest versions of dyn4j can be found on **[Maven Central](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.dyn4j%22%20AND%20a%3A%22dyn4j%22)**.  Older versions will remain on the Google Code downloads page.

The javadocs can be found in the maven distributable or online <a href='http://docs.dyn4j.org'>here</a>.  You can browse the source <a href='http://code.google.com/p/dyn4j/source/browse/'>here</a>.

All binaries are compiled under Java 1.6 and have no dependencies.

If you are intent on building the source from the repository here are few tips:
  * The trunk branch is the development branch and will contain the latest code but may be unstable.
  * For stable builds look in the tags folder and connect to the desired release version.
  * There may be other branches available in the branches folder, labeled for specific purposes.



# Add To The Classpath #
To see the classes you must add the JAR to the classpath of your project.
## Eclipse ##
  1. Copy the dyn4j.jar to your project in eclipse.
  1. Right click on the dyn4j.jar and select Build Path → Add to Build Path

Watch the video below for a full description on how to get a new Eclipse project setup to use dyn4j:

<a href='http://www.youtube.com/watch?feature=player_embedded&v=P9G7Yn6GR-w' target='_blank'><img src='http://img.youtube.com/vi/P9G7Yn6GR-w/0.jpg' width='425' height=344 /></a>

 NOTE: The [ExampleGraphics2D](https://code.google.com/p/dyn4j/source/browse/trunk/examples/org/dyn4j/examples/ExampleGraphics2D.java) class now requires the [Graphics2DRenderer](https://code.google.com/p/dyn4j/source/browse/trunk/examples/org/dyn4j/examples/Graphics2DRenderer.java) class as well.

 NOTE 2: Google Code no longer houses the latest builds.  You can find the latest builds on [Maven Central](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.dyn4j%22%20AND%20a%3A%22dyn4j%22).

## Command Line ##
When building your project on the command line add the jar in the -classpath flag for example:
```
javac ... -classpath /path/to/jars/dyn4j.jar
```

## JAR Signing ##
Currently the releases are not signed.  This will cause the user of your application to receive an error message (if you deploy via JNLP or Applet and require all permissions) about the jar not being signed.

In the meantime, you will need to sign the jars yourself.  Later releases will be signed using an CA if there is enough demand.



# The Basics #
Let's start with some basics about how your game interfaces with the physics engine.  Any real world object that will need to interact with other objects is called a Body.  Bodies interact with one another in a World.  Bodies can also be joined together with other Bodies by Joints.

To setup your scene:
  1. Create a World instance
  1. Create your Bodies and add them to the World
  1. Create your Joints and add them to the World
  1. Call the World.update(double) method in your game loop

Take a look at the examples provided in the [Examples Package](http://code.google.com/p/dyn4j/source/browse/#svn%2Ftrunk%2Fexamples%2Forg%2Fdyn4j%2Fexamples) of the source: ExampleGraphics2D and ExampleJOGL for a starting point.

## Creating The World ##
The World class is in the dynamics package.  To create the World object for your game you only need to pass it a Bounds object.  The Bounds object controls the maximum bounds of the scene.  If an body travels (fully) outside the bounds the state of the body will be set to "inactive" and will no longer be interacting with the World or other bodies.

Most games will use the provided AxisAlignedBounds class which defines a rectangular area.  You can also use the default constructor of the World class to create a World that has no Bounds.  Be careful when doing this since numeric overflow can happen.

Once the World is created there a number of things you may want to configure.  The most common configurable item is gravity.  The default gravity is 9.8 m/s pointing in the negative y direction.  Refer to the [Javadocs](http://docs.dyn4j.org) for more information on other configuration items.

## Creating Bodies ##
A Body represents any object in your scene that you want to interact with other objects using physics.  A body can be comprised of one or more Convex Shapes.  Each shape must be wrapped in a BodyFixture.  A BodyFixture provides a clear distinction between geometry and dynamics by adding the extra information for a shape such as density, friction, and restitution.

Here is a simple diagram explaining the relationship:

<img src='http://www.dyn4j.org/images/body-structure.png' />

There are a number of Convex Shapes provided including Circle, Polygon, Rectangle, etc all contained in the geometry package.

Here are a few tips to creating shapes:
  * You can also use any methods in the Geometry class for alternate ways to create shapes.
  * Rectangle and Circle objects created always have their center at (0, 0).  Segments, Polygons, and Triangles have their centers at the center of the points (average or area based center).
  * Some of the Shape creation methods in the Geometry class create shapes with their centers at the origin.  All the methods in the Geometry class make copies of the input points and arrays.
  * Only convex shapes are supported.  Concave shapes are supported by decomposing the concave shape into multiple convex shapes (dyn4j provides classes to perform the decomposition of simple polygons).

After creating the desired shape you can do two things to add it to a body.  You can create a new BodyFixture object passing the shape to it, configure it, then add it to the body by using the addFixture method.  Or you can use the addFixture(Convex) method to create a default fixture that will automatically be added to the body and returned so you can configure it.

If you use multiple shapes for a body use the Shape class's translate and rotate methods to place the shape at the right position and orientation in local coordinates.
  * Note: This directly modifies the shape's vertices and center.

The mass of a body is created by its respective shapes.  Each shape type contains a createMass method which will use geometry and the density configured in its respective BodyFixture to create a Mass object.  The density units are kg / m<sup>2</sup>.  The density for a shape is set via the setDensity method in the BodyFixture class.

After adding Shapes/BodyFixtures to your body, be sure to call one of the setMass methods to set the mass.  The default mass is an infinite mass centered at the origin.
  * You can use the Mass.Type parameter to create specialized masses like infinite mass bodies.

The setMass methods will also compute what is called the rotation disc radius.  This is used in continuous collision detection and is dependent on the center of mass being computed.
  * The rotation disc radius is the radius of the disc created by a rotating body (imagine a body rotated 360 degrees about its center).

Finally add the body to the World by calling the World.addBody(Body) method.

## Creating Joints ##
A Joint represents a connection between two bodies that limit motion.  A connection might be a frame of a car connecting two wheels or a spring connecting a wheel to a car frame.

All Joint classes are contained in the dynamics.joint package.  Each joint class may have a different constructor depending on the type.

Once the joint is created it can be added to the World by calling the World.addJoint(Joint) method.

See the [Joints](Joints.md) page for more details about each Joint.

## General Creation Tips ##
The BodyFixture, Body, Joint, and World classes, among others, typically only have minimal constructors.  This allows for less confusion in the source.

For example the DistanceJoint class only accepts two anchor points and the bodies.  To use the optional features, call the setXXX methods after creation of the object:

```
// creates a standard fixed distance joint
DistanceJoint dj = new DistanceJoint(b1, b2, p1, p2);
// to make it a spring-damper
dj.setFrequency(8.0);
dj.setDampingRatio(0.4);

// or for a RevoluteJoint
RevoluteJoint rj = new RevoluteJoint(wheel, frame, pivot);
// to use a motor
rj.setMotorSpeed(Math.PI);
rj.setMaximumMotorTorque(1000);
rj.setMotorEnabled(true);

// or for a fixture
BodyFixture f = new BodyFixture(rect);
f.setDensity(1.2);
f.setFriction(0.6);

// or for a body
Body b = new Body();
b.addFixture(f);
b.setLinearDamping(0.97);
b.setVelocity(new Vector2(0.0, 2.0));
```

## Integrating Into The Game Loop ##
Most, if not all, games use active rendering and something called a "game loop" to poll for input, update graphics, etc.  To allow simulation of the scene the World object has two methods that must be called from the game loop:  World.update(double) or World.updatev(double).  These methods must be passed the elapsed time from the last iteration **in seconds**.

The World.updatev(double) method can be used if you prefer a variable time step or would like to control when the World updates yourself.  The World.update(double) method will only update given the step frequency set in the Settings object.
  * Note: Variable time steps can reduce accuracy and stability.

## Exceptions ##
There are a number of RuntimeExceptions that are thrown.  Exceptions like these are typically thrown when the creation of an object fails due to invalid arguments.  These exceptions should rarely occur.

For example, adding a null Body to the World object throws a NullPointerException.  The runtime exceptions you may encounter include:
  * NullPointerException - usually caused by passing null instead of a object reference to a constructor or setXXX method.
  * IllegalArgumentException - usually caused by passing an invalid value that has a predefined range or restriction.
  * UnsupportedOperationException - usually caused by attempting to modify an immutable object, Transform.IDENTITY for example.
  * IndexOutOfBoundsException - usually caused by referencing an index of a BodyFixture, Shape, etc that is out of bounds.  Can also because by supplying the wrong sized list or array to a method that expects a certain size.

Most of these exceptions are obvious or documented in the source and [Javadocs](http://docs.dyn4j.org) (using text like "in the range of [0, 1]") or by @throws javadoc documentation.

# Examples #
For examples see the [Example Listing](ExampleListing.md) wiki page for two starter applications ExampleGraphics2D and ExampleJOGL depending on which rendering engine you will use (renders are not limited to these two, the dyn4j project itself is not dependent on any).

In addition, a listing of the !Sandbox tests, what they do, what they show, and their class names is given.  Use these as examples on how to use all functionality.

Also, the Sandbox application can be used to play with various features of the engine.  You can even create scenes and export them to XML or Java source code.

# Extra Help #
Finally if you need extra help you can look at the following resources:
  * <a href='http://code.google.com/p/dyn4j/w/list'>Wiki</a> - for other topics about the library
  * <a href='http://code.google.com/p/dyn4j/source/browse/'>TestBed</a> application - for examples using the library
  * <a href='http://code.google.com/p/dyn4j/source/browse/'>Sandbox</a> application - to play with the features of the engine
  * <a href='http://code.google.com/p/dyn4j/source/browse/'>JUnit tests</a> - for more examples using the library
  * <a href='http://docs.dyn4j.org'>Javadocs</a> - for more documentation about specific methods/classes
  * <a href='http://code.google.com/p/dyn4j/source/browse/trunk/release-notes.txt'>Release Notes</a> - for information on release changes

If all else fails please use the <a href='http://forum.dyn4j.org'>forums</a> or contact me directly via <strong>william dot bittle at dyn4j dot org</strong>.
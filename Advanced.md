After reading through the GettingStarted page and getting your project integrated with dyn4j, its time to hit some of the more advanced topics to achieve better control over the collision detection and physics pipelines.



# General #
## Package Architecture ##
The following diagram describes the package dependencies:

![http://www.dyn4j.org/images/package-diagram-800.png](http://www.dyn4j.org/images/package-diagram-800.png)

  * The geometry packages are dependent upon the root dyn4j package.
  * The collision packages are dependent upon the base geometry package.
  * The dynamics packages are dependent upon the base geometry package and the collision packages.

## Ids ##
Each Shape, Fixture/BodyFixture, and Body are given random UUIDs upon creation.  These ids are used to refer to the objects stored in maps, comparisons, etc.  Currently the ids are read only as to ensure that each is unique.  This may change in a future release.

## User Data ##
Each Shape, Fixture/BodyFixture, and Body (and some other classes, see the [Javadocs](http://docs.dyn4j.org) for details) contain a member variable called userData which is of type Object.

This member can be used to store anything that you want to be associated with the object.

## Settings ##
The Settings class contained in the dynamics package is the class used to configure the physics pipeline.  Each instance of the World class has it's own Settings.

You can use this object to set physics settings at any time.  The Settings object is defaulted to the settings that worked best for all the tests in the TestBed application.

The Settings class is mainly used to configure the performance to accuracy trade off.

## Units ##
The default units are Meter, Kilogram, Seconds or MKS.  It is possible to use another measurement system, however, all the default settings in the Settings object and other classes will need to be adjusted.

If possible, always use MKS, when thats not an option use the UnitConversion class to convert any values to MKS or from MKS.

## Epsilon ##
An inherent side effect of finite precision floating point arithmetic is numerical error.  Another problem to deal with is the lack of exact representation of all values.  These two problems cause many algorithms and logic to use an epsilon comparison instead of comparisons between 0.

The Epsilon class attempts to approximate the machine epsilon for a double precision floating point number.  This epsilon is the smallest floating point number such that x + epsilon &ne; x.  This value is used in a number of algorithms within dyn4j and can be used in game code as well via the Epsilon.E property.
  * dyn4j actually uses larger values than epsilon in some cases to improve performance and stability.

## Rotations ##
Like Java, rotations are specified in Radians.  The Math.toRadians and Math.toDegrees methods can be used to convert to and from the different units.

# Shapes #
Both the Shape and Body classes implement the Transformable interface.  The Shape class's implementation directly modifies the internal state of the shape.  This is mainly used to locally transform the shape when part of a multi-shape body.  The Body class's implementation modifies the body's transform.

  * Shapes can be reused in other Bodies but remember that if you transform a shape it will be transformed for all Bodies you add it to.  Its recommended to avoid this practice.

All Shapes also have a getRadius method.  This method returns the maximum radius of the shape (the rotation disc radius).

## Convex Hulls ##
Another way to create convex shapes is to generate one from a point set.  Shapes created this way are called Convex Hulls and use the Polygon class as storage.

You can find algorithms to generate convex hulls in the geometry.hull package.  The algorithms generate a minimum convex hull given a point set.  All the algorithms should produce the same result, however one may be faster than another depending on the input.

These are intended for both pre-processing and runtime use.

## Convex Decomposition ##
Another way to create convex shapes is to decompose a larger concave shape into smaller convex pieces.

As stated in the GettingStarted page, all shapes used in the library must be convex.  To help with this process some convex decomposition algorithms are provided.  These algorithms operate on simple polygons **without** holes.

Convex decomposition algorithms are in the geometry.decompose package and are used to decompose a simple polygon into a list of convex shapes.  The algorithms provided are not optimal but have acceptable complexity.

Three algorithms are offered, generally producing different results.  If you are using these for offline processing, use each one to obtain the smallest decomposition.

These are intended for both pre-processing and runtime use.

## Segment vs. Segment ##
If you ran the TestBed and had a look at the Terrain test you may noticed that segment vs. segment collision "doesn't work".  This is by design.  Segments are a special case and provided with out segment vs. segment _collision resolution_.

The Segment class lives in the geometry package and extends the Convex interface.  Because of this it can be used in collision detection **and in the physics pipeline**.  This class represents a infinitely thin segment.  Because of this, segment vs. segment _collision resolution_ does not work, however, _collision detection_ does.

## Creating New Convex Shapes ##
If you choose to create your own Shape class you must implement the Convex interface for the new shape to be usable by the collision detection and physics pipelines.  The getAxes and getFoci methods are used to support SAT, the getFarthestPoint is used by GJK, and the getFarthestFeature is used by the Manifold Solver.

# Bodies #
The Body class contains methods to apply linear and angular damping.  This can be useful to simulate fluid/air resistance and also to reduce the total energy in the system.

Use the setGravityScale method to apply less or more gravity to individual bodies (the default is 1.0).

For initial placement of Bodies, use the translateToOrigin method.  This will translate the Body's center of mass to the origin making it easier to initially place Bodies before starting the simulation.

## Integration With Game Objects ##
One common question is "How do I relate my game objects with Bodies in the World?"  There are a few ways to choose from:

The Body class can be extended or can be contained in another object or can be completely separate.
  * The Body class can be extended, be careful when overriding methods; make sure you call the super method.
  * The Body class can be contained in another class
  * The Body class can be completely separate by using the userData member variable for association.

All three methods are acceptable and intended for use.  The Sandbox application uses the extension method.

## Body States ##
The life time of a body is usually very long.  Over that span the body's state may change.

A body becomes "asleep" when the physics engine has determined that it is not moving.  The body is still tested for collisions but is excluded from the physics pipeline to save time.  Sleeping of a body only occurs when the body's linear and angular velocity are below a threshold set in the Settings object.  Automatic sleeping can be controlled on a body by body basis or globally using the Settings class via the setAutoSleepingEnabled methods.

A body becomes "inactive" when the collision detection system detects that the entire body is outside the bounds of the world.  The body no longer participates in collision detection or physics.  This state will never be set automatically if the world has no bounds.

Using the setActive and setAsleep methods you can manually put a body to sleep or set inactive.

## Applying Forces, Torques, & Impulses ##
When applying forces and torques to a body, they are queued in an accumulator and not immediately applied.  When a simulation step is performed, the stored forces and torques are applied to the bodies and placed in the force and torque member variables.  This is done so that the previous simulation step's force and torque values can be queried via the getForce and getTorque methods.

The force and torque classes also feature an isComplete method that can be overridden by sub classes.  This allows a force to be created that may or may not be removed at the end of a world step given the return of the isComplete method.

Applying forces or torques to a body will immediately awaken the body if it was asleep.

Impulses are applied to bodies immediately (not stored in an accumulator).  Applying an impulse will immediately awaken the body as well.

## Fixtures & BodyFixtures ##
Fixture is the base class for BodyFixture.  BodyFixtures are the only Fixtures allowed to be added to a body.  The Fixture base class is present to allow developers to use the collision detection system without the dynamics engine.

There is a one to one relationship between a convex shape and a BodyFixture.  A convex shape must be contained in a BodyFixture to be added to a body.  The BodyFixture class allows shapes to have different dynamics settings.

Each BodyFixture has its own density, friction and restitution coefficients, and collision filter.  BodyFixtures can also be flagged as "sensors."

A sensor is a BodyFixture who will detect collisions but whose collisions will not be resolved.

Filters can be used to only allow certain BodyFixtures to collide with other BodyFixtures.

## Collision Filtering ##
Collision filtering is used to only allow a category of bodies to collide with another category.

Collision filtering is achieved by the Filter interface and the member on the Fixture/BodyFixture class.

There are three Filter implementations provided, the Filter.DEFAULT\_FILTER, the CategoryFilter (just like Box2D's collision filter, int category + mask), and the TypeFilter.

You can also create your own filter classes to perform more advanced collision filtering by implementing the Filter interface.

There are some gotcha's on this topic:
  * Collision filtering can be tricky; the collision filters can be called in either order filter1.isAllowed(filter2) or filter2.isAllowed(filter1).  This can cause some confusion.  Another problem is if the filters compared are not the same filter class type.  The CategoryFilter is conservative and will return true if the class types are not the same.  Because of this, if you choose to make your own filter implementation class, make sure all Fixture/BodyFixture objects are given an instance of that same class.
  * The Filter.DEFAULT\_FILTER class always returns true and is set by default on every Fixture/BodyFixture created.
  * The Filter on a Fixture/BodyFixture cannot be null.  Use the Filter.DEFAULT\_FILTER instead.
  * The TypeFilter class should not be used directly.  Instead, it should be extended by your own classes.  These extending classes are purely marker classes with no implementation.  The TypeFilter class uses the inheritance hierarchy to perform filtering.

See the [Javadocs](http://docs.dyn4j.org) for more detail.

## Shape Editing ##
Throughout the life of a body it may need to change its shape(s).  For instance, two bodies collide and one body who has two shapes should break into two bodies each with one shape.  Situations where the shapes of a body need to change is called Shape Editing.

To edit the shapes of a body simply call the addFixture and removeFixture methods on the body.

After any editing has been performed make sure to update the body's mass via one of the setMass methods to reflect the changes.

It is generally discouraged that shapes themselves be modified after being added to a body.  Doing so may have unexpected or inaccurate results.

# World #
The World class is the manager for the collision detection and physics systems.  All event listeners, bodies, joints, etc. are added to the World object.

In addition some configurable properties of note are:

## Gravity ##
The World class will allow you to set the gravity of the world as a vector.  This means you could create gravity in any direction (or none at all).

## Coefficient Mixer ##
As stated above, each BodyFixture instance can have its own friction and restitution values.  Because of this, these values must be combined so that the contact solver can solve collision.

The CoefficientMixer class can be used to override the standard friction and restitution mixing routines.

# Collision Detection #
The collision detection system has a three tier architecture:

Broadphase -> Narrowphase -> Manifold Solver

The broadphase collision detector is a O(n<sup>2</sup>) detector that determines possible collision pairs.  After obtaining all the possible collision pairs, the pairs are passed to the narrowphase collision detector.  These algorithms should be fast and inaccurate yet conservative.

The narrowphase detector tests each shape of the first body against each shape of the second body.  If any combination of shapes collide, the result is passed to the ManifoldSolver.

The manifold solver will determine the points on the shapes that will be used for the physics pipeline.

## Broadphase ##
Four implementations of the BroadphaseDetector interface are provided:
SapBruteForce, SapIncremental, SapTree, and !DynamicAABBTree.  !DynamicAABBTree is used by default.

## Narrowphase ##
Two implementations of the NarrowphaseDetector interface are provided: SAT and GJK.  The GJK algorithm is used by default.

## Using Collision Detection Only ##
The collision detection system can be used without the physics pipeline.  Create a class that implements the Collidable interface.  This class will serve as a replacement for the Body class.  Then perform the process outlined above in your own implementation of the World class.

You can also use the current Body and World classes and implement the necessary listeners to effectively turn off the dynamics portions of the library.

## Static Collision Detection Queries ##
The collision detector classes also allow for static collision detection queries.  Just pass the respective values to the detect methods.

In addition to this, you can use the faster boolean result methods if you don't plan to use the extra information provided by the standard detect methods.

See the World class's detect methods for static area queries.

## Continuous Collision Detection (CCD) ##
Continuous collision detection is aimed at solving the "tunneling" problem.  Tunneling occurs when a fast moving object starts on one side of another object and after one time step is on the other side of that object.  This can happen when bodies are fast or thin.

The implementation used in dyn4j is called Conservative Advancement.  This method is used in Bullet, Box2d and a number of other physics engines.  The implementation in dyn4j computes the exact time of impact within a specified tolerance.

After detection of a missed collision, the bodies are linearly interpolated to the time of impact.  Next the closest points on the bodies are solved so that the bodies begin to overlap.  This causes the discrete collision detection to catch the collision in the next time step.  Time is not conserved.

This process is performed at the end of each time step.

# Collision Detection & Physics Pipeline #
The collision detection and physics pipeline is structured as shown in the following flowcharts.  Its important to know the order in which operations occur and when to expect up-to-date or old information.

![http://www.dyn4j.org/images/update-diagram-800.png](http://www.dyn4j.org/images/update-diagram-800.png)

And the detect method:

![http://www.dyn4j.org/images/detect-diagram-800.png](http://www.dyn4j.org/images/detect-diagram-800.png)

The listener classes that are discussed in the next section are shown in green.  These are intended to notify the user of specific actions happening in the pipeline and allow the user to respond.

**Excluding the StepListener.begin and StepListener.end methods, all listener methods should avoid modifying/adding/removing bodies, joints, or the world.  Doing so can cause runtime exceptions or cause unexpected behavior (see the individual Listener classes for more details on operations supported).  If you need to respond to a particular event, its recommended to store the necessary information and respond in the StepListener.begin or StepListener.end methods or after the World.update method has returned.**

One last important concept here is the World.setUpdateRequired method.  Examining the flowchart reveals that new contacts are found immediately after the previous contacts have been solved.  This allows the user to have the most up-to-date information about contacts, colliding bodies, etc.  This also enables accurate Raycasting and Broadphase tests.

However, because of this design, if a body is modified in anyway that would change the contacts (like adding/removing a BodyFixture) the World needs to perform collision detection again.  To have the World perform collision detection again, call the World.setUpdateRequired(true) method.  It is up to the user of the World class to perform this step.  Unexpected behavior may occur if this is omitted, but only for that time step.  Upon the next time step everything will be updated (given nothing else has been changed).

The following is a list of actions that require the setUpdateRequired method to be called:
  * If a Body has been added or removed from the World
  * If a Body has been translated or rotated
  * If a Body's state has been manually changed via the Body.setActive(boolean) method
  * If a BodyFixture has been added or removed from a Body
  * If a BodyFixture's sensor flag has been manually changed via the BodyFixture.setSensor(boolean) method
  * If a BodyFixture's filter has been manually changed via the BodyFixture.setFilter(boolean) method
  * If a BodyFixture's restitution or friction coefficient has changed
  * If a BodyFixture's Shape has been translated or rotated
  * If a BodyFixture's Shape has been changed (vertices, radius, etc.)
  * If a Body's type has changed to or from Static (this is caused by the using setMassType(Mass.INFINITE/Mass.NORMAL) method)
  * If a Joint has been added or removed from the World in which the joined bodies should not be allowed to collide.
  * If the World's CoefficientMixer has been changed

## Event Listening ##
The World class contains a number of event listeners used to receive notifications when an event occurs.

**Excluding the StepListener.begin and StepListener.end methods, all listener methods should avoid modifying/adding/removing bodies, joints, or the world.  Doing so can cause runtime exceptions or cause unexpected behavior (see the individual Listener classes for more details on operations supported).  If you need to respond to a particular event, its recommended to store the necessary information and respond in the StepListener.begin or StepListener.end methods or after the World.update method has returned.**

The World class allows multiple instances of each listener type.  This allows developers to better separate their own code for event handling.

  * Many listeners have methods that return booleans that indicate to continue or stop processing or something along those lines.  In the case of multiple listeners, if **any** listener returns a "skip" valued boolean (or the like) the action will be skipped.

### Out of Bounds Event ###
As described above, when a body goes out of bounds it is set to inactive.

To receive a notification of this event extend the BoundsAdapter class or implement the BoundsListener interface and set it in the World object using the setBoundsListener method.

### Implicit Destruction Event ###
When a call to the World.remove(Body) or World.remove(Joint) is made some joints and contacts may be destroyed.

You can receive notification of these events by extending the DestructionAdapter class or by implementing the DestructionListener interface.  Just like the BoundsListener the DestructionListener is set via the world object.

### Step Event ###
When a call to the World.update(double) is made a simulation step is not always taken.  A better simulation results if the steps taken are the same elapsed time.

The StepAdapter class and StepListener interface are provided so that you know when a simulation step is performed.  There are two events to listen for: begin and end.

  * These will be called every time a World.updatev(double) call is made.

As stated above, you may use this event to perform updates on bodies/joints and the World for any queued events that happened during the time step.

### Collision Event ###
When bodies are detected as colliding by the various phases the CollisionListener set on the World object is notified.

There are four events that can be called:
  * Broadphase - Called when two Bodies are found to be in collision by the Broadphase
  * Narrowphase - Called when two Bodies are found to be in collision by the Narrowphase
  * Manifold - Called when a contact manifold is found between two Bodies
  * Final - Called just before the ContactConstraint is added to the solver.  Use this method to modify the tangent speed of the contacts to simulate a conveyor belt effect.

Each of the the events should return a boolean result.  The returned boolean indicates whether the collision processing should continue.

The CollisionAdapter, which is the default, will always return true for all methods.

### Contact Event ###
When Bodies come into contact the physics engine must resolve that contact.  The ContactAdapter class and ContactListener interface are used to receive contact events.

The contact events include:
  * Begin - A contact was created between two bodies
  * Persist - A contact is still active between two bodies
  * End - A contact no longer exists between two bodies
  * Sensed - A contact exists between a sensor body and another body
  * PreSolve- Called for all contacts that will be resolved
  * PostSolve - A contact between two bodies was resolved

Some of the events above should return a boolean result.  The returned boolean indicates whether the processing of that contact should continue.

The ContactAdapter, which is the default, will always return true for all the methods.

### Ray Cast Event ###
When a ray cast is performed using the World.raycast methods two ray cast events may be triggered.

Notification of these events is through the RaycastListener interface and RaycastAdapter class.

There are two events, one for each Body and one for each BodyFixture on each Body.  Each event returns a boolean value indicating that the World should or should not raycast the Body or BodyFixture.  Implement these methods to ignore certain Bodies or BodyFixtures.

### Time Of Impact Events ###
If continuous collision detection (CCD) is disabled its possible that bodies traveling fast may pass through other objects within one time step.  If CCD is enabled, detection of these situations will generate time of impact events.

The single time of impact event can be listened for via the TimeOfImpactListener interface or the TimeOfImpactAdapter class.

Returning a true value indicates that the event should be handled, returning false indicates that the event should be ignored.

Each event has specific information that can be useful for games.  Check the [Javadocs](http://docs.dyn4j.org) for more information.
A simple listing of classes in the /test directory that show the usage of the dyn4j project.

## Example Applications ##
Getting started examples (under 500 lines of code each) contained in the test/org.dyn4j.game2d.examples package.

[Click here to view the examples](http://code.google.com/p/dyn4j/source/browse/#svn%2Ftrunk%2Fexamples%2Forg%2Fdyn4j%2Fexamples)

These can be used as a staring point to integrate dyn4j into your project.
| **Example** | **Class** | **Description** |
|:------------|:----------|:----------------|
| Using Java2D | _ExampleGraphics2D_ | Simple, self contained example application using Java2D and dyn4j to create a simple scene. |
| Using JOGL  | _ExampleJOGL_ | Simple, self contained example application using JOGL and dyn4j to create a simple scene. |

## Sandbox Examples ##
Examples contained in the Sandbox application located in the /sandbox/org/dyn4j/sandbox/tests folder.

[Click here to view the tests](http://code.google.com/p/dyn4j/source/browse/#svn%2Ftrunk%2Fsandbox%2Forg%2Fdyn4j%2Fsandbox%2Ftests).  These tests are best run in the application itself (Use the Export->Java feature of the Sandbox to generate Java code for the .xml tests).

| **Example** | **Class** | **Description** |
|:------------|:----------|:----------------|
| Extending The Body Class | _SandboxBody_ | This class is used to draw the Body's shape from the shapes added to the body using OpenGL.  Using this same techniques, a game object could be created inheriting from Body. |
| Performance | _Bucket, Funnel, Parallel, Pyramid, RaycastPerformance_ | These test are used to stress test the library (lots of bodies and lots of contacts). |
| General     | _Shapes, Seesaw_ | Classes used to setup the scene/creating objects. |
| AngleJoint  | _Angle, AngleRatio_ | This class sets up an angle joint between two bodies.  The limits are set and the mouse can be used to move the lower body to test the limits. The later tests the ratio option. |
| RevoluteJoint | _Bridge, Chain, Motor_ | These tests are mainly used to stress test the RevoluteJoint's position solving.  The Motor class uses a RevoluteJoint to power a truck like object carry some boxes up a ramp. |
| CCD         | _Bullet, FastRotation_ | These tests show what can happen when CCD is disabled. |
| Concave Bodies | _Concave, Dyn4j_ | These tests show the use of composing convex shapes to form concave Bodies. |
| Convex Decomposition | _All_     | A body in the Sandbox can have a decomposed shape added to it.  The Dyn4j Bodies were generated in this test (then slightly modified manually). |
| Convex Hull Generation | _Hull_    | This test can be used to draw point sets and generate a convex hull from it.  See the controls for details. |
| Collision Filtering | _Filter_  | This class tests the CategoryFilter.  Take a look at the Javadoc for CategoryFilter before using. |
| Contact Listening | _ContactCounter, Destructible.Destructor_ | These show how to use the ContactListener to respond when contact events occur.  The first class simple counts the number of each type of contact and the second performs some logic when a contact has begun. |
| Step Listening | _ContactCounter_ | This class must clear the counts of each contact type each time step, therefore must be notified when a time step occurs. |
| Body Sensors | _Dyn4j, Sensor_ | These test show how sensors can be used and their effect. |
| Joint Collision | _JointCollision_ | This test shows the difference between enabling and disabling the allowedCollision flag on Joints.  The first pair of Bodies is not allowed, the second is allowed. |
| WheelJoint  | _Wheel_   | Shows the creation of a WheelJoint in a car configuration. |
| DistanceJoint | _JointCollision, NewtonsCradle, SpringDamper, Wagon_ | These classes test the DistanceJoint in a number of configurations.  The SpringDamper class shows the use of the spring/damper capabilities. |
| Multithreading | _Parallel_ | NOTE: you must enable multithreading in the TestBed Control Panel since its disabled by default.  This test shows a typical case where multithreading could yield large performance boosts. |
| PrismaticJoint | _Prismatic_ | This class shows how to create a PrismaticJoint and set/enable limits. |
| PulleyJoint | _Pulley_  | This class shows how to setup a PulleyJoint and the use of the ratio to simulate a block and tackle. |
| Stacking    | _Pyramid, Stack, Parallel, Sleep_ | These tests all simulate stacking of boxes. |
| Raycasting  | _Raycast, RaycastPerformance_ | This test, along with the controls for this test, show how to use and the effects of the raycast methods in the World class.  In addition, this class shows the use of a custom RaycastListener. |
| Restitution Values | _Restitution_ | This test shows the bouncing caused by different settings of the restitution value on BodyFixtures. |
| RopeJoint   | _Rope_    | Shows the use of the RopeJoint using limits. |
| Auto-Sleeping | _Sleep, All_ | Shows the effect of automatic sleeping. |
| Segment Shapes | _Shapes, Terrain, Raycast_ | Shows the use of Segments in collision detection, resolution, and raycasting. |
| WeldJoint   | _Weld, Destructible, DivingBoard_ | Shows the use of the WeldJoint to bind two Bodies together |
| MouseJoint  | _Mouse, All_ | Shows the use of the MouseJoint attached to a mouse and fixed at a point. |
| Tanget Speed | _Conveyor_ | Shows the use of the tangent speed setting on contacts to simulate a conveyor belt effect. |
| MotorJoint  | _Animate_ | Shows an example use of the MotorJoint class. |
| Coordinate Shifting | _CoordinateShift, All_ | Shows the use of coordinate shifting for large worlds. |
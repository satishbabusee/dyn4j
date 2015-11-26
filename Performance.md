# Introduction #
This page contains a simple listing of methods to obtain the best performance out of the library.

Generally the default settings should give a good balance between speed and accuracy for most applications.



# Continuous Collision Detection (CCD) #
Continuous collision detection is a very computationally expensive process and should only be used if needed.
  * Disable CCD completely using the Settings class and ContinuousDetection.NONE
  * Enable CCD for bullets only.  You must flag which Bodies will need to be detected for tunneling by using the Body.setBullet(boolean) method.
  * Configure the current ConservativeAdvancement algorithm via the setMaxIterations(int) method.  The minimum is 5 with the default being 30.
  * Configure the current ConservativeAdvancement algorithm via the setDistanceEpsilon(double) method. The minimum is 0.0 and the default is 1E-6.  The closer the value is to zero determines how accurate and how slow the algorithm will be.
  * Keep the number of bullet Bodies to a minimum.

# Auto Sleeping #
Auto sleeping is enabled by default, leave this setting on if at all possible.

In addition to leaving this setting enabled:
  * Increase the sleep velocity and sleep angular velocity values to put Bodies to sleep faster via the Settings.setSleepVelocity(double) and Settings.setSleepAngularVelocity(double) methods.
  * Decrease the time required for Bodies to be stationary before being put to sleep using the Settings.setSleepTime(double) method.

# World Updates #
Using the World.update(double) method forces that the World only update at a given frequency (in seconds) defined in the Settings class.  The value can be anything greater than zero, the default is 1/60 (60 times per second).  You can modify this value to have the engine update less often, leaving more time for your application to perform other tasks.

Use the World.setUpdateRequired method sparingly since this method will trigger another collision detection with all bodies in the world upon the next update.

See the section in [Advanced](Advanced.md) for more detail about the collision detection and physics pipeline.

# Collision Detection #
Implement the CollisionListener and ContactListener interfaces to catch collisions/contacts that you don't want/need processed.  This will save time in the pipline.

If your World is totally enclosed or its unlikely that Bodies will leave the Bounds, set the Bounds on the World object to null.  This will disable Bounds checking.

## Broadphase ##
The !Sandbox application shows little performance difference between the various broadphase detectors.  The !DynamicAABBTree class is the default and by far has the best Raycast performance, stick with this one if your application is raycast heavy.

The SapTree class is next in line with better raycast performance than the other Sap implementations but not as good as the !DynamicAABBTree class.  However, SapIncremental class does have better collision detection performance than all the others.  Using this broadphase may provide better performance if your application is light on raycasts.

If you don't use raycasts at all either the SapIncremental or SapTree is probably the best choice.

The SapBruteForce class is an implementation of the old Sap broadphase using the new BroadphaseDetector interface and will have similar performance to the old one.

All the BroadphaseDetectors use a AABBs to test for collisions.  They also use an AABB expansion value to create larger AABBs than the close fitting ones.  This expansion value is configurable and larger values allow for less updates to the broadphase when a Body moves or rotates.  However, the larger the AABB becomes, the more collision pairs are generated.  Use this to customize the trade-off.

## Narrowphase ##
In general, for the !Sandbox, the SAT algorithm performs faster than GJK.

If you are using GJK you can configure a number of properties to improve performance while sacrificing accuracy:
  * Decrease the maximum number of iterations that GJK will perform.  This is only really a problem when Circles are being used.  The default is 100 and the minimum is 5.  In practice I have rarely seen it hit double digits.
  * Increase the distance epsilon.  This controls how GJK knows when to stop iterating.  Again this is really only a problem if Circles are being used.  The default value is 1E-8.  Increasing this value will decrease the accuracy of the algorithm.

These GJK tips may help you if you are using CCD or raycasting as well since GJK is used by those processes.

In addition, GJK uses EPA to obtain a penetration vector and depth.  EPA uses a similar concept as GJK and has the same configuration values.  Use the same tips above to configure EPA as well.

# Collision Resolution #
The collision resolution system has a number of properties to configure to get the right balance of speed and accuracy for your application.
  * Decrease the number of velocity or position solving iterations using the Settings.setPositionConstraintSolverIterations(int) and Settings.setVelocityConstraintSolverIterations(int).  The minimum value is 1 with a default of 10 for both settings.
  * Implement a new CoefficientMixer than the default.  The default performs a Math.sqrt() in the friction mixing method.  Instead, for example, you could use an averaging method.

# Raycasting #
Raycasting, like CCD, can be very computationally expensive.  In general try to raycast against specific Bodies instead of the entire World.

In addition to this use the all, maxLength, and ignoreSensors parameters to decrease the number of Body/Shapes that must be checked.

Implement the RaycastListener to ignore Bodies and BodyFixtures that don't need to be tested.

# Bodies #
Avoid over use of the Body.getJoinedBodies(), Body.getJoints(), Body.getInContactBodies(), and Body.getContacts() methods since all of these methods create new lists for every invocation to return the information.

Avoid excessive calls to the Body.setMass methods as these are expensive.  If you need to toggle the Mass type between a special type and the normal type use the Body.setMassType(type) method instead since this method does not recompute the Mass.

Increase the linear and angular damping of your Bodies to increase the rate at which they are automatically put to sleep.

Apply filters to Bodies to avoid unnecessary collision detection/resolution steps.

If you only want to know when a collision happens, set the BodyFixture to be a sensor.
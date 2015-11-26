

# Typical Joints #

## Distance Joint ##
The DistanceJoint class represents a fixed distance between two bodies.  The bodies are allowed to rotate freely about the anchor points.  The DistanceJoint class can also act like a spring-damper with specified fixed distance is a target distance (or rest distance).

This joint is defined by supplying the anchor points, a<sub>1</sub> and a<sub>2</sub>, in world coordinates.

![http://www.dyn4j.org/images/distance-joint.png](http://www.dyn4j.org/images/distance-joint.png)

## Revolute Joint ##
The RevoluteJoint class represents a joint that only allows rotation between the joined bodies.  This class also supports the use of a motor and limits.  The motor can be used to apply motion between the bodies and the limits can be used to limit the angle between the two bodies.
  * The angular limits are relative to the initial angle between the two bodies.

This joint requires one world space anchor point, a, which is used as the pivot point.

![http://www.dyn4j.org/images/revolute-joint.png](http://www.dyn4j.org/images/revolute-joint.png)

## Weld Joint ##
The WeldJoint class represents a joint that "welds" two bodies together.  This can be used to fix two bodies together as one, then removed to simulate destructible bodies.  When bodies are joined by a WeldJoint their relative rotation and translation is constant (determined by the initial configuration).

Like the DistanceJoint class you can add a spring-damper.  However, this acts as a rotational spring damper instead of a linear spring damper.

This joint requires one world space anchor point, a, which is used as the weld point.

![http://www.dyn4j.org/images/weld-joint.png](http://www.dyn4j.org/images/weld-joint.png)

## Prismatic Joint ##
The PrismaticJoint class represents a joint that allows bodies to translate freely along an axis.  Like the RevoluteJoint, this joint allows for motors and limits.  The motor will drive the linear motion along the axis toward the given speed and the limits will limit the linear motion along the axis.

This joint requires one world space anchor point, a, which is used as the pivot point to prevent rotation and a world space axis used as the allowed translation axis.

![http://www.dyn4j.org/images/prismatic-joint.png](http://www.dyn4j.org/images/prismatic-joint.png)

## Mouse Joint ##
The MouseJoint class is a joint that was specifically designed to manipulate objects in a scene using the mouse (but can be used with out the mouse).  The MouseJoint creates a spring-damper distance joint at a specified world space point on the body and drives the distance to zero.  As the mouse (or something else) moves the body, its dragged along as if it was connected by a rubber band.

This joint requires the anchor point, a, in world coordinates and the spring-damper values.  Notice that this joint only accepts one body.

The setTarget(Vector2) method should be used to update the target point, t, to drag the body to that position.

![http://www.dyn4j.org/images/mouse-joint.png](http://www.dyn4j.org/images/mouse-joint.png)

## Pulley Joint ##
The PulleyJoint class represents a joint connecting two bodies by a fixed length "rope."  The PulleyJoint only limits translation along the "rope" axes and can limit the length of the "rope."  Setting the ratio to something other than 1.0 causes the pulley to emulate a block-and-tackle.

This joint requires a world space fixed point, pa<sub>1</sub> and pa<sub>2</sub>, and a world space body point, a<sub>1</sub> and a<sub>2</sub> for each body.

The lengths of the "ropes" are computed:

`||`pa<sub>1</sub> - a<sub>1</sub>`||` = ratio `*` `||`pa<sub>2</sub> - a<sub>2</sub>`||`

![http://www.dyn4j.org/images/pulley-joint.png](http://www.dyn4j.org/images/pulley-joint.png)

## Rope Joint ##
The RopeJoint class represents a joint connecting two bodies by a "rope."  The RopeJoint is best described as a DistanceJoint with a max length or min length.  The bodies can move closer and further away and rotate freely, but are forced to be within some maximum or minimum distance of one another.

This joint requires a world space anchor point for each body and a maximum or minimum distance at which they can separate.  Initially when created, this joint acts like a DistanceJoint where both the maximum and minimum are the same length (initially computed as the distance between the two anchor points).

![http://www.dyn4j.org/images/rope-joint.png](http://www.dyn4j.org/images/rope-joint.png)

## Wheel Joint ##
The WheelJoint class represents a joint connecting two bodies where the bodies are allowed to rotate about the given anchor point and translate about the given axis.  The translation portion of the allowed motion has an optional spring damper.  This is an ideal joint for vehicles.  The motor for this joint is a rotational motor.

This joint requires a world space anchor point, a (the pivot point), and a world space (normalized) axis for the allowed translation.

This joint can be thought of as a combination of the revolute, prismatic and distance joints.

![http://www.dyn4j.org/images/wheel-joint.png](http://www.dyn4j.org/images/wheel-joint.png)

# Other Joints #

## Angle Joint ##
The AngleJoint class represents a joint connecting two bodies limiting their angles.  This is similar to the RevoluteJoint when limits are enforced.  When the angle limits are equal this joint will force both body's angles to be identical.  Regardless of the limits, the bodies are allowed to translate freely.
  * The angular limits are relative to the initial angle between the two bodies.

Setting the ratio field of the AngleJoint allows the bodies to rotate at the given ratio of one another.  This creates a gear-like effect.  If limits are used in conjunction with the ratio, the ratio will be ignored when the joint reaches a limit.

This joint only requires that the two joined bodies be given, initially defaulting to equal limits.

## Friction Joint ##
The FrictionJoint class represents a joint that attempts to drive the relative motion between the bodies to zero.  The maximum force and torque members are used to limit the rate at which the motion is driven to zero.

This joint only requires one world space anchor point which is used as the pivot point for adding friction to the relative angular motion.

## Motor Joint ##
The MotorJoint class represents a joint that uses motors to move a body.  The application sets a target position and rotation (relative to the other body) and the joint attempts to reach that state.  The maximum motor torque and force can be limited.  This joint is better for character movement since it still allows the bodies to interact naturally with the world.

This joint only requires two bodies (the initial linear target is the first body's center of mass).  Typically one of the bodies is static.  The linear and angular targets are relative to the first body's center of mass and angle.

## Line Joint (Deprecated) ##
The LineJoint class represents a joint that allows bodies to translate freely along an axis and rotate freely about a pivot point.  This joint is identical to the PrismaticJoint except allows rotation about the pivot point.  Like the RevoluteJoint and PrismaticJoints, this joint allows for motors and limits.  The motor will drive the linear motion along the axis toward the given speed and the limits will limit the linear motion along the axis.

This joint requires one world space anchor point which is used as the pivot point and a world space axis used as the allowed translation axis.

**This joint has been deprecated and replaced with the WheelJoint class.**
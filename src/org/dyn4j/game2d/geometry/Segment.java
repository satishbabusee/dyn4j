/*
 * Copyright (c) 2010, William Bittle
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted 
 * provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice, this list of conditions 
 *     and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions 
 *     and the following disclaimer in the documentation and/or other materials provided with the 
 *     distribution.
 *   * Neither the name of dyn4j nor the names of its contributors may be used to endorse or 
 *     promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.dyn4j.game2d.geometry;

/**
 * Represents a line {@link Segment}.
 * @author William Bittle
 */
public class Segment extends Wound implements Convex, Shape, Transformable {
	/** The segment {@link Shape.Type} */
	public static final Shape.Type TYPE = new Shape.Type();
	
	/** The segment length */
	protected double length;
	
	/**
	 * Full constructor.
	 * @param point1 the first point
	 * @param point2 the second point
	 */
	public Segment(Vector point1, Vector point2) {
		super();
		// make sure the two points are not coincident
		if (point1.equals(point2)) {
			throw new IllegalArgumentException("A line segment must have two different vertices.");
		}
		this.vertices = new Vector[2];
		this.vertices[0] = point1;
		this.vertices[1] = point2;
		this.center = Geometry.getAverageCenter(this.vertices);
		this.length = point1.distance(point2);
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.geometry.Shape#getType()
	 */
	@Override
	public Type getType() {
		return Segment.TYPE;
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.geometry.Wound#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SEGMENT[").append(super.toString()).append("]");
		return sb.toString();
	}
	
	/**
	 * Returns point1 in local coordinates.
	 * @return {@link Vector}
	 */
	public Vector getPoint1() {
		return this.vertices[0];
	}
	
	/**
	 * Returns point2 in local coordinates.
	 * @return {@link Vector}
	 */
	public Vector getPoint2() {
		return this.vertices[1];
	}
	
	/**
	 * Returns the length of the line {@link Segment}.
	 * @return double
	 */
	public double getLength() {
		return this.length;
	}
	
	/**
	 * Determines where the point is relative to the given line.
	 * <pre>
	 * Set L = linePoint2 - linePoint1
	 * Set P = point - linePoint1
	 * location = L.cross(P)
	 * </pre>
	 * Returns 0 if the point lies on the line created from the line segment.<br />
	 * Assuming a right handed coordinate system:<br />
	 * Returns < 0 if the point lies on the right side of the line<br />
	 * Returns > 0 if the point lies on the left side of the line
	 * <p>
	 * Assumes all points are in world space.
	 * @param point the point
	 * @param linePoint1 the first point of the line
	 * @param linePoint2 the second point of the line
	 * @return double
	 */
	public static double getLocation(Vector point, Vector linePoint1, Vector linePoint2) {
		return (linePoint2.x - linePoint1.x) * (point.y - linePoint1.y) -
			  (point.x - linePoint1.x) * (linePoint2.y - linePoint1.y);
	}

	/**
	 * Returns the point on the given line closest to the given point.
	 * <p>
	 * Project the point onto the line:
	 * <pre>
	 * V<sub>line</sub> = P<sub>1</sub> - P<sub>0</sub>
	 * V<sub>point</sub> = P<sub>0</sub> - P
	 * P<sub>closest</sub> = V<sub>point</sub>.project(V<sub>line</sub>)
	 * </pre>
	 * Assumes all points are in world space.
	 * @see Vector#project(Vector)
	 * @param point the point
	 * @param linePoint1 the first point of the line
	 * @param linePoint2 the second point of the line
	 * @return {@link Vector}
	 */
	public static Vector getPointOnLineClosestToPoint(Vector point, Vector linePoint1, Vector linePoint2) {
		// create a vector from the point to the first line point
		Vector p1ToP = point.difference(linePoint1);
		// create a vector representing the line
	    Vector line = linePoint2.difference(linePoint1);
	    // get the length squared of the line
	    double ab2 = line.dot(line);
	    // check ab2 for zero (linePoint1 == linePoint2)
	    if (ab2 == 0.0) return linePoint1.copy();
	    // get the projection of AP on AB
	    double ap_ab = p1ToP.dot(line);
	    // get the position from the first line point to the projection
	    double t = ap_ab / ab2;
	    // create the point on the line
	    return line.multiply(t).add(linePoint1);
	}
	
	/**
	 * Returns the point on this line segment closest to the given point.
	 * <p>
	 * If the point closest to the given point on the line created by this
	 * line segment is not on the line segment then either of the segments
	 * end points will be returned.
	 * <p>
	 * Assumes all points are in world space.
	 * @see Segment#getPointOnLineClosestToPoint(Vector, Vector, Vector)
	 * @param point the point
	 * @param linePoint1 the first point of the line
	 * @param linePoint2 the second point of the line
	 * @return {@link Vector}
	 */
	public static Vector getPointOnSegmentClosestToPoint(Vector point, Vector linePoint1, Vector linePoint2) {
		// create a vector from the point to the first line point
		Vector p1ToP = point.difference(linePoint1);
		// create a vector representing the line
	    Vector line = linePoint2.difference(linePoint1);
	    // get the length squared of the line
	    double ab2 = line.dot(line);
	    // get the projection of AP on AB
	    double ap_ab = p1ToP.dot(line);
	    // check ab2 for zero (linePoint1 == linePoint2)
	    if (ab2 == 0.0) return linePoint1.copy();
	    // get the position from the first line point to the projection
	    double t = ap_ab / ab2;
	    // make sure t is in between 0.0 and 1.0
	    t = Interval.clamp(t, 0.0, 1.0);
	    // create the point on the line
	    return line.multiply(t).add(linePoint1);
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.geometry.Convex#getAxes(java.util.List, org.dyn4j.game2d.geometry.Transform)
	 */
	@Override
	public Vector[] getAxes(Vector[] foci, Transform transform) {
		// get the number of foci
		int size = foci != null ? foci.length : 0;
		// create an array to hold the axes
		Vector[] axes = new Vector[2 + size];
		int n = 0;
		// get the vertices
		Vector p1 = transform.getTransformed(this.vertices[0]);
		Vector p2 = transform.getTransformed(this.vertices[1]);
		// get the edge that makes this segment
		Vector line = p1.to(p2);
		// use both the edge and its normal
		axes[n++] = line.getLeftHandOrthogonalVector();
		axes[n++] = line;
		// add the voronoi region axes if point is supplied
		for (int i = 0; i < size; i++) {
			// get the focal point
			Vector f = foci[i];
			// find the closest point
			if (p1.distanceSquared(f) < p2.distanceSquared(f)) {
				axes[n++] = p1.to(f);
			} else {
				axes[n++] = p2.to(f);
			}
		}
		// return all the axes
		return axes;
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.geometry.Convex#getFoci(org.dyn4j.game2d.geometry.Transform)
	 */
	@Override
	public Vector[] getFoci(Transform transform) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.geometry.Shape#contains(org.dyn4j.game2d.geometry.Vector, org.dyn4j.game2d.geometry.Transform)
	 */
	@Override
	public boolean contains(Vector point, Transform transform) {
		// put the point in local coordinates
		Vector p = transform.getInverseTransformed(point);
		// create a reference to the end points
		Vector p1 = this.vertices[0];
		Vector p2 = this.vertices[1];
		// get the location of the given point relative to this segment
		double value = Segment.getLocation(p, p1, p2);
		// see if the point is on the line created by this line segment
		if (value == 0) {
			double distSqrd = p1.distanceSquared(p2);
			if (p.distanceSquared(p1) <= distSqrd
			 && p.distanceSquared(p2) <= distSqrd) {
				// if the distance to the point from both points is less than or equal
				// to the length of the segment squared then we know its on the line segment
				return true;
			}
			// if the point is further away from either point than the length of the
			// segment then its not on the segment
			return false;
		}
		return false;
	}
	
	/**
	 * Returns true if the given point is inside this {@link Shape}.
	 * <p>
	 * If the given point lies on an edge the point is considered
	 * to be inside the {@link Shape}.
	 * <p>
	 * The given point is assumed to be in world space.
	 * <p>
	 * If the radius is greater than zero then the point is tested to be
	 * within the shape expanded radially by the radius.
	 * @param point world space point
	 * @param transform {@link Transform} for this {@link Shape}
	 * @param radius the expansion radius; in the range [0, &infin;]
	 * @return boolean
	 */
	public boolean contains(Vector point, Transform transform, double radius) {
		// if the radius is zero or less then perform the normal procedure
		if (radius <= 0) {
			return contains(point, transform);
		} else {
			// put the point in local coordinates
			Vector p = transform.getInverseTransformed(point);
			// otherwise act like the segment is two circles and a rectangle
			if (this.vertices[0].distanceSquared(p) <= radius * radius) {
				return true;
			} else if (this.vertices[1].distanceSquared(p) <= radius * radius) {
				return true;
			} else {
				// see if the point is in the rectangle portion
				Vector l = this.vertices[0].to(this.vertices[1]);
				Vector p1 = this.vertices[0].to(p);
				Vector p2 = this.vertices[1].to(p);
				if (l.dot(p1) > 0 && -l.dot(p2) > 0) {
					double dist = p1.project(l.getRightHandOrthogonalVector()).getMagnitudeSquared();
					if (dist <= radius * radius) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.geometry.Shape#project(org.dyn4j.game2d.geometry.Vector, org.dyn4j.game2d.geometry.Transform)
	 */
	@Override
	public Interval project(Vector n, Transform transform) {
		double v = 0.0;
		// get the vertices
		Vector p1 = transform.getTransformed(this.vertices[0]);
		Vector p2 = transform.getTransformed(this.vertices[1]);
		// project the first
    	double min = n.dot(p1);
    	double max = min;
    	// project the second
        v = n.dot(p2);
        if (v < min) { 
            min = v;
        } else if (v > max) { 
            max = v;
        }

        return new Interval(min, max);
	}

	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.geometry.Convex#getFurthestPoint(org.dyn4j.game2d.geometry.Vector, org.dyn4j.game2d.geometry.Transform)
	 */
	@Override
	public Vector getFarthestPoint(Vector n, Transform transform) {
		// get the vertices and the center
		Vector p1 = transform.getTransformed(this.vertices[0]);
		Vector p2 = transform.getTransformed(this.vertices[1]);
		Vector c = transform.getTransformed(this.center);
		// create vectors from the center to each vertex
		Vector v1 = c.to(p1);
		Vector v2 = c.to(p2);
		// project them onto the vector
		double dot1 = n.dot(v1);
		double dot2 = n.dot(v2);
		// find the greatest projection
		if (dot1 >= dot2) {
			return p1;
		} else {
			return p2;
		}
	}
	
	/**
	 * Returns the feature farthest in the direction of n.
	 * <p>
	 * For a {@link Segment} it's always the {@link Segment} itself.
	 * @param n the direction
	 * @param transform the local to world space {@link Transform} of this {@link Convex} {@link Shape}
	 * @return {@link Feature}
	 */
	@Override
	public Feature.Edge getFarthestFeature(Vector n, Transform transform) {
		// the furthest feature for a line is always the line itself
		Vector max = null;
		// get the vertices and the center
		Vector p1 = transform.getTransformed(this.vertices[0]);
		Vector p2 = transform.getTransformed(this.vertices[1]);
		Vector c = transform.getTransformed(this.center);
		// create vectors from the center to each vertex
		Vector v1 = c.to(p1);
		Vector v2 = c.to(p2);
		// project them onto the vector
		double dot1 = n.dot(v1);
		double dot2 = n.dot(v2);
		// find the greatest projection
		if (dot1 >= dot2) {
			max = p1;
		} else {
			max = p2;
		}
		// return the points of the segment in the
		// opposite direction as the other shape
		if (p1.to(p2).right().dot(n) > 0) {
			return new Feature.Edge(new Vector[] {p2, p1}, max);
		} else {
			return new Feature.Edge(new Vector[] {p1, p2}, max);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.geometry.AbstractShape#rotate(double, double, double)
	 */
	@Override
	public void rotate(double theta, double x, double y) {
		super.rotate(theta, x, y);
		this.vertices[0].rotate(theta, x, y);
		this.vertices[1].rotate(theta, x, y);
	}

	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.geometry.AbstractShape#translate(double, double)
	 */
	@Override
	public void translate(double x, double y) {
		super.translate(x, y);
		this.vertices[0].add(x, y);
		this.vertices[1].add(x, y);
	}
}

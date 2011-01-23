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
package org.dyn4j.game2d.testbed.test;

import java.util.Random;

import org.dyn4j.game2d.collision.Bounds;
import org.dyn4j.game2d.collision.RectangularBounds;
import org.dyn4j.game2d.dynamics.Fixture;
import org.dyn4j.game2d.dynamics.World;
import org.dyn4j.game2d.geometry.Circle;
import org.dyn4j.game2d.geometry.Mass;
import org.dyn4j.game2d.geometry.Rectangle;
import org.dyn4j.game2d.testbed.ContactCounter;
import org.dyn4j.game2d.testbed.Entity;
import org.dyn4j.game2d.testbed.Test;

/**
 * Tests lots of random shapes (and sizes) moving through a funnel.
 * @author William Bittle
 */
public class Funnel extends Test {
	/** The number of bodies */
	private static final int SIZE = 200;
	
	/* (non-Javadoc)
	 * @see test.Test#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Tests dropping " + SIZE + " bodies into a funnel.  "
		+ "Used to stress test the dynamics engine.  "
		+ SIZE + " bodies are created and dropped from random locations "
		+ "into a funnel where they eject onto a flat surface.  This test uses both circles "
		+ "and boxes.";
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.Test#initialize()
	 */
	@Override
	public void initialize() {
		// call the super method
		super.initialize();
		
		// set the camera position and zoom
		this.home();
		
		// set the bounds
		this.bounds = new Rectangle(16.0, 18.0);
		this.bounds.translate(0.0, 6.0);
		
		// create the world
		Bounds bounds = new RectangularBounds(this.bounds);
		this.world = new World(bounds);
		
		// setup the contact counter
		ContactCounter cc = new ContactCounter();
		this.world.setContactListener(cc);
		this.world.setStepListener(cc);

		// setup the bodies in the world
		this.setup();
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.Test#setup()
	 */
	@Override
	protected void setup() {
		// create the floor
		Rectangle floorShape = new Rectangle(15.0, 1.0);
		Entity floor = new Entity();
		floor.addFixture(new Fixture(floorShape));
		floor.setMassFromShapes(Mass.Type.INFINITE);
		this.world.add(floor);
		
		// create the funnel sides
		Rectangle leftShape = new Rectangle(1.0, 7.0);
		Entity left = new Entity();
		left.addFixture(new Fixture(leftShape));
		left.setMassFromShapes(Mass.Type.INFINITE);
		left.translate(-8.0, 8.0);
		left.rotateAboutCenter(Math.toRadians(30.0));
		this.world.add(left);
		
		Rectangle rightShape = new Rectangle(1.0, 7.0);
		Entity right = new Entity();
		right.addFixture(new Fixture(rightShape));
		right.setMassFromShapes(Mass.Type.INFINITE);
		right.translate(8.0, 8.0);
		right.rotateAboutCenter(-Math.toRadians(30.0));
		this.world.add(right);
		
		// temp variables
		Random random = new Random();
		
		// create a ton of shapes
		for (int i = 0; i < SIZE; i++) {
			// randomize the size
			double s = random.nextDouble() + 0.3;
			// randomize the position (alternate sign instead of randomize it)
			double x = (i % 2 == 0 ? -1.0 : 1.0) * random.nextDouble() * 7.0;
			double y = random.nextDouble() * 10.0 + 4.0;
			// randomize the shape type
			int t = random.nextInt(2);
			
			// create the body
			Entity e = new Entity();
			if (t == 0) {
				Rectangle r = new Rectangle(s, s);
				Fixture f = new Fixture(r);
				f.setDensity(0.1);
				e.addFixture(f);
			} else {
				Circle c = new Circle(s * 0.5);
				Fixture f = new Fixture(c);
				f.setDensity(0.1);
				e.addFixture(f);
			}
			e.setMassFromShapes();
			e.translate(x, y);
			this.world.add(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.game2d.Test#home()
	 */
	@Override
	public void home() {
		// set the scale
		this.scale = 32.0;
		// set the offset
		this.offset.set(0.0, -5.0);
	}
}
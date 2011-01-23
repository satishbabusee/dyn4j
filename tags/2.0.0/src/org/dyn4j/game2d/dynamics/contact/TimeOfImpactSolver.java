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
package org.dyn4j.game2d.dynamics.contact;

import org.dyn4j.game2d.collision.continuous.TimeOfImpact;
import org.dyn4j.game2d.collision.narrowphase.Separation;
import org.dyn4j.game2d.dynamics.Body;
import org.dyn4j.game2d.dynamics.Settings;
import org.dyn4j.game2d.geometry.Interval;
import org.dyn4j.game2d.geometry.Mass;
import org.dyn4j.game2d.geometry.Vector2;

/**
 * Represents a position solver for a pair of {@link Body}s who came in
 * contact during a time step but where not detected by the discrete
 * collision detectors.
 * <p>
 * This class will translate and rotate the {@link Body}s into a collision.
 * @author William Bittle
 * @version 2.0.0
 * @since 2.0.0
 */
public class TimeOfImpactSolver {
	/**
	 * Moves the given {@link Body}s into collision given the {@link TimeOfImpact}
	 * information.
	 * @param b1 the first {@link Body}
	 * @param b2 the second {@link Body}
	 * @param toi the {@link TimeOfImpact}
	 */
	public void solve(Body b1, Body b2, TimeOfImpact toi) {
		Settings settings = Settings.getInstance();
		double linearTolerance = settings.getLinearTolerance();
		double maxLinearCorrection = settings.getMaxLinearCorrection();
		
		Vector2 c1 = b1.getWorldCenter();
		Vector2 c2 = b2.getWorldCenter();
		
		Mass m1 = b1.getMass();
		Mass m2 = b2.getMass();
		
		double mass1 = m1.getMass();
		double mass2 = m2.getMass();
		
		double invMass1 = mass1 * m1.getInverseMass();
		double invI1 = mass1 * m1.getInverseInertia();
		double invMass2 = mass2 * m2.getInverseMass();
		double invI2 = mass2 * m2.getInverseInertia();
		
		Separation separation = toi.getSeparation();
		
		// solve the constraint
		Vector2 p1w = separation.getPoint1();
		Vector2 p2w = separation.getPoint2();
		
		Vector2 r1 = c1.to(p1w);
		Vector2 r2 = c2.to(p2w);
		
		Vector2 n = separation.getNormal();
		double d = separation.getDistance();
		
		double C = Interval.clamp(d - linearTolerance, -maxLinearCorrection, 0.0);
		
		double rn1 = r1.cross(n);
		double rn2 = r2.cross(n);
		
		double K = invMass1 + invMass2 + invI1 * rn1 * rn1 + invI2 * rn2 * rn2;
		
		double impulse = 0.0;
		if (K > 0.0) {
			impulse = -C / K;
		}
		
		Vector2 J = n.product(impulse);

		// translate and rotate the objects
		b1.translate(J.product(invMass1));
		b1.rotate(invI1 * r1.cross(J), c1.x, c1.y);
		
		b2.translate(J.product(-invMass2));
		b2.rotate(-invI2 * r2.cross(J), c2.x, c2.y);
	}
}
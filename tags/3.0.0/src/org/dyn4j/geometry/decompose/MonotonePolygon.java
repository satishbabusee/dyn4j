/*
 * Copyright (c) 2011 William Bittle  http://www.dyn4j.org/
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
package org.dyn4j.geometry.decompose;

import java.util.List;

/**
 * Represents a monotone polygon.
 * <p>
 * A monotone polygon can be triangulated in O(n) time.  Algorithms within this package may decompose
 * a polygon into monotone pieces, which are then used to decompose into triangles.
 * @author William Bittle
 * @version 2.2.0
 * @since 2.2.0
 * @param <E> the vertex data type
 */
public class MonotonePolygon<E> {
	/**
	 * Enumeration of the types of monotone polygons supported.
	 * <p>
	 * The type of monotone polygon is the axis in which the polygon
	 * is monotone with respect to.
	 * @author William Bittle
	 * @version 2.2.0
	 * @since 2.2.0
	 */
	public enum Type {
		/** Represents a y-monotone polygon */
		Y,
		/** Represents a x-monotone polygon */
		X
	}
	
	/** The type of monotone polygon */
	protected MonotonePolygon.Type type;
	
	/** The sorted array of vertices */
	protected List<MonotoneVertex<E>> vertices;
	
	/**
	 * Full constructor.
	 * @param type the monotone polygon type
	 * @param vertices the sorted array of vertices; descending order
	 */
	public MonotonePolygon(MonotonePolygon.Type type, List<MonotoneVertex<E>> vertices) {
		this.type = type;
		this.vertices = vertices;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MONOTONE_POLYGON[")
		.append(this.type);
		
		int size = this.vertices.size();
		for (int i = 0; i < size; i++) {
			sb.append("|").append(this.vertices.get(i));
		}
		
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Returns the maximum vertex in the sorted array.
	 * @return {@link MonotoneVertex}
	 */
	public MonotoneVertex<E> getMaximum() {
		return this.vertices.get(0);
	}
	
	/**
	 * Returns the minimum vertex in the sorted array.
	 * @return {@link MonotoneVertex}
	 */
	public MonotoneVertex<E> getMinimum() {
		return this.vertices.get(this.vertices.size() - 1);
	}
	
	/**
	 * Returns the type of monotone polygon.
	 * @return {@link MonotonePolygon.Type}
	 */
	public MonotonePolygon.Type getType() {
		return this.type;
	}
	
	/**
	 * Returns the sorted list of vertices.
	 * @return List&lt;{@link MonotoneVertex}&gt;
	 */
	public List<MonotoneVertex<E>> getVertices() {
		return this.vertices;
	}
	
	/**
	 * Sets the sorted list of vertices.
	 * @param vertices the sorted list of vertices; descending order
	 */
	public void setVertices(List<MonotoneVertex<E>> vertices) {
		this.vertices = vertices;
	}
}

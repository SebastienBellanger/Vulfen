package com.tojosebe.vulfen.game;

import com.vulfox.math.Vector2f;

public class CollisionCalculator {
	
	/**
	 * Calculates if a Circle collides with an orthogonal square. Returns the collision vector if
	 * collision or null if no collision.
	 * @param circle the circle.
	 * @param square the square.
	 * @return collision vector if collision or null if no collision.
	 */
	public static boolean circleOrtogonalSquareCollision(Pong circle, Brick square) {
		
	    float circleDistanceX = Math.abs(circle.getPosition().getX() - square.getPosition().getX());
	    float circleDistanceY = Math.abs(circle.getPosition().getY() - square.getPosition().getY());

	    //1. Eliminate the easy cases where the circle is far enough away from the rectangle (in either direction) that no intersection is possible.
		if (circleDistanceX > (square.getWidth() * 0.5f + circle.getRadius())) {
			return false;
		}
		if (circleDistanceY > (square.getHeight() * 0.5f + circle.getRadius())) {
			return false;
		}

		//2. Handle the easy cases where the circle is close enough to the rectangle (in either direction) that an intersection is guaranteed.
		if (circleDistanceX <= (square.getWidth() * 0.5f)) {
			return true;
		}
		if (circleDistanceY <= (square.getHeight() * 0.5f)) {
			return true;
		}

	    float cornerDistanceSquare = square(circleDistanceX - square.getWidth() * 0.5f) + square(circleDistanceY - square.getHeight() * 0.5f);
	    
	    return cornerDistanceSquare <= (square(circle.getRadius()));
	    
//		Vector2f collisionVector = null;
		
//		/* First check for collision against the corners. */
//		if (circlePointCollision(circle, square.getPositionCorner1())) {
//			collisionVector = calculateCollisionVector(circle, square.getPositionCorner1());
//		} else if (circlePointCollision(circle, square.getPositionCorner2())) {
//			collisionVector = calculateCollisionVector(circle, square.getPositionCorner2());
//		} else if (circlePointCollision(circle, square.getPositionCorner3())) {
//			collisionVector = calculateCollisionVector(circle, square.getPositionCorner3());
//		} else if (circlePointCollision(circle, square.getPositionCorner4())) {
//			collisionVector = calculateCollisionVector(circle, square.getPositionCorner4());
//		}
//		
//		/* Now check for collision against the sides. */
//		if (collisionVector == null) {
//			//TODO
//		}
		
//		return collisionVector;
	}
	
	private static float square(float number) {
		return number * number;
	}

	private static Vector2f calculateCollisionVector(Pong circle,
			Vector2f positionCorner1) {
		// TODO
		return null;
	}

	private static boolean circlePointCollision(Pong circle, Vector2f positionCorner1) {
		//TODO
		return false;
	}


	
}

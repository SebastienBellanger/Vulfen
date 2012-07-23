package com.tojosebe.vulfen.game;

import com.vulfox.math.Vector2f;

public class CollisionCalculator {
	
	/** Help vector. Saves memory by not allocating new every time. */
	private static Vector2f collisionCheckVector = new Vector2f();
	
	public static boolean circleCircleCollision(Vector2f circle1Pos, Vector2f circle2Pos, float radiusCircle1, float radiusCircle2) {
		
		circle1Pos.sub(circle2Pos, collisionCheckVector);
		
		float lengthSquared = collisionCheckVector.getLengthSquared();
		float distance = radiusCircle1 + radiusCircle2;
		
		return lengthSquared < (distance * distance);
	}
	
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
	}
	
	private static float square(float number) {
		return number * number;
	}

	/**
	 * If a collision has happened update the velocity vector for the Circle. The square is solid.
	 * PRECONDITION: check if a collision has really happened. 
	 * @param circle
	 * @param square
	 */
	public static void calculateCollisionVectorCircleVsSolidSquare(Pong circle,
			Brick square) {
		
		boolean done = false;

		/* First check for collision against the corners. */
		
		//top left corner
		if (circle.getPosition().getX() < square.getPositionCorner1().getX() &&
				circle.getPosition().getY() < square.getPositionCorner1().getY()) {
			calculateCollisionVectorCircleSolidPoint(circle, square.getPositionCorner1());
			done = true;
//			System.out.println("COLLISION Corner1");
//			//move circle out of square TODO: egentligen ska vi flytta ut cirkeln längs riktningsvectorn.
//			float diffX = Math.abs(square.getPositionCorner1().getX() - circle.getPosition().getX());
//			float diffY = Math.abs(square.getPositionCorner1().getY() - circle.getPosition().getY());
//			if (diffY < diffX) {
//				circle.getPosition().setX(circle.getPosition().getX() - Math.abs(circle.getRadius() - diffX));
//			} else {
//				circle.getPosition().setY(circle.getPosition().getY() - Math.abs(circle.getRadius() - diffY));
//			}
		//top right corner
		} else if (circle.getPosition().getX() > square.getPositionCorner2().getX() &&
				circle.getPosition().getY() < square.getPositionCorner2().getY()) {
			calculateCollisionVectorCircleSolidPoint(circle, square.getPositionCorner2());
			done = true;
//			System.out.println("COLLISION Corner2");
//			//move circle out of square TODO: egentligen ska vi flytta ut cirkeln längs riktningsvectorn.
//			float diffX = Math.abs(square.getPositionCorner2().getX() - circle.getPosition().getX());
//			float diffY = Math.abs(square.getPositionCorner2().getY() - circle.getPosition().getY());
//			if (diffY < diffX) {
//				circle.getPosition().setX(circle.getPosition().getX() + Math.abs(circle.getRadius() - diffX));
//			} else {
//				circle.getPosition().setY(circle.getPosition().getY() - Math.abs(circle.getRadius() - diffY));
//			}
		//bottom left corner	
		} else if (circle.getPosition().getX() < square.getPositionCorner3().getX() &&
			circle.getPosition().getY() > square.getPositionCorner3().getY()) {
			calculateCollisionVectorCircleSolidPoint(circle, square.getPositionCorner3());
			done = true;
//			System.out.println("COLLISION Corner3");
//			//move circle out of square TODO: egentligen ska vi flytta ut cirkeln längs riktningsvectorn.
//			float diffX = Math.abs(square.getPositionCorner3().getX() - circle.getPosition().getX());
//			float diffY = Math.abs(square.getPositionCorner3().getY() - circle.getPosition().getY());
//			if (diffY < diffX) {
//				circle.getPosition().setX(circle.getPosition().getX() - Math.abs(circle.getRadius() - diffX));
//			} else {
//				circle.getPosition().setY(circle.getPosition().getY() + Math.abs(circle.getRadius() - diffY));
//			}
		//bottom right corner	
		} else if (circle.getPosition().getX() > square.getPositionCorner4().getX() &&
				circle.getPosition().getY() > square.getPositionCorner4().getY()) {
				calculateCollisionVectorCircleSolidPoint(circle, square.getPositionCorner4());
				done = true;
//				System.out.println("COLLISION Corner4");
//				//move circle out of square TODO: egentligen ska vi flytta ut cirkeln längs riktningsvectorn.
//				float diffX = Math.abs(square.getPositionCorner4().getX() - circle.getPosition().getX());
//				float diffY = Math.abs(square.getPositionCorner4().getY() - circle.getPosition().getY());
//				if (diffY < diffX) {
//					circle.getPosition().setX(circle.getPosition().getX() + Math.abs(circle.getRadius() - diffX));
//				} else {
//					circle.getPosition().setY(circle.getPosition().getY() + Math.abs(circle.getRadius() - diffY));
//				}
		}
		
		if (!done) {
			//It is a collision against one of the sides. Find out which one!
			if (circle.getPosition().getY() > square.getPosition().getY() - square.getHeight() * 0.5f && circle.getPosition().getY() < square.getPosition().getY() + square.getHeight() * 0.5f) {
				//It is a collision against left or right side.
				if (circle.getPosition().getX() < square.getPosition().getX()) {
					//collision against left side!
					System.out.println("COLLISION left side");
					circle.velocity.setX(-circle.velocity.getX());
					//move circle out of square TODO: egentligen ska vi flytta ut cirkeln längs riktningsvectorn.
					float move = circle.getRadius() - (square.getPositionCorner1().getX() - circle.getPosition().getX());
					circle.getPosition().setX(circle.getPosition().getX() - Math.abs(move));
				} else {
					//collision against right side!
					System.out.println("COLLISION right side");
					circle.velocity.setX(-circle.velocity.getX());
					//move circle out of square TODO: egentligen ska vi flytta ut cirkeln längs riktningsvectorn.
					float move = circle.getRadius() - (circle.getPosition().getX() - square.getPositionCorner2().getX());
					circle.getPosition().setX(circle.getPosition().getX() + Math.abs(move));
				}
			} else {
				if (circle.getPosition().getY() < square.getPosition().getY()) {
					//collision against top side!
					System.out.println("COLLISION top side");
					circle.velocity.setY(-circle.velocity.getY());
					//move circle out of square TODO: egentligen ska vi flytta ut cirkeln längs riktningsvectorn.
					float move = circle.getRadius() - (square.getPositionCorner1().getY() - circle.getPosition().getY());
					circle.getPosition().setY(circle.getPosition().getY() - Math.abs(move));
				} else {
					//collision against bottom side!
					System.out.println("COLLISION bottom side");
					circle.velocity.setY(-circle.velocity.getY());
					//move circle out of square TODO: egentligen ska vi flytta ut cirkeln längs riktningsvectorn.
					float move = circle.getRadius() - (circle.getPosition().getY() - square.getPositionCorner3().getY());
					circle.getPosition().setY(circle.getPosition().getY() + Math.abs(move));
				}
			}
		}
	}

	private static void calculateCollisionVectorCircleSolidPoint(Pong circle,
			Vector2f positionCorner) {
		
		circle.getPosition().sub(positionCorner, collisionCheckVector);
		
		float length = collisionCheckVector.getLength(); //TODO: sqrt

		Vector2f mtd = collisionCheckVector.mul((circle.getRadius() - length) / length);

		circle.getPosition().addT(mtd.mul(0.505f));
		positionCorner.subT(mtd.mul(0.505f));
		
		Vector2f pointVelocity = new Vector2f(circle.velocity); // TODO: new
		pointVelocity.invT();

		//same as normalizeT:
		collisionCheckVector.divT(length);

		float aci = circle.velocity.dot(collisionCheckVector);
		float bci = pointVelocity.dot(collisionCheckVector);

		circle.velocity.addT(collisionCheckVector.mul((bci - aci) * 0.90f));
	}

	private static boolean circlePointCollision(Pong circle, Vector2f positionCorner1) {
		return circleCircleCollision(circle.getPosition(), positionCorner1, circle.getRadius(), 2.0f);
	}


	
}

package com.tojosebe.vulfen.game;

import com.vulfox.math.Vector2f;
import com.vulfox.util.Logger;

public class CollisionCalculator {

	/** Help vector. Saves memory by not allocating new every time. */
	private static Vector2f collisionCheckVector = new Vector2f();

	private static Vector2f edgeNormalUp = new Vector2f(0.0f, -1.0f);
	private static Vector2f edgeNormalRight = new Vector2f(1.0f, 0.0f);
	private static Vector2f edgeNormalDown = new Vector2f(0.0f, 1.0f);
	private static Vector2f edgeNormalLeft = new Vector2f(-1.0f, 0.0f);

	public static boolean circleCircleCollision(Vector2f circle1Pos,
			Vector2f circle2Pos, float radiusCircle1, float radiusCircle2) {

		circle1Pos.sub(circle2Pos, collisionCheckVector);

		float lengthSquared = collisionCheckVector.getLengthSquared();
		float distance = radiusCircle1 + radiusCircle2;

		return lengthSquared < (distance * distance);
	}

	/**
	 * Calculates if a Circle collides with an orthogonal square. Returns the
	 * collision vector if collision or null if no collision.
	 * 
	 * @param circle
	 *            the circle.
	 * @param square
	 *            the square.
	 * @return collision vector if collision or null if no collision.
	 */
	public static boolean circleOrtogonalSquareCollision(Pong circle,
			Brick square) {

		// clamp(value, min, max) - limits value to the range min..max

		// Find the closest point to the circle within the rectangle
		float closestX = clamp(circle.getPosition().getX(), square.getEdgeLeft().getX(), square.getEdgeRight().getX());
		float closestY = clamp(circle.getPosition().getY(), square.getEdgeUp().getY(), square.getEdgeDown().getY());

		// Calculate the distance between the circle's center and this closest point
		float distanceX = circle.getPosition().getX() - closestX;
		float distanceY = circle.getPosition().getY() - closestY;

		// If the distance is less than the circle's radius, an intersection occurs
		float distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);

		return distanceSquared < (circle.getWidth()*0.5f * circle.getWidth()*0.5f);
	}
	
	private static float clamp(float value, float min, float max) {
		float result = value;

		if (value > max) {
			result = max;
		}

		if (value < min) {
			result = min;
		}

		return result;
	}

	/**
	 * If a collision has happened update the velocity vector for the Circle.
	 * The square is solid. PRECONDITION: check if a collision has really
	 * happened.
	 * @param timeStep 
	 * 
	 * @param circle
	 * @param square
	 */
	public static void calculateCollisionVectorCircleVsSolidSquare(Pong pong,
			Brick brick, boolean checkCorners, boolean checkSides, float timeStep) {

		boolean done = false;
		boolean hitUp = false;
		boolean hitDown = false;
		boolean hitLeft = false;
		boolean hitRight = false;

		Vector2f circleToSideVector = collisionCheckVector;
		
		/** First check which sides are hit. */

		// Down side
		pong.getPosition().clone(circleToSideVector);
		brick.getEdgeDown().sub(circleToSideVector, circleToSideVector);
		if (circleToSideVector.dot(edgeNormalDown) <= 0) {
			hitDown = true;
		}

		// Up side
		pong.getPosition().clone(circleToSideVector);
		brick.getEdgeUp().sub(circleToSideVector, circleToSideVector);
		if (circleToSideVector.dot(edgeNormalUp) <= 0) {
			hitUp = true;
		}

		// Left side
		pong.getPosition().clone(circleToSideVector);
		brick.getEdgeLeft().sub(circleToSideVector, circleToSideVector);
		if (circleToSideVector.dot(edgeNormalLeft) <= 0) {
			hitLeft = true;
		}

		// Right side
		pong.getPosition().clone(circleToSideVector);
		brick.getEdgeRight().sub(circleToSideVector, circleToSideVector);
		if (circleToSideVector.dot(edgeNormalRight) <= 0) {
			hitRight = true;
		}

		/* First check for collision against the corners. */

		if (checkCorners) {
			
			// top left corner
			if (hitUp && hitLeft) {
				
				calculateCollisionVectorCircleSolidPoint(pong,
						brick.getPositionCorner1());
				done = true;
				Logger.log("COLLISION Corner1");
				
				//move circle out of square
				float xDiff = pong.getPosition().getX() + pong.getRadius() - brick.getPositionCorner1().getX();
				float yDiff = pong.getPosition().getY() + pong.getRadius() - brick.getPositionCorner1().getY();
				
				if (xDiff > yDiff) {
					pong.getPosition().setY(pong.getPosition().getY() - yDiff - 1.0f); 
				} else {
					pong.getPosition().setX(pong.getPosition().getX() - xDiff - 1.0f);
				}

			} else if (hitUp && hitRight) {
				calculateCollisionVectorCircleSolidPoint(pong,
						brick.getPositionCorner2());
				done = true;
				Logger.log("COLLISION Corner2");
				
				//move circle out of square
				float xDiff = brick.getPositionCorner2().getX() - (pong.getPosition().getX() - pong.getRadius());
				float yDiff = pong.getPosition().getY() + pong.getRadius() - brick.getPositionCorner2().getY();
				
				if (xDiff > yDiff) {
					pong.getPosition().setY(pong.getPosition().getY() - yDiff - 1.0f); 
				} else {
					pong.getPosition().setX(pong.getPosition().getX() + xDiff + 1.0f);
				}

				// bottom left corner
			} else if (hitDown && hitLeft) {
				calculateCollisionVectorCircleSolidPoint(pong,
						brick.getPositionCorner3());
				done = true;
				Logger.log("COLLISION Corner3");
				
				//move circle out of square
				float xDiff = pong.getPosition().getX() + pong.getRadius() - brick.getPositionCorner3().getX();
				float yDiff = brick.getPositionCorner3().getY() - (pong.getPosition().getY() - pong.getRadius());
				
				if (xDiff > yDiff) {
					pong.getPosition().setY(pong.getPosition().getY() + yDiff + 1.0f); 
				} else {
					pong.getPosition().setX(pong.getPosition().getX() - xDiff - 1.0f);
				}

				// bottom right corner
			} else if (hitDown && hitRight) {
				calculateCollisionVectorCircleSolidPoint(pong,
						brick.getPositionCorner4());
				done = true;
				Logger.log("COLLISION Corner4");
				
				//move circle out of square
				float xDiff = brick.getPositionCorner4().getX() - (pong.getPosition().getX() - pong.getRadius());
				float yDiff = brick.getPositionCorner4().getY() - (pong.getPosition().getY() - pong.getRadius());
				
				if (xDiff > yDiff) {
					pong.getPosition().setY(pong.getPosition().getY() + yDiff + 1.0f); 
				} else {
					pong.getPosition().setX(pong.getPosition().getX() + xDiff + 1.0f);
				}
			}
		}

		if (!done && checkSides) {
			
			if (hitLeft) {
				
				float penetration = (brick.getWidth() * 0.5f + pong.getRadius()) - (brick.getPosition().getX() - pong.getPosition().getX());
				
				Logger.log("penetration " + penetration);
				if (penetration >= -0.001f) { 
					Logger.log("COLLISION left side. Penetration is " + penetration);
					pong.getPosition().setX(pong.getPosition().getX() - penetration);
					pong.velocity.setX(-pong.velocity.getX());
				}
				
			} else if (hitRight) {
				
				float penetration = (brick.getWidth() * 0.5f + pong.getRadius()) - (pong.getPosition().getX() - brick.getPosition().getX());
				
				Logger.log("penetration " + penetration);

				if (penetration >= -0.001f) {
					Logger.log("COLLISION right side. Penetration is " + penetration);
					pong.getPosition().setX(pong.getPosition().getX() + penetration);
					pong.velocity.setX(-pong.velocity.getX());
				}
			} else if (hitUp) {
				
				float penetration = (brick.getHeight() * 0.5f + pong.getRadius()) - (brick.getPosition().getY() - pong.getPosition().getY());
				
				Logger.log("penetration " + penetration);
				
				if (penetration >= -0.001f) {
					pong.velocity.setY(-pong.velocity.getY());
					Logger.log("COLLISION top side. Penetration is " + penetration);
					pong.getPosition().setY(pong.getPosition().getY() - penetration);
				}
			} else if (hitDown) {
				
				float penetration = (brick.getHeight() * 0.5f + pong.getRadius()) - (pong.getPosition().getY() - brick.getPosition().getY());
				
				Logger.log("penetration " + penetration);
				
				if (penetration >= -0.001f) {
					pong.velocity.setY(-pong.velocity.getY());
					Logger.log("COLLISION bottom side. Penetration is " + penetration);
					pong.getPosition().setY(pong.getPosition().getY() + penetration);
				}
			}
		}
	}

	private static void calculateCollisionVectorCircleSolidPoint(Pong circle,
			Vector2f positionCorner) {

		circle.getPosition().sub(positionCorner, collisionCheckVector);

		float length = collisionCheckVector.getLength(); // TODO: sqrt

		Vector2f mtd = collisionCheckVector.mul((circle.getRadius() - length)
				/ length);

		circle.getPosition().addT(mtd.mul(0.505f));
		positionCorner.subT(mtd.mul(0.505f));

		Vector2f pointVelocity = new Vector2f(circle.velocity); // TODO: new
		pointVelocity.invT();

		// same as normalizeT:
		collisionCheckVector.divT(length);

		float aci = circle.velocity.dot(collisionCheckVector);
		float bci = pointVelocity.dot(collisionCheckVector);

		circle.velocity.addT(collisionCheckVector.mul((bci - aci) * 0.90f));
	}

	// private static boolean circlePointCollision(Pong circle, Vector2f
	// positionCorner1) {
	// return circleCircleCollision(circle.getPosition(), positionCorner1,
	// circle.getRadius(), 2.0f);
	// }

	public static boolean circleOrtogonalSquareSingleSideCollision(Pong pong,
			Brick brick) {

		int hits = 0;
		Vector2f circleToSideVector = collisionCheckVector;

		// Check which side we have a collision with.
		// Calculate dot product of circle to side center vector and side
		// normals.
		// the side that is "hit" will be positive".
		// if more than dot product is positive a corner is hit.

		// Down side
		pong.getPosition().clone(circleToSideVector);
		// subtract circleToSideVector and store in circleToSideVector.
		brick.getEdgeDown().sub(circleToSideVector, circleToSideVector);
		if (circleToSideVector.dot(edgeNormalDown) <= 0) {
			Logger.log("WE HIT THE DOWN SIDE");
			hits++;
		}

		// Up side
		pong.getPosition().clone(circleToSideVector);
		// subtract circleToSideVector and store in circleToSideVector.
		brick.getEdgeUp().sub(circleToSideVector, circleToSideVector);
		if (circleToSideVector.dot(edgeNormalUp) <= 0) {
			Logger.log("WE HIT THE UP SIDE");
			hits++;
		}

		// Left side
		pong.getPosition().clone(circleToSideVector);
		// subtract circleToSideVector and store in circleToSideVector.
		brick.getEdgeLeft().sub(circleToSideVector, circleToSideVector);
		if (circleToSideVector.dot(edgeNormalLeft) <= 0) {
			Logger.log("WE HIT THE LEFT SIDE");
			hits++;
		}

		// Right side
		pong.getPosition().clone(circleToSideVector);
		// subtract circleToSideVector and store in circleToSideVector.
		brick.getEdgeRight().sub(circleToSideVector, circleToSideVector);
		if (circleToSideVector.dot(edgeNormalRight) <= 0) {
			Logger.log("WE HIT THE RIGHT SIDE");
			hits++;
		}

		return hits == 1;
	}

	public static boolean circleOrtogonalSquareCornerCollision(Pong pong,
			Brick brick) {
		int hits = 0;
		Vector2f circleToSideVector = collisionCheckVector;

		// Check which side we have a collision with.
		// Calculate dot product of circle to side center vector and side
		// normals.
		// the side that is "hit" will be positive".
		// if more than 1 dot product is positive then one of the corners are hit.

		// Down side
		pong.getPosition().clone(circleToSideVector);
		// subtract circleToSideVector and store in circleToSideVector.
		brick.getEdgeDown().sub(circleToSideVector, circleToSideVector);
		if (circleToSideVector.dot(edgeNormalDown) <= 0) {
			Logger.log("WE HIT THE DOWN SIDE (corner check)");
			hits++;
		}

		// Up side
		pong.getPosition().clone(circleToSideVector);
		// subtract circleToSideVector and store in circleToSideVector.
		brick.getEdgeUp().sub(circleToSideVector, circleToSideVector);
		if (circleToSideVector.dot(edgeNormalUp) <= 0) {
			Logger.log("WE HIT THE UP SIDE (corner check)");
			hits++;
			if (hits == 2) {
				return true;
			}
		}

		// Left side
		pong.getPosition().clone(circleToSideVector);
		// subtract circleToSideVector and store in circleToSideVector.
		brick.getEdgeLeft().sub(circleToSideVector, circleToSideVector);
		if (circleToSideVector.dot(edgeNormalLeft) <= 0) {
			Logger.log("WE HIT THE LEFT SIDE (corner check)");
			hits++;
			if (hits == 2) {
				return true;
			}
		}

		// Right side
		pong.getPosition().clone(circleToSideVector);
		// subtract circleToSideVector and store in circleToSideVector.
		brick.getEdgeRight().sub(circleToSideVector, circleToSideVector);
		if (circleToSideVector.dot(edgeNormalRight) <= 0) {
			Logger.log("WE HIT THE RIGHT SIDE (corner check)");
			hits++;
			if (hits == 2) {
				return true;
			}
		}

		return false;
	}

}

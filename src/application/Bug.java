package application;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

//References
/* 
 * https://www.programcreek.com/java-api-examples/?class=javafx.scene.shape.Circle&method=setStroke
 * 
 */


/**
 * Represents a Bug in the world and describes how it can operate within the world
 * 
 *
 */
public abstract class Bug extends Circle {

	BugWorldFX main;
	
	//Tracks its movement behaviour
	private int moveCounter = 0;
	protected int chosenVertical;
	protected int chosenHorizontal;
	private boolean moveChosen = false;
	
	//Plant interaction
	protected Plant interactedPlant;
	protected Coordinate collisionEdge;
	
	//Stats to describe the state of the bug
	protected boolean isHungry = false;;
	protected double maxEnergy;
	protected double hungerThreshold;
	
	protected boolean isDead = false;
	protected double deathThreshold = 15;
	
	//Images that represent the bug
	Image bugImage;
	Image bugHungryImage;
	Image bugDeadImage;
	
	//How much the bug will move when it moves
	protected float moveAmount = 2;
	
	
	
	
	public Bug(double x, double y, double radius, BugWorldFX main) {
		
		
		super(x, y, radius);

		this.main = main;
		

		maxEnergy = radius; //Radius indirectly represents the bug's hunger / energy level
		hungerThreshold = radius/1.3;
		
	}
	
	/**
	 * Makes the bug act accordingly in the world depending on whether it is dead or alive and whether it interacts
	 * with any other plants
	 */
	public void behave() {
		
		if (!isDead) {
			
			moveRandomly();
					
			if(this.plantCollisionCheck()) {
				
				plantInteraction();
			}
			
			keepInBounds();
		}
	}
	
	/**
	 * Causes movement decisions (Can be vertical, horizontal or diagonal)
	 * 
	 * 
	 */
	public void moveRandomly() {
		
		//Choose a direction of movement
		if (!moveChosen) {
		
			//Choose up or down
			int upOrDown = makeDecision();
			int leftOrRight = makeDecision();
			
			bugMovement(upOrDown, leftOrRight, moveAmount);
			
			chosenVertical = upOrDown;
			chosenHorizontal = leftOrRight;
			moveChosen = true;
	
	
		}
		
		//If already chosen a direction to move, keep moving in that direction until you 
		//have hit the movement limit then choose another movement
		else {
			
			int moveLimit = 30;
			
			if (moveCounter > moveLimit) {
				
				moveChosen = false;
				moveCounter = 0;
				moveRandomly();
				return;
				
			}
			
			else {
				
				bugMovement(chosenVertical, chosenHorizontal, moveAmount);
				moveCounter++;
					
			}	
			
		}
		
	}

	/**
	 * Based on the decisions the bug has made it will actually make the bug move
	 * 
	 * Also calculates the energy cost of movement and updates its physical representation
	 * 
	 * If the bug is hungry and in contact with a plant will also eat it.
	 * 
	 * @param upOrDown
	 * @param leftOrRight
	 * @param moveAmount
	 */
	private void bugMovement(int upOrDown, int leftOrRight, float moveAmount) {
		
		//Move down
		if (upOrDown == 1) {
			
			this.setCenterY(this.getCenterY() + moveAmount);
		}
		//move up
		else if (upOrDown == 2) {
			
			this.setCenterY(this.getCenterY() - moveAmount);
		}
		
		//move left
		if (leftOrRight == 1) {
			
			this.setCenterX(this.getCenterX() - moveAmount);
		}
		//move right
		else if (leftOrRight == 2){
			
			this.setCenterX(this.getCenterX() + moveAmount);
	
		}
		
		//Energy Cost Per Cycle
		this.setRadius(this.getRadius() - 0.01);
		
		//Make sure to check if it should've died before running the eating method
		//Otherwise the bug will "die" then eat and come back to life.
		checkStatus();
		
		if (!isDead) {
			
			this.plantEatCheck();
		}
	}

	/**
	 * Bug checks its status to figure out whether it is dead, hungry or full.
	 * Updates its image accordingly
	 */
	protected void checkStatus() {
		
		// TODO Auto-generated method stub
		if (this.getRadius() < deathThreshold) {
			
			isDead = true;
			this.setFill(new ImagePattern(bugDeadImage));
		}
		
		else if (this.getRadius() < hungerThreshold) {
			
			isHungry = true;
			this.setFill(new ImagePattern(bugHungryImage));
		}
		
		else if (this.getRadius() > hungerThreshold) {
			
			isHungry = false;
			this.setFill(new ImagePattern(bugImage));
		}
		
	}

	/**
	 * Utility method to generate a random int
	 * @param min
	 * @param max
	 * @return
	 */
	static public int numberInRange(int min, int max) {
		
		return (int) Math.floor(Math.random()*(max - min + 1) + min);
	
	}

	//Helps decide on movement direction (0 means stay still, 1 and 2 determine left/right or up/down depending on context)
	private int makeDecision() {
		
		return numberInRange(0, 2);
	}

	
	/**
	 * Makes the bugs stay within the world by not going past the edges of the stage
	 * 
	 * Also accounts for user resizing the stage
	 * 
	 */
	private void keepInBounds() {
		
		double currentSceneWidth = main.bugArea.getWidth();
		double currentSceneHeight = main.bugArea.getHeight();	
		
		//Code to account for user resizing the stage to excessively small dimensions
		//The bugs otherwise start bouncing into the other panes due to the nature of my code
		//This code causes them to stay "calm"
		//
		//To be honest with you this was actually a happy accident
		//My original intention was actually just to make the bug "disappear" from the scene. But I forgot to add return statements.
		//But this actually works better!
		//
		//These statements also appear to work in conjunction with the statements below it, because if I 
		//Comment the "code to account for user resizing the stage" then it doesn't exhibit the correct behaviour
		if (currentSceneWidth < 50) {
			
			this.setCenterX(5000);
		}
		
		if (currentSceneHeight < 50) {
			
			this.setCenterY(5000);
		}
		
		
		//Code to account for user resizing the stage
		//Keeps bugs within the stage by "teleporting" them back to their furthest valid position in the scene
		//Then reverses its movement decision
		if (this.getCenterX() > currentSceneWidth) {
			
			this.setCenterX(currentSceneWidth - this.getRadius());
			this.chosenHorizontal = reverseDirection(chosenHorizontal);	
		}
		
		if (this.getCenterY() > currentSceneHeight) {
			
			this.setCenterY(currentSceneHeight - this.getRadius());
			this.chosenVertical = reverseDirection(chosenVertical);
		}
		
		
		double bugAreaXStart = this.getRadius();
		double bugAreaXEnd = main.wholeScene.getWidth() - this.getRadius();
		
		double bugAreaYStart = this.getRadius();
		double bugAreaYEnd = main.bugArea.getHeight() - this.getRadius(); //- main.topPaneHeight - this.getRadius();
		
		//Code to account for going out of bounds of pre-existing scene bounds
		//Again by teleporting it back to its furthest valid position and reversing its movement direction
		if (this.getCenterX() <= bugAreaXStart) {
			
			this.setCenterX(bugAreaXStart);
			this.chosenHorizontal = reverseDirection(chosenHorizontal);		
		}
		else if (this.getCenterX() >= bugAreaXEnd) {
			
			this.setCenterX(bugAreaXEnd);
			this.chosenHorizontal = reverseDirection(chosenHorizontal);
		}
		
		if (this.getCenterY() <= bugAreaYStart) {
			
			this.setCenterY(bugAreaYStart);
			this.chosenVertical = reverseDirection(chosenVertical);
		}
		else if (this.getCenterY() >= bugAreaYEnd) {
			
			this.setCenterY(bugAreaYEnd);
			this.chosenVertical = reverseDirection(chosenVertical);
			
		}
		
	}
	
	/**
	 * After a bug has detected a collision with a plant, makes them bounce off the plant rather than
	 * going straight through it
	 * 
	 */
	private void plantInteraction() {
		
		//An adjustment factor for the teleport, we want it to teleport just far away enough
		//To be imperceptible to the user but without triggering further unnecessary collision detection
		double adjustmentFactor = 20;
		
		//Decides whether the intrusion point was closer to the left edge or right edge
		//And teleports the bug to closest valid position of the closest edge with the adjustment in place
		//Then reverses its direction
		if (collisionEdge.x >= interactedPlant.plantLeft && collisionEdge.x <= interactedPlant.plantRight) {
			
			double distanceFromLeft = Math.abs(collisionEdge.x - interactedPlant.plantLeft);
			double distanceFromRight = Math.abs(collisionEdge.x - interactedPlant.plantRight);
			
			if (distanceFromLeft < distanceFromRight) {
				
				this.setCenterX(this.getCenterX() - adjustmentFactor);
				chosenHorizontal = reverseDirection(chosenHorizontal);
			}
			else {
				
				this.setCenterX(this.getCenterX() + adjustmentFactor);
				chosenHorizontal = reverseDirection(chosenHorizontal);
			}
			
		}
		
		//Decides whether the intrusion point was closer to the top edge or right edge, teleports them appropriately and reverses the direction
		if (collisionEdge.y >= interactedPlant.plantTop && collisionEdge.x <= interactedPlant.plantTop) {
			
			double distanceFromTop = Math.abs(collisionEdge.y - interactedPlant.plantTop);
			double distanceFromBottom = Math.abs(collisionEdge.y - interactedPlant.plantBottom);
			
			if (distanceFromTop < distanceFromBottom) {
				
				this.setCenterY(this.getCenterY() - adjustmentFactor);
				chosenVertical = reverseDirection(chosenVertical);
			}
			else {
				
				this.setCenterY(this.getCenterY() + adjustmentFactor);
				chosenVertical = reverseDirection(chosenVertical);
			}
			
		}
		
		interactedPlant = null; //Reset that value since it should no longer be interacting with that plant
		
		
	}

	/*Knowing that the decision maker produces only 3 values (0, 1, 2 - where 0 represents no movement and 1 and 2 represent
	 * left/right or up/down) 
	 * 
	 * If bug was moving left, then make them move right.   If moving up, make move down.
	 */
	private int reverseDirection(int directionIndicator) {
		
		if (directionIndicator == 1) {
			
			return 2;
		}
		if (directionIndicator == 2) {
			
			return 1;
		}
		
		return 0;
		
	}
	
	/**
	 * Returns true if the bug has collided with a plant and also records which plant it has collided with
	 * @return
	 */
	private boolean plantCollisionCheck() {
		
		ArrayList<Plant> plantList = main.plantList;
		
		//Determine key coordinates of the Circle that represents the bug's dimensions
		double centerX = this.getCenterX(); //Center
		double bugREdgeHalf = this.getCenterX() + (this.getRadius() / 2);
		double bugREdge = this.getCenterX() + this.getRadius(); //Full Right
		double bugLEdgeHalf = this.getCenterX() - (this.getRadius() / 2); //Full Left
		double bugLEdge = this.getCenterX() - this.getRadius(); //Full Left

		
		double centerY = this.getCenterY(); //Center
		double bugTEdgeHalf = this.getCenterY() - (this.getRadius() / 2); //Full Top
		double bugTEdge = this.getCenterY() - this.getRadius(); //Full Top
		double bugBedgeHalf = this.getCenterY() + (this.getRadius() / 2);
		double bugBedge = this.getCenterY() + this.getRadius(); //Full Bottom
		
		ArrayList<Coordinate> collisionEdges = new ArrayList<Coordinate>();
		
		//Center (just in case)
		collisionEdges.add(new Coordinate(centerX, centerY));
		
		//Top, Right, Bottom, Left
		collisionEdges.add(new Coordinate(centerX, bugTEdge));
		collisionEdges.add(new Coordinate(bugREdge, centerY));
		collisionEdges.add(new Coordinate(centerX, bugBedge));
		collisionEdges.add(new Coordinate(bugLEdge, centerY));
		
		//HorzRightTop, HorzRightBot, HorzLeftTop, HorzLeftBot;
		collisionEdges.add(new Coordinate(bugREdgeHalf, bugTEdgeHalf));
		collisionEdges.add(new Coordinate(bugREdgeHalf, bugBedgeHalf));
		collisionEdges.add(new Coordinate(bugLEdgeHalf, bugTEdgeHalf));
		collisionEdges.add(new Coordinate(bugLEdgeHalf, bugTEdgeHalf));

	
		//Check all plants and check if any of the coordinates of the bug have collided with the plant
		for (Plant plant : plantList) {
						
			for (Coordinate coordinate : collisionEdges) {
				
				if (coordinate.x >= plant.plantLeft && coordinate.x <= plant.plantRight) {
					
					if (coordinate.y >= plant.plantTop && coordinate.y <= plant.plantBottom) {
						
						interactedPlant = plant;
						collisionEdge = coordinate;
						
						return true;
					}
					
				}
				
			}
			
		}
		
		return false;
	}
	
	
	/**
	 * Adaptation of existing detection code but a bit more forgiving to make sure the bug does eat the plant
	 * @return
	 */
	private boolean plantEatCheck() {
			
			ArrayList<Plant> plantList = main.plantList;
			
			double[] horzEdges = new double[3];
			
			horzEdges[0] = this.getCenterX();
			double bugREdge = this.getCenterX() + this.getRadius();
			horzEdges[1] = bugREdge;
			double bugLEdge = this.getCenterX() - this.getRadius();
			horzEdges[2] = bugLEdge;
	
			
			double[] vertEdges = new double[3];
			
			vertEdges[0] = this.getCenterY();
			double bugTEdge = this.getCenterY() - this.getRadius();
			vertEdges[1] = bugTEdge;
			double BugBedge = this.getCenterY() + this.getRadius();
			vertEdges[2] = BugBedge;
			
			ArrayList<Coordinate> collisionEdges = new ArrayList<Coordinate>();
			
			collisionEdges.add(new Coordinate(horzEdges[0], vertEdges[1]));
			collisionEdges.add(new Coordinate(horzEdges[0], vertEdges[2]));
			
			collisionEdges.add(new Coordinate(horzEdges[1], vertEdges[0]));
			collisionEdges.add(new Coordinate(horzEdges[2], vertEdges[0]));
			
			collisionEdges.add(new Coordinate(horzEdges[0], vertEdges[0]));
	
	
			
			
			for (Plant plant : plantList) {
							
				for (Coordinate coordinate : collisionEdges) {
					
					if (coordinate.x >= plant.plantLeft - 10 && coordinate.x <= plant.plantRight + 10) {
						
						if (coordinate.y >= plant.plantTop - 10 && coordinate.y <= plant.plantBottom + 10) {
							
							if (isHungry) {
								
								eat(plant);
							}
							
							return true;
						}
						
					}
					
				}
				
			}
			
			return false;
		}

	/**
	 * Eats the given plant and restores the energy of the bug
	 * 
	 * @param interactedPlant
	 */
	private void eat(Plant interactedPlant) {
		
		if (interactedPlant.alive) {
			
			interactedPlant.gotEaten();
			
			this.setRadius(maxEnergy);
		}
		
	}

	

}

//Image taken from https://www.pngegg.com/en/png-btrfh

package application;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

/**
 * Represents a bumbleebee in the world
 * 
 * Bumblebees move quickly
 *
 */
public class BumbleBee extends Bug {

	Image bugRightImage = new Image("bumblebee.png");
	Image bugLeftImage = new Image("bumblebeeLeft.png");
	
	Image bugHungryRightImage = new Image("bumblebeeHungry.png");
	Image bugHungryLeftImage = new Image("bumblebeeHungryLeft.png");
	
	Image bugDeadRightImage = new Image("bumblebeeDead.png");
	Image bugDeadLeftImage = new Image("bumblebeeDeadLeft.png");
	
	public BumbleBee(double x, double y, double radius, BugWorldFX main) {
		
		super(x, y, radius, main);
		
		bugImage = new Image("bumblebee.png");
		this.setFill(new ImagePattern(bugImage));
		this.setStroke(null);
		
		bugHungryImage = new Image("bumblebeeHungry.png");
		bugDeadImage = new Image("bumblebeeDead.png");
		
		moveAmount = 3;
		
	}

	@Override
	//Override version accounts for directionality of the bumblebee
	protected void checkStatus() {
	
	// TODO Auto-generated method stub
		if (this.getRadius() < deathThreshold) {
			
			isDead = true;
			
			if(chosenHorizontal == 2) { 
				this.setFill(new ImagePattern(bugDeadRightImage));
			}
			else {
				
				this.setFill(new ImagePattern(bugDeadLeftImage));
			}
		}
		
		else if (this.getRadius() < hungerThreshold) {
			
			isHungry = true;
			
			if (chosenHorizontal == 2) {
				
				this.setFill(new ImagePattern(bugHungryRightImage));
			}
			else {
				
				this.setFill(new ImagePattern(bugHungryLeftImage));
			}
			
		}
		
		else if (this.getRadius() > hungerThreshold) {
			
			isHungry = false;
			
			if (chosenHorizontal == 2) {
				
				this.setFill(new ImagePattern(bugRightImage));
			}
			else {
				
				this.setFill(new ImagePattern(bugLeftImage));
			}
			
		}
		
	}


	
}
	
	



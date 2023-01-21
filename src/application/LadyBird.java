//png taken from: https://www.lmpsdorset.org/page/?title=Class+Pages&pid=46


package application;

/**
 * Represents a ladybird in the world
 * 
 * Ladybirds move slower than bees
 */
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class LadyBird extends Bug {

	public LadyBird(double x, double y, double radius, BugWorldFX main) {
		super(x, y, radius, main);
		
		bugImage = new Image("Ladybird.png");
		this.setFill(new ImagePattern(bugImage));
		this.setStroke(null);
		
		bugHungryImage = new Image("LadybirdHungry.png");
		bugDeadImage = new Image("LadybirdDead.png");
		
		moveAmount = 1.5f;
	}



}

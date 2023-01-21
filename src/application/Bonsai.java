package application;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

/**
 * Represents a Bonsai Tree with two possible states
 *
 */
public class Bonsai extends Plant {

	public Bonsai(double x, double y, double size, BugWorldFX main) {
		super(x, y, size, main);
		
		growingSprite = new Image("smallBonsai.png");
		grownSprite = new Image("bigBonsai.png");
		
		this.setFill(new ImagePattern(growingSprite));
	}

}

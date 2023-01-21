package application;

import javafx.scene.image.Image;

/**
 * Regular old plant
 *
 */
public class regularPlant extends Plant {

	public regularPlant(double x, double y, double size, BugWorldFX main) {
		super(x, y, size, main);
		
		growingSprite = new Image("smallPlant.png");
		grownSprite = new Image("grownPlant.png");
	}

}

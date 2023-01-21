package application;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

//References
/*
 * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/shape/Rectangle.html
 * 
 * https://www.clipartmax.com/middle/m2i8b1d3i8i8G6H7_growing-plant-png-clipart-plant-growing-cartoon/
 * https://pngtree.com/freepng/mbe-plant-potted-rose-four-leaf-clover-cactus_4047331.html
 * 
 * https://www.kindpng.com/imgv/iTboohb_bonsai-tree-pin-bonsai-tree-cartoon-transparent-hd/
 * https://www.pngitem.com/middle/iTbohJm_transparent-bonsai-tree-png-cartoon-bonsai-tree-png/
 * 
 */

/**
 * Represents a plant in the world
 * 
 *<p> Plants have status which describes their size. They can grow and they shrink in size when eaten.
 * 
 * If Eaten excessively they die.
 * 
 *
 */
public abstract class Plant extends Rectangle{
	
	BugWorldFX main;
	
	double currentSize;
	double maxSize = 45;
	double bigPlant = 35;
	
	boolean alive = true;
	
	double plantLeft = this.getX();
	double plantRight = this.getX() + this.getWidth();
	
	double plantTop = this.getY();
	double plantBottom = this.getY() + this.getHeight();
	
	Image growingSprite;
	Image grownSprite;
	
	public Plant(double x, double y, double size, BugWorldFX main) {
		
		super(x, y, size, size);
				
		currentSize = size;
		
		
		this.main = main;
	}
	
	/**
	 * When the size of the plant changes it needs to update the variables which describe its size
	 */
	public void updateDimensions(){
		
		plantLeft = this.getX();
		plantRight = this.getX() + this.getWidth();
		
		plantTop = this.getY();
		plantBottom = this.getY() + this.getHeight();
	}
	
	/**
	 * Calls appropriate behaviour for plant depending on its state
	 */
	public void behave() {
		
		if (alive) {
			grow();
		}
	}
	
	/**
	 * Causing plant to grow accordingly 
	 * 
	 */
	public void grow() {
		
		if (currentSize <= maxSize) {
			
			currentSize += 0.02;
			this.setWidth(currentSize);
			this.setHeight(currentSize);
			
			this.setX(this.getX() - 0.01);
			this.setY(this.getY() - 0.01);
			
			updateDimensions();
			
			checkStatus();
			
		}

		
	}
	
	/**
	 * Shrinks plant size to represent being eaten
	 * 
	 */
	public void gotEaten() {
		
		this.currentSize -= 15;
		
		this.setWidth(currentSize);
		this.setHeight(currentSize);
		
		this.setX(this.getX() + 7.5);
		this.setY(this.getY() + 7.5);
		
		updateDimensions();
		
		checkStatus();
		
	}
	
	
	/**
	 * Checks current state and updates its status including it's physical representation
	 * 
	 */
	private void checkStatus() {
		
		if (currentSize <= 0) {
			
			alive = false;
			return;
		}
		
		if (currentSize < 30) {
			
			this.setFill(new ImagePattern(growingSprite));
		}
		else if (currentSize >= bigPlant) {
			
			this.setFill(new ImagePattern(grownSprite));
		}
		else if (currentSize < bigPlant) {
			
			this.setFill(new ImagePattern(grownSprite));
		}
		

		
	}

}

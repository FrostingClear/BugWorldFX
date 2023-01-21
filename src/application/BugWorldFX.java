//References: https://stackoverflow.com/questions/53659555/how-to-change-keyframe-duration

//https://code.makery.ch/blog/javafx-dialogs-official/


//https://docs.oracle.com/javase/8/javafx/api/javafx/scene/input/MouseEvent.html


package application;
	
import java.awt.Frame;
import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;


public class BugWorldFX extends Application {
	
	protected int defaultSceneWidth = 1024;
	protected int defaultSceneHeight = 768;
	
	protected int topPaneHeight = 80; //Also sorts out the bottom pane height too
	
	protected double bugRadius = 25;
	protected ArrayList<Bug> bugList = new ArrayList<Bug>();
	
	protected int plantSize = 15;
	protected ArrayList<Plant> plantList = new ArrayList<Plant>();
	
	//The overall layout
	final protected BorderPane sceneRoot = new BorderPane();
	final protected Scene wholeScene = new Scene(sceneRoot, defaultSceneWidth, defaultSceneHeight);
	protected HBox topPane = initialiseTopPane();
	protected Pane bugArea = initialiseBugArea();
	protected HBox bottomPane = initialiseBotPane();
	
	protected KeyFrame worldFrame;
	protected double updateInterval = 16;
	
	Timeline theTimeline;

	
	//Bug area parameters
	protected double bugAreaXStart;
	protected double bugAreaXEnd;
	protected double bugAreaYStart;
	protected double bugAreaYEnd;
	
	
	protected double elapsedTime = 0;
	protected Text timeDisplay;
	protected Text bugsLeft;
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		buildScene();//Setup the basic scene;
		

		//Setup the timeline
		theTimeline = new Timeline();
		theTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
		theTimeline.getKeyFrames().add(worldFrame);
		theTimeline.play();
		
		//Give the stage a title, set the scene and show the stage
		primaryStage.setTitle("BugWorld FX");
		primaryStage.setScene(wholeScene);
		primaryStage.show();

	}
	
	/**
	 * Utility method. Random number generator
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static double numberInRange(double min, double max) {
		
		return Math.random()*(max - min + 1) + min;
	}
	
	
	/**
	 * Will populate the world with plants, choosing randomly which type of plant to add
	 * 
	 * Plants are placed in a grid pattern
	 * 
	 */
	protected void addPlants() {
	
		//Columns
		for (int i = 0; i <= 5; i++) {
			//Rows
			for (int j = 0; j <= 4; j++) {
				
				//We don't want plants placed on the top or left borders
				if (i != 0 && j != 0) {
				
					//Spacing between plants
					double xPos = 170 * i;
					double yPos = 120 * j;
					
					int plantChooser = Bug.numberInRange(0, 1); //re-using utility method in bug (which returns an integer)
					
					Plant plant = null;
					
					if (plantChooser == 0) {
						
						plant = new Bonsai(xPos, yPos, plantSize, this);
					}
					else {
						
						plant = new regularPlant(xPos, yPos, plantSize, this);
					}
					
					
					plantList.add(plant);
					bugArea.getChildren().add(plant);
				}
			}
			
		}
		
	}

	
	/**
	 * Will populate the world with bugs, choosing randomly which type of bug to add
	 * 
	 * bugs are also placed randomly within the visual area
	 * 
	 */
	protected void addBugs() {
		
		int bugsToCreate = 20;
		
		//Calculate the bounds of the bug area
		bugAreaXStart = bugRadius;
		bugAreaXEnd = wholeScene.getWidth() - bugRadius;
		
		bugAreaYStart = bugRadius;
		bugAreaYEnd = bugArea.getHeight() - bugRadius;
		
		
		for (int i = 0; i < bugsToCreate; i++) {
		
			//Must be within bounds
			double xPos = numberInRange(bugAreaXStart, bugAreaXEnd);
			double yPos = numberInRange(bugAreaYStart, bugAreaYEnd);
			
			//Called the bug's static utility method....that one returns an (int) - the one in this class returns a double.
			int speciesChooser = Bug.numberInRange(0, 1);
			
			Bug randomBug = null;

			
			if (speciesChooser == 0) {
				
				randomBug = new LadyBird(xPos, yPos, bugRadius, this);
			}
			else if (speciesChooser == 1) {
				
				randomBug = new BumbleBee(xPos, yPos, bugRadius, this);
			}
		
			bugArea.getChildren().add(randomBug);
			bugList.add(randomBug);
		}
		

	}

	/**
	 * Returns the number of bugs still alive
	 * 
	 * @return
	 */
	protected int bugsLeft() {
		
		int bugsLeft = 0;
		
		for (Bug bug : bugList) {
			
			if (!bug.isDead) {
				
				bugsLeft++;
			}
		}
		
		return bugsLeft;
	}

	
	/**
	 * When this method is called, makes it so the user can click on a hungry bug in the world and feed it.
	 * 
	 * Does not revive dead bugs, no affect on bugs that are not hungry
	 * 
	 */
	public void setFeeder() {
	
		EventHandler<MouseEvent> event = new EventHandler<MouseEvent> () {
			
	
		@Override
		public void handle(MouseEvent bug) {
			Bug theBug = (Bug) bug.getSource();
			
			if (!theBug.isDead && theBug.isHungry) {
				
				theBug.setRadius(theBug.maxEnergy); //Radius effectively represents the bug's energy
				
				if (theBug instanceof BumbleBee) {
					
					BumbleBee theBee = (BumbleBee) theBug;
					
					if (theBee.chosenHorizontal == 2) {
						theBee.setFill(new ImagePattern(theBee.bugRightImage));
					}
					else {
						
						theBee.setFill(new ImagePattern(theBee.bugLeftImage));
					}
					
				}
				else {
					
					theBug.setFill(new ImagePattern(theBug.bugImage));
				}
			}
			
		}};
		
	
		for (Bug bug : bugList) {
			
			bug.setOnMouseClicked(event);
		}
	}

	/**
	 * Returns a Pane that will represent where the bugs roam.
	 * 
	 */
	private Pane initialiseBugArea() {
		
		Pane bugArea = new Pane();
				
		return bugArea;
		
	}


	/**
	 * Sets up the layout of the scene
	 */
	private void buildScene() {
		
		initialiseTopPane();
		initialiseBugArea();
		this.setupWorldFrame();
		
		addButtons();
		
		sceneRoot.setTop(topPane);
		sceneRoot.setCenter(bugArea);
		sceneRoot.setBottom(bottomPane);
		
	}
	
	
	
	/**
	 * Returns a HBox with the chosen settings to represent the topPane
	 * 
	 */
	private HBox initialiseTopPane() {
		
		//TODO - Background Color Gradient Rather Than Flat Colours
		
		HBox topPane = new HBox();
		
		topPane.setStyle("-fx-background-color: #03a1fc");

		
		topPane.setSpacing(8);
		topPane.setAlignment(Pos.CENTER);
		topPane.setPrefHeight(topPaneHeight);
		
		
		return topPane;
	}
	
	private HBox initialiseBotPane() {
		
		HBox botPane = new HBox();
		
		//botPane.setAlignment();
		
		botPane.setStyle("-fx-background-color: #69bdd2");
		botPane.setPrefHeight(topPaneHeight);
		botPane.setPadding(new Insets(30, 70, 10, 70));
		botPane.setSpacing(150);
		

		
		return botPane;
	}
	
	private void setupWorldFrame() {
		
		worldFrame = new KeyFrame(Duration.millis(updateInterval), new worldUpdater(this));
	}

	//Sets up the start button
	private void addButtons() {
				
		Button startButton = new Button();
		startButton.setText("Initiate BugWorldFX");
		startButton.setPrefWidth(150);
		topPane.getChildren().add(startButton);
		
		startButton.setOnAction(new startButton(this));
		
	
	}

	public static void main(String[] args) {
		launch(args);
	}
	
		
	
	
	
}

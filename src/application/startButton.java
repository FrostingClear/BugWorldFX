package application;

import javafx.animation.KeyFrame;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Handles a lot of the heavy work once the world is initialised
 * 
 * Gets the bugs and plants into the world and sets up the rest of the user interface and
 * functional buttons
 * 
 *
 */
public class startButton implements EventHandler<ActionEvent> {

	protected BugWorldFX main;
	Button originalButton;
	final Button quitButton = new Button();
	final Button pauseButton = new Button();
	
	private int buttonWidth = 100;
	
	public startButton(BugWorldFX main) {

		this.main = main;
	}

	@Override
	public void handle(ActionEvent event) {
		
		originalButton = (Button) event.getSource();
		
		main.addBugs();
		main.addPlants();
		
		main.topPane.getChildren().remove(originalButton); //removes the "initialise bugworld" button. since world has already been initialised
		
		//Setup the pause button
		pauseButton.setText("Pause");
		pauseButton.setPrefWidth(buttonWidth);
		pauseButton.setOnAction(new pauseButton(main));
		
		main.topPane.getChildren().add(pauseButton);
		
		
		//Setup the restart button which resets the world and appropriate tracking metrics
		Button Restart = new Button();
		Restart.setText("Restart");
		Restart.setPrefWidth(buttonWidth);
		main.topPane.getChildren().add(Restart);
		
		Restart.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				
				main.bugList.clear();
				main.plantList.clear();
				main.bugArea.getChildren().clear();
				main.elapsedTime = 0;
				
				main.addBugs();
				main.addPlants();
				
				main.theTimeline.play();
				pauseButton.setText("Pause");

			}
			
			
			
		});
		
		//Setup the bugfeeder button which sets the event listener for all the bugs
		Button bugFeeder = new Button();
		bugFeeder.setText("Feed Bugs");
		bugFeeder.setPrefWidth(buttonWidth);
		main.topPane.getChildren().add(bugFeeder);
		
		bugFeeder.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				main.setFeeder();
				
				main.theTimeline.pause();
				
				Alert usageTip = new Alert(AlertType.INFORMATION);
				usageTip.setTitle("Instructions");
				usageTip.setHeaderText(null);
				usageTip.setContentText("Feed Hungry Bugs Yourself\n\n"
						+ "Hungry Bugs Have Duller Colour than the fed ones\n\n"
						+ "Try Slowing animation speed or pause if you have trouble\n\n"
						+ "Feed as many as you want(you can't revive dead ones though!)");

				usageTip.showAndWait();
				
				//If the world was previously running (ie. the pause/resume button says pause)
				//Then get it running again
				if (pauseButton.getText().equals("Pause")) {
					
					main.theTimeline.play();
				}
			}
			
			
		});
		
		//Button to quit the program
		quitButton.setText("Exit Program");
		quitButton.setPrefWidth(buttonWidth);
		main.topPane.getChildren().add(quitButton);
		quitButton.setOnAction(new quitButton(main));
		
		
		
		//Bottom Pane Nodes, contained within their own separate layout boxes
		//with preferred sizes, so the adjusting of one doesn't affect another
		
		//Will eventually display how many bugs are left alive etc.
		HBox lifeContainer = new HBox();
		lifeContainer.setPrefWidth(250);
		
		main.bugsLeft = new Text();
		lifeContainer.setPrefWidth(300);
		
		lifeContainer.getChildren().add(main.bugsLeft);
		main.bottomPane.getChildren().add(lifeContainer);
		
		
		//Slider Setup
		Slider slider = new Slider(1, 60, 32);
		slider.setShowTickMarks(true);

		
		VBox sliderBox = new VBox();
		sliderBox.setAlignment(Pos.CENTER);
		sliderBox.getChildren().add(new Text("(Slower) Animation Speed Slider (Faster)"));
		 
		slider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
				
				//Higher number means SLOW animation, we want the opposite, so calculate accordingly
				//Existing max is 60, so using 61 is appropriate, we don't want the slider to 
				//ever cause the animation to stop altogether
				main.updateInterval = 61 - (double) newValue;
				
				//Replace the keyframe with a new one which has the new timing value
				main.theTimeline.stop();
				main.theTimeline.getKeyFrames().clear();

				main.worldFrame = new KeyFrame(Duration.millis(main.updateInterval), new worldUpdater(main));
				main.theTimeline.getKeyFrames().add(main.worldFrame);
				main.theTimeline.play();
				
			}
			  
		 });
		
		sliderBox.getChildren().add(slider);
		
		main.bottomPane.getChildren().add(sliderBox);
		

		//Setup the node which displays elapsed time
		HBox timeContainer = new HBox();
		timeContainer.setPrefWidth(300);
		
		main.timeDisplay = new Text();
		timeContainer.getChildren().add(main.timeDisplay);
		main.bottomPane.getChildren().add(timeContainer);
		

		
		
		
	}

}

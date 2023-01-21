package application;

import java.util.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

public class worldUpdater implements EventHandler<ActionEvent> {
	
	private boolean allgone;

	private BugWorldFX main;
	
	public worldUpdater(BugWorldFX main) {

		this.main = main;
		
	}

	/**
	 * Calls the appropriate behaviour for all the bugs and plants in the world
	 * 
	 * Also tracks the amount of time elapsed in the world and updates those panes accordingly.
	 * 
	 * Also updates how many bugs are alive and if all dead reports how long they survived for in total
	 * 
	 */
	@Override
	public void handle(ActionEvent event) {
		
		ArrayList<Bug> bugList = main.bugList;
		
		//We only want to trigger the elapsed time code if we're actually updating the bugs
		//If we don't do this, this stopwatch counts as soon as the program starts up 
		boolean activated = false; 
		
		for (Bug bug : bugList) {
			
			bug.behave();
			activated = true; //We are now updating the bugs
			
		}
		
		ArrayList<Plant> plantList = main.plantList;
		
		for (Plant plant : plantList) {
			
			plant.behave();
		}
		
		//So now we can update the elapsed time accordingly
		if (activated) {
			
			main.elapsedTime += main.updateInterval;
			
			double elapsedTimeSec = main.elapsedTime / 1000;
			
			main.timeDisplay.setText("Real Time Elapsed: " + (int) elapsedTimeSec + " seconds");
			
			if (main.bugsLeft() > 0) {
				main.bugsLeft.setFill(Color.BLACK);

				main.bugsLeft.setText("Bugs still alive: " + main.bugsLeft());
			}
			else if (!allgone) {
				
				main.bugsLeft.setText("All Bugs Dead " + "(After " + (int) elapsedTimeSec + " seconds)");
				main.bugsLeft.setFill(Color.RED);
				
				
				allgone = true;
				
			}
			
			activated = false; //reset for next cycle
		}
		
	}
	

}

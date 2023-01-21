package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * Despite the name actually represents a pause and resume button in one functionality
 * 
 *
 */
public class pauseButton implements EventHandler<ActionEvent> {

	BugWorldFX main;
	
	Button originalButton;
	
	public pauseButton(BugWorldFX main) {

		this.main = main;
	}

	/**
	 * Either pauses or resumes the world depending on its current state
	 * 
	 * Button flips its name accordingly
	 * 
	 */
	@Override
	public void handle(ActionEvent event) {
		
		originalButton = (Button) event.getSource();
		
		if (originalButton.getText().equals("Pause")) {
		
			main.theTimeline.pause();
			originalButton.setText("Resume");
		}
		else if (originalButton.getText().equals("Resume")) {
			
			main.theTimeline.play();
			originalButton.setText("Pause");
		}
		
		
	}
	
	

}

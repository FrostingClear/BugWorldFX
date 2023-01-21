//https://code.makery.ch/blog/javafx-dialogs-official/

package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class quitButton implements EventHandler<ActionEvent>{

	BugWorldFX main;

	
	public quitButton(BugWorldFX main) {
		// TODO Auto-generated constructor stub
		this.main = main;

	}

	/**
	 * Calls an alert to thank the user when they press quit
	 */
	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Thank You Message");
		alert.setHeaderText(null);
		alert.setContentText("I Hope You Enjoyed BugWorldFX");

		alert.showAndWait();
		
		Platform.exit();
	}

}

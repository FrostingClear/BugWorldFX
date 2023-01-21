package application;

import javafx.animation.KeyFrame;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

/**
 * Create new keyframes with updated timing based on the worlds current update interval
 * 
 * 
 *
 */
public class FrameUpdater implements ChangeListener<Number> {

	BugWorldFX main;
	
	public FrameUpdater(BugWorldFX main) {

		this.main = main;
	}

	@Override
	public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {

		main.updateInterval = (double) newValue;
		
		main.worldFrame = new KeyFrame(Duration.millis(main.updateInterval), new worldUpdater(main));

		
	}



}

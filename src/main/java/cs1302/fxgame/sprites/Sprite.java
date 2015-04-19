package cs1302.fxgame.sprites;

import com.michaelcotterell.game.Game;
import com.michaelcotterell.game.GameTime;
import com.michaelcotterell.game.Updateable;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.Scene;

public abstract class Sprite extends Rectangle implements Updateable{
	public Sprite(double xCoordinate, double yCoordinate, int width, int height){
		super(xCoordinate, yCoordinate, width, height);
	}
	
	public abstract boolean getState();
}
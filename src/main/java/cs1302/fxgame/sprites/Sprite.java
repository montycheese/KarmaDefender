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
	public int rank;
	private boolean state;
	
	/**
	 * Constructs a new sprite object
	 * 
	 * @param double xCoordinate the starting x coordinate of the sprite object.
	 * @param double yCoordinate the starting y coordinate of the sprite object.
	 * @param int width the width of the object
	 * @param int height the height of the object
	 */
	public Sprite(double xCoordinate, double yCoordinate, int width, int height){
		super(xCoordinate, yCoordinate, width, height);
	}
	
	/**
	 * Returns a boolean of whether or not the sprite has a state of true or false.
	 * True represents its state on the board. False means it should be removed with the next update.
	 * 
	 * @return boolean representing the object's state.
	 */
	public abstract boolean getState();
}
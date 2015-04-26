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

public class Shield extends Sprite{
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	public int rank = 0;
	private Color color;
	private int health;
	private boolean state = true;
	
	/**
	 * Constructs a shield object that can withstand fire damage.
	 * 
	 * @param double xCoordinate the starting x coordinate of the cannon object.
	 * @param double yCoordinate the starting y coordinate of the cannon object.
	 * @param int width the width of the shield
	 * @param int height the height of the shield
	 */
	public Shield(double xCoordinate, double yCoordinate, int width, int height){
		super(xCoordinate, yCoordinate, width, height);
		this.color = Color.DARKGREEN;
		setFill(this.color);
		this.health = 10;
	}
	
	@Override
	public void update(Game game, GameTime gameTime){
		if(health == 9) color = Color.DARKOLIVEGREEN;
		else if(health == 8) color = Color.DARKOLIVEGREEN.brighter();
		else if(health == 7) color = Color.GREEN;
		else if(health == 6) color = Color.GREEN.brighter();
		else if(health == 5) color = Color.LIMEGREEN;
		else if(health == 4) color = Color.LAWNGREEN;
		else if(health == 3) color = Color.LIGHTGREEN;
		else if(health == 2) color = Color.PALEGREEN;
		else if(health == 1) color = Color.WHITE;
		setFill(color);
		if(health <= 0) this.state = false;
	}
	
	/**
	 * Decrements the shield's health value by one.
	 */
	public void decrementHealth(){
		this.health--;
	}
	
	@Override
	public boolean getState(){
		return this.state;
	}
}
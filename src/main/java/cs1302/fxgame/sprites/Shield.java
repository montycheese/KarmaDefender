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
	
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public int rank = 0;
	private int health;
	private boolean state = true;
	
	public Shield(double xCoordinate, double yCoordinate, int width, int height){
		super(xCoordinate, yCoordinate, width, height);
		this.health = 10;
	}
	
	@Override
	public void update(Game game, GameTime gameTime){
		if(health <= 0) this.state = false;
	}
	
	@Override
	public boolean getState(){
		return this.state;
	}
}
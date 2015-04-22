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

public class Cannon extends Sprite{
	
	public final int rank = 0;
	private int width = 50;
	private int height = 10;
	private int velocity = 4;
	private int xDir = 0;
	private int livesRemaining;
	private boolean state = true;
	
	public Cannon(double xCoordinate, double yCoordinate){
		super(xCoordinate, yCoordinate, 50, 10);
		setFill(Color.ANTIQUEWHITE);
		this.livesRemaining = 3;
	}
	
	@Override
	public void update(Game game, GameTime gameTime){
		int dx = 0;	
		if (game.getKeyManager().isKeyPressed(KeyCode.RIGHT)) dx += velocity;
        if (game.getKeyManager().isKeyPressed(KeyCode.LEFT))  dx -= velocity;
        if((this.getBoundsInParent().getMinX() + dx >= game.getSceneBounds().getMinX()) &&
           (this.getBoundsInParent().getMaxX() + dx <= game.getSceneBounds().getMaxX())
           )
        	this.setTranslateX(translateXProperty().add(dx).get());
		
	}
	
	public boolean getState(){
		return this.state;
	}
	
	public int getNumberOfLives(){
		return this.livesRemaining;
	}
	
	public void decNumberOfLives(){
		this.livesRemaining--;
	}
}
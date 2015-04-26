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

public class Fire extends Sprite{
	
	public static int totalShotsFired = 0;
	private int velocity = 3;
	private int yDir;
	private boolean state = true; // the state variable represents whether or not the bullet
								 // continues to exist uninterrupted. i.e has not collided with anything
								// nor has it gone off screen
	
	/**
	 * Constructs objects that represent both player and enemy fire. These are treated as vectors in
	 * 2-D space
	 * 
	 * @param xCoordinate the starting x coordinate of the fire object.
	 * @param yCoordinate the starting y coordinate of the fire object.
	 * @param yDir the y component of the direction vector.
	 */
	public Fire(double xCoordinate, double yCoordinate, int yDir){
		super(xCoordinate, yCoordinate, 10, 20);
		this.yDir = yDir;
		setFill(Color.LIGHTGREEN);
	}
	
	@Override
	public void update(Game game, GameTime gameTime){
		int dy = velocity * yDir;
	
		//add to check for intersections later
        if((this.getBoundsInParent().getMinY() <= game.getSceneBounds().getMaxY()) &&
           (this.getBoundsInParent().getMaxY() >= game.getSceneBounds().getMinY())
           )
        	this.setTranslateY(translateYProperty().add(dy).get());
        else{
        	this.state = false;
        }
		
	}
	@Override
	public boolean getState(){
		return this.state;
	}
	
}
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
	private int velocity = 4;
	private int yDir;
	private String name = "Fire";
	private boolean state = true; // the state variable represents whether or not the bullet
								 // continues to exist uninterrupted. i.e has not collided with anything
								// nor has it gone off screen
	
	public Fire(double xCoordinate, double yCoordinate, int yDir){
		super(xCoordinate, yCoordinate, 5, 20);
		this.yDir = yDir;
		setFill(Color.ANTIQUEWHITE);
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
	public boolean getState(){
		return this.state;
	}
	
	public String toString(){
		return this.name;
	}
	
}
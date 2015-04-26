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
import java.util.Random;
import java.lang.System;


public class MysteryShipSprite extends EnemySprite{
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 20;
	public int rank = 10;
	private int velocity = 1;
	private int xDir;
	private int distanceTraveled = 0;
	private boolean state = true;
	private int seconds = 0;
	
	/**
	 * Constructs the random mystery ship object.
	 * 
	 * @param double xCoordinate the starting x coordinate of the mystery ship object.
	 * @param double yCoordinate the starting y coordinate of the mystery ship object.
	 */
	public MysteryShipSprite(double xCoordinate, double yCoordinate){
		super(xCoordinate, yCoordinate);
		//super.setSize(WIDTH, HEIGHT);
		if(xCoordinate <= 0) this.xDir = 1;
		else this.xDir = -1;
		setFill(Color.BLACK);
	}
	
	@Override
	public void update(Game game, GameTime gameTime){
		int dx = velocity * xDir;
		setTranslateX(translateXProperty().add(dx).get());
		this.distanceTraveled += Math.abs(dx);
		if (this.distanceTraveled > game.getSceneBounds().getMaxX()) this.state = false;
	}
	
	@Override
	public boolean canFire(){
		return false;
	}
	@Override 
	public boolean isAtRoot(){
		return false;
	}
	
	@Override
	public int getRank(){
		return this.rank;
	}


	
}
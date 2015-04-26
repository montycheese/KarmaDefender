package cs1302.fxgame.sprites;

import com.michaelcotterell.game.Game;
import com.michaelcotterell.game.GameTime;
import com.michaelcotterell.game.util.TimeSpan;
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


public class EnemySprite extends Sprite{
	
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;
	public int rank = 1;
	private int velocity = 5;
	private int xDir = 1;
	//tracks how many pixels each enemy ship has moved
	private int distanceTraveled = 0;
	//tracks milliseconds since lastmovement, used to give a frame by frame movement style
	// to the sprite
	private double milliseconds = 0;
	private long timeLastShotFired;
	//tracks whether or not the ship is the bottommost in its respective column, allowing it to shoot
	private boolean isAtRoot = false; 
	private boolean state = true;
	
	/**
	 * Constructs an object of the enemy sprite object.
	 * 
	 * @param xCoordinate the starting x coordinate of the cannon object.
	 * @param yCoordinate the starting y coordinate of the cannon object.
	 */
	public EnemySprite(double xCoordinate, double yCoordinate){
		super(xCoordinate, yCoordinate, 20, 20);
		setFill(Color.ANTIQUEWHITE);
		this.timeLastShotFired = System.nanoTime();
	}
	
	@Override
	public void update(Game game, GameTime gameTime){
		if(this.milliseconds == 0 ) this.milliseconds = gameTime.getTotalGameTime().getTotalMilliseconds();
		//System.out.println("Seconds elapsed " + gameTime.getTotalGameTime().getTotalMilliseconds());
		if(gameTime.getTotalGameTime().getTotalMilliseconds() - this.milliseconds > 500){
			int dx = velocity * xDir;
			if(this.distanceTraveled >= Math.round(game.getSceneBounds().getWidth()/4)){
				float dy = velocity * 1.5f;
				changeXDirection();
				setTranslateY(translateYProperty().add(velocity).get());
				this.distanceTraveled = 0;
			}
			else{
				setTranslateX(translateXProperty().add(dx).get());
				this.distanceTraveled += Math.abs(dx);
			}
			this.milliseconds = gameTime.getTotalGameTime().getTotalMilliseconds();
		}
	}
	@Override
	public boolean getState(){
		return this.state;
	}
	
	/**
	 * Sets the magnitude of the velocity of the sprite
	 * 
	 * @param velocity the magnitude of the velocity to set the sprite at.
	 */
	public void setVelocity(int velocity){
		this.velocity = velocity;
	}
	
	/**
	 * returns the rank of the enemy sprite
	 * 
	 * @return int rank of the enemy sprite
	 */
	public int getRank(){
		return this.rank;
	}

	/**
	 * Changes the x direction of the sprite's movement vector.
	 */
	public void changeXDirection(){
		this.xDir *= -1;
	}
	
	/**
	 * sets a sprite as the root ship
	 */
	public void setAtRoot(){
		this.isAtRoot = true;
	}
	
	/**
	 * Returns a boolean value of whether or not the sprite is the root of its column.
	 * 
	 * @return boolean of whether or not the sprite is the root.
	 */
	public boolean isAtRoot(){
		return this.isAtRoot;
	}
	
	/**
	 * Returns whether or not the sprite is able to fire. i.e. whether it is root and whether or not
	 * it satisfies a pseudo random number
	 * 
	 * @return boolean whether the sprite can fire or not.
	 */
	public boolean canFire(){
		Random rand = new Random();
		int r = rand.nextInt(500);
		if(r == 0 && 
				(System.nanoTime() - this.timeLastShotFired > 2000000000L)
			) 
		{
				if(this.isAtRoot) this.timeLastShotFired = System.nanoTime(); //guaranteed to shoot if both conditions are met
				return true;
		}
		return false;
	}
	
}
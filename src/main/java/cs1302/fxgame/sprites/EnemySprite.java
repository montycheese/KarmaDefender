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


public class EnemySprite extends Sprite{
	
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;
	public int rank = 1;
	private int velocity = 1;
	private int xDir = 1;
	private int distanceTraveled = 0;
	private long timeLastShotFired;
	//tracks whether or not the ship is the bottommost in its respective column, allowing it to shoot
	private boolean isAtRoot = false; 
	private boolean state = true;
	
	public EnemySprite(double xCoordinate, double yCoordinate){
		super(xCoordinate, yCoordinate, 20, 20);
		setFill(Color.ANTIQUEWHITE);
		this.timeLastShotFired = System.nanoTime();
	}
	
	@Override
	public void update(Game game, GameTime gameTime){
		int dx = velocity * xDir;
		if(this.distanceTraveled >= Math.round(game.getSceneBounds().getWidth()/4)){
			int dy = velocity;
			changeXDirection();
			setTranslateY(translateYProperty().add(velocity).get());
			this.distanceTraveled = 0;
		}
		else{
			setTranslateX(translateXProperty().add(dx).get());
			this.distanceTraveled += Math.abs(dx);
		}
	}
	@Override
	public boolean getState(){
		return this.state;
	}

	public void changeXDirection(){
		this.xDir *= -1;
	}
	public void setAtRoot(){
		this.isAtRoot = true;
	}
	public boolean isAtRoot(){
		return this.isAtRoot;
	}
	public boolean canFire(){
		Random rand = new Random();
		int r = rand.nextInt(100);
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
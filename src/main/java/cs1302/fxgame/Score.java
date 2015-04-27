package cs1302.fxgame;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import cs1302.fxgame.sprites.EnemySprite;

public class Score extends Text{
	
	private int score = 0;
	
	/**
	 * Constructs the score object used to display the user's current score
	 * 
	 * @param xCoordinate the starting x coordinate of the score object.
	 * @param yCoordinate the starting y coordinate of the score object.
	 */
	public Score(double xCoordinate, double yCoordinate){
		super(xCoordinate, yCoordinate, "Karma: 0" );
		setFont(new Font("Helvetica", 24));
		setFill(Color.GREEN);
	}
	
	/**
	 * Increases the score of the player based on the sprite they destroy
	 * 10 points for regular, 100 for mystery ships
	 * 
	 * @param enemy the enemysprite object destroyed
	 */
	public void increaseScore(EnemySprite enemy){
		this.score += this.calculateScore(enemy);
		setText("Karma: " + Integer.toString(score));
	}
	
	/**
	 * Resets the user score to 0.
	 */
	public void resetScore(){
		this.score = 0;
		setText("Karma: 0");
	}
	
	/**
	 * returns an int representing the score gained depending on the enemy destroyed.
	 * 
	 * @param enemy the enemysprite object destroyed
	 * @return int of the score to be incremented
	 */
	private int calculateScore(EnemySprite enemy){
		return enemy.getRank() * 10;
	}
	
}
package cs1302.fxgame;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import cs1302.fxgame.sprites.EnemySprite;

public class Score extends Text{
	
	private int score = 0;
	
	public Score(double xCoordinate, double yCoordinate){
		super(xCoordinate, yCoordinate, "Score: 0" );
		setFont(new Font("Helvetica", 24));
		setFill(Color.GREEN);
	}
	
	public void increaseScore(EnemySprite enemy){
		this.score += this.calculateScore(enemy);
		setText("Score: " + Integer.toString(score));
	}
	
	public void resetScore(){
		this.score = 0;
		setText("Score: 0");
	}
	
	private int calculateScore(EnemySprite enemy){
		return enemy.getRank() * 10;
	}
	
	/*public int calculateFinalScore(int numLives){
		return this.score * numLives;
	}*/
}
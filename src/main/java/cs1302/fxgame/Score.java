package cs1302.fxgame;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import cs1302.fxgame.sprites.Sprite;

public class Score extends Text{
	
	private int score = 0;
	
	public Score(double xCoordinate, double yCoordinate){
		super(xCoordinate, yCoordinate, "Score: 0" );
		setFont(new Font("Helvetica", 24));
		setFill(Color.GREEN);
	}
	
	public void increaseScore(Sprite sprite){
		this.score += this.calculateScore(sprite);
		setText("Score: " + Integer.toString(score));
	}
	
	public void resetScore(){
		this.score = 0;
		setText("Score: 0");
	}
	
	private int calculateScore(Sprite sprite){
		//if (sprite.rank == 1) 
			return 100;
		//.. add more later
	}
}
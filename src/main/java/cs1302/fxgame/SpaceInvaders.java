package cs1302.fxgame;

import com.michaelcotterell.game.Game;
import com.michaelcotterell.game.GameTime;
import com.michaelcotterell.game.Updateable;
import cs1302.fxgame.sprites.EnemySprite;
import cs1302.fxgame.sprites.Sprite;
import cs1302.fxgame.sprites.Cannon;
import cs1302.fxgame.sprites.Fire;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.lang.System;


public class SpaceInvaders extends Game{
	
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	/*private ArrayList<Fire> shotsFired = new ArrayList<Fire>();*/
	private long timeLastShotFired;

	// rectangle to hold the background
    private Rectangle bg = new Rectangle(0, 0, 640, 480) {{ 
         setFill(Color.BLACK); 
    }};
    
    private Cannon cannon = new Cannon(320, 455);
    private Score score = new Score(35, 25);
    
    public SpaceInvaders(Stage stage){
    	super(stage, "Space Invaders", 60, 640, 480);
        getSceneNodes().getChildren().addAll(bg, cannon, score);
    	sprites.add(cannon);
    	addAllEnemySprites();
    	timeLastShotFired = System.nanoTime();
    }
    
    @Override
    public void update(Game game, GameTime gameTime){
    	ArrayList<Sprite> temp = new ArrayList<Sprite>();
    	for(Sprite sprite: sprites){
    		sprite.update(game, gameTime);
    		if (!sprite.getState()) temp.add(sprite);
    	}
    
    	if (game.getKeyManager().isKeyPressed(KeyCode.SPACE) && 
    		// can only fire every 2 seconds
    		System.nanoTime() - this.timeLastShotFired > 1000000000L
    		)
    	{
    		this.timeLastShotFired = System.nanoTime();
    		Sprite fire = new Fire(cannon.getBoundsInParent().getMaxX()-(cannon.getWidth()/2), 
								cannon.getBoundsInParent().getMinY(),
								-1);
    		Fire.totalShotsFired++; // can remove later
			addSprite(fire);
    	}
    	/*
    	if (!shotsFired.isEmpty()){
    		for(Fire fire: shotsFired){
    			for(Sprite sprite: sprites){
    				if (fire.getBoundsInParent().intersects(sprite.getBoundsInParent())){
    					// make some sort of animation
    					temp.add(sprite);
    					temp.add(fire);
    				}
    			}
    		}
    	}*/
    	//Using a temp array to store which sprites to remove to avoid a concurrent
    	//modification exception
    	for(Sprite sprite: temp) removeSprite(sprite);
    }
    private void removeSprite(Sprite sprite){
    	System.out.println("removing:" + sprite.toString());
    	sprites.remove(sprite);
    	this.getSceneNodes().getChildren().remove(sprite);
    }
    public void addSprite(Sprite sprite){ 
    	this.getSceneNodes().getChildren().add(sprite);
    	sprites.add(sprite);
    }
    public void addBulletSprite(Fire fire){
    	this.shotsFired.add(fire);
    }
    
    private void addAllEnemySprites(){
    	EnemySprite[] listOfNodes = new EnemySprite[11];
    	int xCoordinate = 50; int yCoordinate = 50;
    	for (int i = 0; i < 5; i++){
    		for(int j = 0; j < 11; j++){
    			EnemySprite e = new EnemySprite(xCoordinate, yCoordinate);
    			if(i==4) e.setAtRoot();
    			if(i!=0) e.setLocationOfNextSprite(listOfNodes[j]);
    			listOfNodes[j] = e;
    			addSprite(e);
    			xCoordinate += e.getWidth() + 15;
    		}
    		yCoordinate += listOfNodes[listOfNodes.length-1].getHeight() + 10;
    		xCoordinate = 50;
    	}
    }
 
}
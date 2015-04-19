package cs1302.fxgame;

import com.michaelcotterell.game.Game;
import com.michaelcotterell.game.GameTime;
import com.michaelcotterell.game.Updateable;
import com.michaelcotterell.game.util.TimeSpan;
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
	private long timeLastShotFired;

	// rectangle to hold the background
    private Rectangle bg = new Rectangle(0, 0, 640, 480) {{ 
         setFill(Color.BLACK); 
    }};
    
    private Cannon cannon = new Cannon(320, 455);
    
    public SpaceInvaders(Stage stage){
    	super(stage, "Space Invaders", 60, 640, 480);
        getSceneNodes().getChildren().addAll(bg, cannon);
    	sprites.add(cannon);
    	timeLastShotFired = System.nanoTime();
    }
    
    @Override
    public void update(Game game, GameTime gameTime){
    	ArrayList<Sprite> temp = new ArrayList<Sprite>();
    	for(Sprite sprite: sprites){
    		sprite.update(game, gameTime);
    		if (!sprite.getState()) temp.add(sprite);
    	}
    	//Using a temp array to store which sprites to remove to avoid a concurrent
    	//modification exception
    	for(Sprite sprite: temp) removeSprite(sprite);
    
    	if (game.getKeyManager().isKeyPressed(KeyCode.SPACE) && 
    		System.nanoTime() - this.timeLastShotFired > 2000000000L
    		)
    	{
    		//System.out.println("seconds past" + (System.nanoTime() - this.timeLastShotFired));
    		this.timeLastShotFired = System.nanoTime();
    		Sprite fire = new Fire(cannon.getBoundsInParent().getMaxX()-(cannon.getWidth()/2), 
								cannon.getBoundsInParent().getMinY(),
								-1);
    		Fire.totalShotsFired++; // can remove later
    	
			addSprite(fire);
    	}
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
 
}
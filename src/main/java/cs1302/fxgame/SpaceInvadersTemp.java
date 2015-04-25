package cs1302.fxgame;

import com.michaelcotterell.game.Game;
import com.michaelcotterell.game.GameTime;
import com.michaelcotterell.game.Updateable;
import cs1302.fxgame.sprites.EnemySprite;
import cs1302.fxgame.sprites.Sprite;
import cs1302.fxgame.sprites.Cannon;
import cs1302.fxgame.sprites.Fire;
import cs1302.fxgame.sprites.Shield;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Iterator;
import java.lang.System;


public class SpaceInvadersTemp extends Game{

    private ArrayList<EnemySprite>[] enemySprites = new ArrayList[11];
    private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    private ArrayList<Fire> shotsFired = new ArrayList<Fire>();
    private ArrayList<Shield> shields = new ArrayList<Shield>();
    
    private long timeLastShotFired;
    //private boolean godMode = true;

    // rectangle to hold the background
    private Rectangle bg = new Rectangle(0, 0, 640, 480) {{ 
         setFill(Color.BLACK); 
    }};
    
    private Cannon cannon = new Cannon(320, 455);
    private Score score = new Score(35, 25);
    private Text lives = new Text() {{
        setTranslateX(560);
        setTranslateY(20);
        setFill(Color.ANTIQUEWHITE);
        setText("Lives ");
   }};
    
    public SpaceInvadersTemp(Stage stage){
    	super(stage, "Space Invaders", 60, 640, 480);
        getSceneNodes().getChildren().addAll(bg, cannon, score, lives);
    	sprites.add(cannon);
    	addAllEnemySprites();
    	addAllShields();
    	timeLastShotFired = System.nanoTime();
    }
    
    @Override
    public void update(Game game, GameTime gameTime){
    	
    	if(!shields.isEmpty()){
    		update(shields, game, gameTime);
    	}
    	if(!sprites.isEmpty()){
    		update(sprites, game, gameTime);
    	}
    	setRootShooters(); // make only the bottom most sprite in each row shoot
    	for(ArrayList<EnemySprite> enemyLists: enemySprites){
    		if (enemyLists != null){
    			for(Iterator<EnemySprite> it = enemyLists.iterator(); it.hasNext();){
    				EnemySprite enemy = it.next();
    				if(enemy.isAtRoot() && enemy.canFire()){
    					addSprite(new Fire(enemy.getBoundsInParent().getMaxX()-(EnemySprite.WIDTH/2), 
								enemy.getBoundsInParent().getMaxY(),
								1), shotsFired);
    				}
    				enemy.update(game, gameTime);
    			}
    		}
    	}
    	
    	if (!shotsFired.isEmpty()){
    		update(shotsFired, game, gameTime);
    	}
    	if (game.getKeyManager().isKeyPressed(KeyCode.SPACE) && 
    		// can only fire every 2 seconds
    		System.nanoTime() - this.timeLastShotFired > 1000000000L
    		)
    	{
    		this.timeLastShotFired = System.nanoTime();
    		addSprite(new Fire(cannon.getBoundsInParent().getMaxX()-(cannon.getWidth()/2), 
								cannon.getBoundsInParent().getMinY()-40,
				   -1), shotsFired);
    		Fire.totalShotsFired++; // can remove later
    	}
    	
    	if (!shotsFired.isEmpty()){
    		for(Iterator<Fire> itFire = shotsFired.iterator(); itFire.hasNext();){
    			Fire fire = itFire.next();
    			for(Iterator<Sprite> itSprite = sprites.iterator(); itSprite.hasNext();){
    				Sprite sprite = itSprite.next();
    				if (fire.getBoundsInParent().intersects(sprite.getBoundsInParent())){
    					// make some sort of animation
    					itSprite.remove();
    					itFire.remove();
    					this.getSceneNodes().getChildren().removeAll(fire, sprite);
    				}
				
    			}
    			for(ArrayList<EnemySprite> enemyLists: enemySprites){
    				if(enemyLists!=null){
    					for(Iterator<EnemySprite> itEnemy = enemyLists.iterator(); itEnemy.hasNext();){
    						EnemySprite enemy = itEnemy.next();
    						if(fire.getBoundsInParent().intersects(enemy.getBoundsInParent())){
    							itEnemy.remove();
    							itFire.remove();
    							this.getSceneNodes().getChildren().removeAll(fire, enemy);
    							//add method to decrease # of enemies left
    						}
    					}
    				}
    			}		
    			for(Iterator<Shield> itShield = shields.iterator(); itShield.hasNext();){
    				Shield shield = itShield.next();
    				if (fire.getBoundsInParent().intersects(shield.getBoundsInParent())){
    					// make some sort of animation
    					shield.decrementHealth();
    					itFire.remove();
    					this.getSceneNodes().getChildren().remove(fire);
    				}
    			}
    		}
    	}
    }
    
    private <T extends Sprite> void update(ArrayList<T> list, Game game, GameTime gameTime){
    	for(Iterator<T> it = list.iterator(); it.hasNext();){
			T t = it.next();
			t.update(game, gameTime);
			if (!t.getState()){
				it.remove();
				getSceneNodes().getChildren().remove(t);
			}
		}
    }
    
    private void setRootShooters(){
    	for(ArrayList<EnemySprite> enemyLists: enemySprites){
    		if(enemyLists == null) continue;
    		else if(enemyLists.isEmpty()){
    			enemyLists = null;
    		}
    		else{
    			enemyLists.get(enemyLists.size()-1).setAtRoot();
    		}
    	}
    }
    
    private <T extends Sprite> void removeSprite(T sprite, ArrayList<T> list){
	System.out.println("removing: " + sprite.toString());
	list.remove(sprite);
	this.getSceneNodes().getChildren().remove(sprite);
    }

    private <T extends Sprite> void addSprite(T sprite, ArrayList<T> list){
	this.getSceneNodes().getChildren().add(sprite);
	list.add(sprite);
    }
    
    private void addAllEnemySprites(){
    	int xCoordinate = 50; int yCoordinate = 50;
    	for(int i = 0; i < 11; i++){
    		ArrayList<EnemySprite> enemies = new ArrayList<>();
    		for(int j = 0; j < 5; j++){
    			EnemySprite e = new EnemySprite(xCoordinate, yCoordinate);
    			if(j == 4) e.setAtRoot(); // the enemy is at the bottom and can shoot
    			addSprite(e, enemies);
    			yCoordinate += 50; 
    		}
    		enemySprites[i] = enemies;
    		xCoordinate += EnemySprite.WIDTH + 15;
    		yCoordinate = 50; // can change later
    	}
    }
    
    private void addAllShields(){
    	int xCoordinate = 50; int yCoordinate = 360;
    	for(int i = 0; i < 6; i++){
    		addSprite(new Shield(xCoordinate, yCoordinate, Shield.WIDTH, Shield.HEIGHT), shields);
    		xCoordinate += Shield.WIDTH * 2;
    	}
    }
}
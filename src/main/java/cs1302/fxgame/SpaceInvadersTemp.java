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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.lang.System;


public class SpaceInvadersTemp extends Game{

    private ArrayList<EnemySprite>[] enemySprites = new ArrayList[11];
    private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    private ArrayList<Fire> shotsFired = new ArrayList<Fire>();
    private ArrayList<Shield> shields = new ArrayList<Shield>();
    private long timeLastShotFired;
    private boolean godMode = true;

    // rectangle to hold the background
    private Rectangle bg = new Rectangle(0, 0, 640, 480) {{ 
         setFill(Color.BLACK); 
    }};
    
    private Cannon cannon = new Cannon(320, 455);
    private Score score = new Score(35, 25);
    
    public SpaceInvadersTemp(Stage stage){
    	super(stage, "Space Invaders", 60, 640, 480);
        getSceneNodes().getChildren().addAll(bg, cannon, score);
    	sprites.add(cannon);
    	addAllEnemySprites();
    	addAllShields();
    	timeLastShotFired = System.nanoTime();
    }
    
    @Override
    public void update(Game game, GameTime gameTime){
	
    	ArrayList<Sprite> temp = new ArrayList<Sprite>();
    	ArrayList<Fire> tempFire = new ArrayList<Fire>();
    	ArrayList<EnemySprite> tempEnemy = new ArrayList<EnemySprite>();
    	
    	
    	for(Sprite sprite: sprites){
    		sprite.update(game, gameTime);
    		if (!sprite.getState()) temp.add(sprite);
    	}
    	setRootShooters();
    	for(ArrayList<EnemySprite> enemyLists: enemySprites){
    		if (enemyLists != null){
    			for(EnemySprite enemy: enemyLists){
    				if(enemy.isAtRoot() && enemy.canFire()){
    					addSprite(new Fire(enemy.getBoundsInParent().getMaxX()-(EnemySprite.WIDTH/2), 
								enemy.getBoundsInParent().getMaxY(),
								1), shotsFired);
    				}
    				enemy.update(game, gameTime);
    			}
    		}
    	}
    	for(Fire fire: shotsFired){
    		fire.update(game, gameTime);
    		if(!fire.getState()) tempFire.add(fire);
    	}
    
    	if (game.getKeyManager().isKeyPressed(KeyCode.SPACE) && 
    		// can only fire every 2 seconds
    		System.nanoTime() - this.timeLastShotFired > 1000000000L
    		)
    	{
    		this.timeLastShotFired = System.nanoTime();
    		addSprite(new Fire(cannon.getBoundsInParent().getMaxX()-(cannon.getWidth()/2), 
								cannon.getBoundsInParent().getMinY()-50,
				   -1), shotsFired);
    		Fire.totalShotsFired++; // can remove later
    	}
    	
    	if (!shotsFired.isEmpty()){
    		for(Fire fire: shotsFired){
    			for(Sprite sprite: sprites){
    				if (fire.getBoundsInParent().intersects(sprite.getBoundsInParent())){
    					// make some sort of animation
    					temp.add(sprite);
    					tempFire.add(fire);
    				}
				
    			}
    			for(ArrayList<EnemySprite> enemyLists: enemySprites){
    				if(enemyLists!=null){
    					for(EnemySprite enemy: enemyLists){
    						if(fire.getBoundsInParent().intersects(enemy.getBoundsInParent())){
    							tempEnemy.add(enemy);
    							tempFire.add(fire);
    							//add method to decrease # of enemies left
    						}
    					}
    				}
    			}		      
    		}
    	}
    	//Using a temp array to store which sprites to remove to avoid a concurrent
    	//modification exception
    	if(!godMode){
    		for(Sprite sprite: temp) 
    			removeSprite(sprite, sprites);
    	}
    	for(Fire fire: tempFire){
    		removeSprite(fire, shotsFired);
    	}
    	for(EnemySprite enemy: tempEnemy){
    		for(ArrayList<EnemySprite> enemyLists: enemySprites){
    			if(enemyLists!=null && enemyLists.contains(enemy)){
    				removeSprite(enemy, enemyLists);
    				if(enemyLists.isEmpty()){
    					enemyLists = null;
    				}
    			}
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
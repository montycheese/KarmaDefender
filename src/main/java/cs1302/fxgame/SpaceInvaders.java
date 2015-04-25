package cs1302.fxgame;

import com.michaelcotterell.game.Game;
import com.michaelcotterell.game.GameTime;
import com.michaelcotterell.game.Updateable;
import cs1302.fxgame.sprites.EnemySprite;
import cs1302.fxgame.sprites.MysteryShipSprite;
import cs1302.fxgame.sprites.Sprite;
import cs1302.fxgame.sprites.Cannon;
import cs1302.fxgame.sprites.Fire;
import cs1302.fxgame.sprites.Shield;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.media.AudioClip;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Random;
import java.lang.System;



public class SpaceInvaders extends Game{

    private ArrayList<EnemySprite>[] enemySprites = new ArrayList[11];
    private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    private ArrayList<Fire> shotsFired = new ArrayList<Fire>();
    private ArrayList<Shield> shields = new ArrayList<Shield>();
    
    private int numEnemySprites = 55;
    private int level = 1;
    private long timeLastShotFired;
    private final long SHOT_DELAY = 1_000_000_000L;
    private double secondsSinceLastMysteryShip = 0;
    private final String MYSTERY_SPRITE_PATH = "/resources/MysteryShipSprite.png";
    private final String ENEMY_SPRITE_PATH = "/resources/EnemySprite.png";
    private final String BG_MUSIC_PATH = "/resources/BG_Music.mp3";
    private final String LASER_SOUND_PATH = "/resources/laser.aiff";
    
    // rectangle to hold the background
    private Rectangle bg = new Rectangle(0, 0, 640, 480) {{ 
         setFill(Color.BLACK); 
    }};
    
    private Cannon cannon = new Cannon(320, 455);
    private Score score = new Score(35, 25);
    private Text lives = new Text() {{
        setTranslateX(520);
        setTranslateY(25);
        setFill(Color.ANTIQUEWHITE);
        setFont(new Font("Helvetica", 24));
        setText("Lives: 3");
   }};
    
    public SpaceInvaders(Stage stage){
    	super(stage, "Space Invaders", 60, 640, 480);
        getSceneNodes().getChildren().addAll(bg, cannon, score, lives);
    	addAllEnemySprites();
    	addAllShields();
    	timeLastShotFired = System.nanoTime();
    	AudioClip bgMusic = new AudioClip(getClass().getResource(BG_MUSIC_PATH).toString());
    	bgMusic.play();
    	bgMusic.setCycleCount(AudioClip.INDEFINITE);
    }
    
    @Override
    public void update(Game game, GameTime gameTime){
    	cannon.update(game, gameTime);
    	if(!shields.isEmpty()){
    		update(shields, game, gameTime);
    	}
    	if(!sprites.isEmpty()){
    		update(sprites, game, gameTime);
    	}
    	
    	if(gameTime.getTotalGameTime().getTotalSeconds() - this.secondsSinceLastMysteryShip > 15){
    		Image img = new Image(MYSTERY_SPRITE_PATH);
    		Random r = new Random();
    		double xCoordinate = (r.nextInt(2) == 0) ? game.getSceneBounds().getMinX() : game.getSceneBounds().getMaxX();
    		MysteryShipSprite mysteryShip = new MysteryShipSprite(xCoordinate, 30);
    		mysteryShip.setFill(new ImagePattern(img, 0,0,1,1,true));
    		addSprite(mysteryShip, enemySprites[enemySprites.length-1]);
    		this.secondsSinceLastMysteryShip = gameTime.getTotalGameTime().getTotalSeconds();
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
    					AudioClip laserSound = new AudioClip(getClass().getResource(LASER_SOUND_PATH).toString());
    		    		laserSound.play();
    				}
    				if(
    					this.numEnemySprites == 50 ||
    					this.numEnemySprites == 45 || 
    					this.numEnemySprites == 40 || 
    					this.numEnemySprites == 35 || 
    					this.numEnemySprites ==	30 || 
    					this.numEnemySprites ==	25 ||				
    					this.numEnemySprites ==	20 || 
    					this.numEnemySprites ==	15 || 
    					this.numEnemySprites ==	10 || 
    					this.numEnemySprites == 5
    					){
    					enemy.setVelocity(15 - (numEnemySprites/5));
    				}
    				else if (this.numEnemySprites <= 0) proceedToNextLevel();
    				enemy.update(game, gameTime);
    			}
    		}
    	}
    	
    	if (!shotsFired.isEmpty()){
    		update(shotsFired, game, gameTime);
    	}
    	if (game.getKeyManager().isKeyPressed(KeyCode.SPACE) && 
    		// can only fire every 2 seconds
    		System.nanoTime() - this.timeLastShotFired > SHOT_DELAY
    		)
    	{
    		this.timeLastShotFired = System.nanoTime();
    		addSprite(new Fire(cannon.getBoundsInParent().getMaxX()-(cannon.getWidth()/2), 
								cannon.getBoundsInParent().getMinY()-40,
				   -1), shotsFired);
    		Fire.totalShotsFired++; // can remove later
    		AudioClip laserSound = new AudioClip(getClass().getResource(LASER_SOUND_PATH).toString());
    		laserSound.play();
    	}
    	
    	if (!shotsFired.isEmpty()){
    		for(Iterator<Fire> itFire = shotsFired.iterator(); itFire.hasNext();){
    			Fire fire = itFire.next();
    			if (fire.getBoundsInParent().intersects(cannon.getBoundsInParent())){
    				itFire.remove();
    				cannon.decrementNumberOfLives();
    				lives.setText("Lives: " + Integer.toString(cannon.getNumberOfLives()));
    				if(cannon.getNumberOfLives() <= 0){
    					//game over
    					getSceneNodes().getChildren().remove(cannon);
    					endGame();
    				}
    				getSceneNodes().getChildren().remove(fire);
    				
    			}
    			for(ArrayList<EnemySprite> enemyLists: enemySprites){
    				if(enemyLists!=null){
    					for(Iterator<EnemySprite> itEnemy = enemyLists.iterator(); itEnemy.hasNext();){
    						EnemySprite enemy = itEnemy.next();
    						if(fire.getBoundsInParent().intersects(enemy.getBoundsInParent())){
    							score.increaseScore(enemy);
    							itEnemy.remove();
    							itFire.remove();
    							this.getSceneNodes().getChildren().removeAll(fire, enemy);
    							if(this.numEnemySprites > 0 && enemy.getRank() == 1){
    								this.numEnemySprites--;
    							}
    							else if (this.numEnemySprites <= 0){
    								//make next level restore enemies and shields
    							}
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
    
    //if user dies
    private void endGame(){
    	stop();
    	score.setFont(new Font("Helvetica", 72));
    	score.setTranslateX(160);
    	score.setTranslateY(200);
    	if (getKeyManager().isKeyPressed(KeyCode.DOWN))
    		run();
    	
    }
    
    private void proceedToNextLevel(){
    	addAllEnemySprites();
    	this.numEnemySprites = 55;
    	this.level++;
    	
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
    
    private <T extends Sprite> void addSprite(T sprite, ArrayList<T> list){
	this.getSceneNodes().getChildren().add(sprite);
	list.add(sprite);
    }
    
    private void addAllEnemySprites(){
    	int xCoordinate = 50; int yCoordinate = 50;
    	Image img = new Image(ENEMY_SPRITE_PATH);
    	for(int i = 0; i < enemySprites.length; i++){
    		ArrayList<EnemySprite> enemies = new ArrayList<>();
    		for(int j = 0; j < 5; j++){
    			EnemySprite e = new EnemySprite(xCoordinate, yCoordinate);
    			e.setFill(new ImagePattern(img, 0,0,1,1,true)); // setting the image for enemy sprite
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
    	int xCoordinate = 90; int yCoordinate = 380;
    	for(int i = 0; i < 8; i++){
    		addSprite(new Shield(xCoordinate, yCoordinate, Shield.WIDTH, Shield.HEIGHT), shields);
    		xCoordinate += Shield.WIDTH * 2;
    	}
    }
}
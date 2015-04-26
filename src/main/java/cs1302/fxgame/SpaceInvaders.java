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

	//Declaring lists of specific game objects
    private ArrayList<EnemySprite>[] enemySprites = new ArrayList[11]; //spaceships and mystery ships
    private ArrayList<Sprite> sprites = new ArrayList<Sprite>(); //user's sprite, fire objects
    private ArrayList<Fire> shotsFired = new ArrayList<Fire>(); //bullets
    private ArrayList<Shield> shields = new ArrayList<Shield>(); //bunkers
    
    private int numEnemySprites = 55;
    private int level = 1;
    private long timeLastShotFired;
    private final long SHOT_DELAY = 1_000_000_000L; //Shot delay for the user's cannon
    private double secondsSinceLastMysteryShip = 0; 
    //Declaring relative file paths
    private final String MYSTERY_SPRITE_PATH = "/resources/MysteryShipSprite.png";
    private final String ENEMY_SPRITE_PATH = "/resources/EnemySprite.png";
    private final String BG_MUSIC_PATH = "/resources/BG_Music.mp3";
    private final String LASER_SOUND_PATH = "/resources/laser.aiff";
    private final String UPVOTE_LASER_PATH = "/resources/upvotes.png";
    private final String DOWNVOTE_LASER_PATH = "/resources/downvotes.png";
    private final String CANNON_SPRITE_PATH = "/resources/Grumpy_Cat.png";
    
    // rectangle to hold the background
    private Rectangle bg = new Rectangle(0, 0, 640, 490) {{ 
         setFill(Color.BLACK); 
    }};
    
    // declaring nodes
    private Cannon cannon = new Cannon(320, 460);
    private Score score = new Score(35, 25);
    private Text lives = new Text() {{
        setTranslateX(520);
        setTranslateY(25);
        setFill(Color.ANTIQUEWHITE);
        setFont(new Font("Helvetica", 24));
        setText("Lives: 3");
   }};
    
   /**
    * Constructs the SpaceInvaders game object that creates the scene in which the game is
    * displayed and handeled.
    * 
    *  @param stage the stage object that holds the game
    */
    public SpaceInvaders(Stage stage){
    	super(stage, "Internet Defender", 60, 640, 480);
    	//setting the cannon's image
    	Image img = new Image(CANNON_SPRITE_PATH);
    	cannon.setFill(new ImagePattern(img, 0, 0, 1, 1, true));
        getSceneNodes().getChildren().addAll(bg, cannon, score, lives);
    	addAllEnemySprites(); 
    	addAllShields();
    	timeLastShotFired = System.nanoTime();
    	//loop soundtrack
    	AudioClip bgMusic = new AudioClip(getClass().getResource(BG_MUSIC_PATH).toString());
    	bgMusic.play();
    	bgMusic.setCycleCount(AudioClip.INDEFINITE);
    }
    
    @Override
    public void update(Game game, GameTime gameTime){
    	//updating every sprite in game
    	cannon.update(game, gameTime);
    	if(!shields.isEmpty()){
    		update(shields, game, gameTime);
    	}
    	if(!sprites.isEmpty()){
    		update(sprites, game, gameTime);
    	}
    	
    	//determine when mysteryships appear
    	if(gameTime.getTotalGameTime().getTotalSeconds() - this.secondsSinceLastMysteryShip > 15){
    		Image img = new Image(MYSTERY_SPRITE_PATH);
    		Random r = new Random();
    		//Psuedorandomly determine whether the ship comes from right or left
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
    				//determine whether an enemy sprite shoots or not
    				if(enemy.isAtRoot() && enemy.canFire()){
    					//if can shoot, create new fire object and set an image to it
    					Image img = new Image(DOWNVOTE_LASER_PATH);
    					Fire fire = new Fire(enemy.getBoundsInParent().getMaxX()-(EnemySprite.WIDTH/2), 
								enemy.getBoundsInParent().getMaxY(),
								1);
    					fire.setFill(new ImagePattern(img, 0, 0, 1, 1, true));
    					addSprite(fire, shotsFired);
    					//create funny laser sound when a shot is fired
    					AudioClip laserSound = new AudioClip(getClass().getResource(LASER_SOUND_PATH).toString());
    		    		laserSound.play();
    				}
    				//determine when to increase speed of enemy sprites
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
    				//when user has eliminated all enemy sprites
    				else if (this.numEnemySprites <= 0) proceedToNextLevel();
    				if(enemy.getBoundsInParent().getMaxY() >= 380){
    					destroyAllShields();
    				}
    				//if the enemy sprites reach the bottom, the game ends.
    				if(enemy.getBoundsInParent().getMaxY() >=game.getSceneBounds().getMaxY()){
    					endGame();
    				}
    				enemy.update(game, gameTime);
    			}
    		}
    	}
    	
    	//updats all fire objects in real time if there are any in the scene.
    	if (!shotsFired.isEmpty()){
    		update(shotsFired, game, gameTime);
    	}
    	if (game.getKeyManager().isKeyPressed(KeyCode.SPACE) && 
    		// can only fire every 2 seconds
    		System.nanoTime() - this.timeLastShotFired > SHOT_DELAY
    		)
    	{
    		this.timeLastShotFired = System.nanoTime();
    		Image img = new Image(UPVOTE_LASER_PATH);
			Fire fire = new Fire(cannon.getBoundsInParent().getMaxX()-(cannon.getWidth()/2), 
					cannon.getBoundsInParent().getMinY()-40, -1);
			fire.setFill(new ImagePattern(img, 0, 0, 1, 1, true));
			addSprite(fire, shotsFired);
    		AudioClip laserSound = new AudioClip(getClass().getResource(LASER_SOUND_PATH).toString());
    		laserSound.play();
    	}
    	
    	//Determine collisions
    	if (!shotsFired.isEmpty()){
    		//iterate through all fire objects if there are any
    		for(Iterator<Fire> itFire = shotsFired.iterator(); itFire.hasNext();){
    			Fire fire = itFire.next();
    			// if it intersects with the user's cannon
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
    						// if it intersects with any enemy ships
    						if(fire.getBoundsInParent().intersects(enemy.getBoundsInParent())){
    							score.increaseScore(enemy);
    							itEnemy.remove();
    							itFire.remove();
    							this.getSceneNodes().getChildren().removeAll(fire, enemy);
    							//decrement variable holding num of enemy sprites left 
    							if(this.numEnemySprites > 0 && enemy.getRank() == 1){
    								this.numEnemySprites--;
    							}
    						}
    					}
    				}
    			}		
    			//determine collisions of fire objects and shield bunkers.
    			for(Iterator<Shield> itShield = shields.iterator(); itShield.hasNext();){
    				Shield shield = itShield.next();
    				if (fire.getBoundsInParent().intersects(shield.getBoundsInParent())){
    					shield.decrementHealth();
    					itFire.remove();
    					this.getSceneNodes().getChildren().remove(fire);
    				}
    			}
    		}
    	}
    }
    
    /**
     * Called in the update method to end the game when either the player has no lives
     * remaining or the aliens reach the bottom of the map.
     */
    private void endGame(){
    	//stop the game loop
    	stop();
    	//enlarge the score and bring it to the center of frame.
    	score.setFont(new Font("Helvetica", 72));
    	score.setTranslateX(160);
    	score.setTranslateY(200);
    }
    
    /**
     * Called in the update method when the user clears a level, it reinitializes the spaceship
     * sprites and increments the level. 
     */
    private void proceedToNextLevel(){
    	addAllEnemySprites();
    	this.numEnemySprites = 55;
    	this.level++;
    	
    }
    
    /**
     * Updates specific sprites and removes them from the scene and their containing list if
     * they are destroyed or moved off screen.
     * 
     * @param list the list containing the sprites to be updated
     * @param game the game object
     * @param gameTime the gametime object representing time information about each respective game.
     */
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
    
    /**
     * Sets certain enemy sprites as designated shooters, i.e. the ones at the bottom of each 
     * respective column of sprites.
     */
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
    
    /**
     * Adds sprites to their respectie lists
     * 
     * @param sprite a sprite object
     * @param list a list containing sprite objects of type T
     */
    private <T extends Sprite> void addSprite(T sprite, ArrayList<T> list){
	this.getSceneNodes().getChildren().add(sprite);
	list.add(sprite);
    }
    
    /**
     * Initalizes the game grid with enemy sprites
     */
    private void addAllEnemySprites(){
    	int xCoordinate = 50; int yCoordinate = 50;
    	Image img = new Image(ENEMY_SPRITE_PATH); //set enemysprites to image of reddit alien
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
    		yCoordinate = 50; 
    	}
    }
    
    /**
     * Destroys all the shields on the map. Called if the enemy sprites reach a certain Y value.
     */
    private void destroyAllShields(){
    	getSceneNodes().getChildren().removeAll(shields);
    	shields.clear();
    }
    
    /**
     * Initalizes the game map with shields at the beginning of each game.
     */
    private void addAllShields(){
    	int xCoordinate = 90; int yCoordinate = 380;
    	for(int i = 0; i < 8; i++){
    		addSprite(new Shield(xCoordinate, yCoordinate, Shield.WIDTH, Shield.HEIGHT), shields);
    		xCoordinate += Shield.WIDTH * 2;
    	}
    }
}
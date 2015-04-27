package cs1302.fxgame;

import com.michaelcotterell.game.Game;

import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Driver extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception { 

    	Game game = new SpaceInvaders(primaryStage);
    	
        primaryStage.setTitle(game.getTitle());
        primaryStage.setScene(game.getScene());
        primaryStage.show();
        game.run();   
    } // start
    
    public static void main(String[] args) {
        launch(args);
    } // main

} // Driver


/*
 //test
    	Group g = new Group();
    	Scene s = new Scene(g, 300, 300, Color.BLACK);
    	Rectangle r = new Rectangle(25, 25, 250, 250);
    	r.setFill(Color.BLUE);
    	g.getChildren().add(r);
    	g.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
    			if (e.getCode() == KeyCode.ENTER){
    				primaryStage.setTitle(game.getTitle());
    		        primaryStage.setScene(game.getScene());
    		        primaryStage.show();
    		        System.out.println("pressed enter");
    		        game.run();     
    		}
    	});
    	primaryStage.setTitle("test");
    	primaryStage.setScene(s);
    	primaryStage.show();
    	//test
    	//if key pressed start game
 */


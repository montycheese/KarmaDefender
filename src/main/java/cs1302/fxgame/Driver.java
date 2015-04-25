package cs1302.fxgame;

import com.michaelcotterell.game.Game;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Driver extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception { 
    	//Game intro = new Intro(primaryStage);
    	Game game = new SpaceInvaders(primaryStage);
        //primaryStage.setTitle(game.getTitle());
        //primaryStage.setScene(game.getScene());
        //primaryStage.show();
    	//primaryStage.setTitle(intro.getTitle());
        //primaryStage.setScene(intro.getScene());
        //primaryStage.show();
        //intro.run();
        primaryStage.setTitle(game.getTitle());
        primaryStage.setScene(game.getScene());
        primaryStage.show();
        game.run();
        
    } // start
    
    public static void main(String[] args) {
        launch(args);
    } // main

} // Driver


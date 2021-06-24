package solver;

import java.util.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Display extends Application {
	private static GridPane gridPane;
	private static Text tscore;
	public static Model model;
	AbstractMove move;
	public static Backup back = new Backup();

	/**
	 * Launches the application.
	 * @param pArgs This program takes no argument.
	 */
	public static void main(String[] pArgs) 
	{
        launch(pArgs);
    }

	@Override
	public void start(Stage pStage) throws Exception
	{
		model = new Model();
		pStage.setTitle("2048");
		

        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        
        
        
        tscore = new Text();
        tscore.setText("Score: 0");
        
        updateTiles();
        
        VBox box = new VBox();
        box.getChildren().add(tscore);
        box.getChildren().add(gridPane);
        
       

        //gridPane.setTileAlignment(Pos.CENTER);
        Scene scene = new Scene(box, 650, 650);
        scene.setFill(Color.grayRgb(220));
        
        
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DOWN) {
                System.out.println("DOWN");
                move = new MoveDown(model);
                move.perform();
            } else if (e.getCode() == KeyCode.UP) {
                System.out.println("UP");
                move = new MoveUp(model);
                move.perform();
            } else if (e.getCode() == KeyCode.RIGHT) {
                System.out.println("RIGHT");
                move = new MoveRight(model);
                move.perform();
            } else if (e.getCode() == KeyCode.LEFT) {
                System.out.println("LEFT");
                move = new MoveLeft(model);
                move.perform();
            } else if (e.getCode() == KeyCode.R) {
                System.out.println("R");
                reset();
            } else if (e.getCode() == KeyCode.Z) {
                System.out.println("Z");
                Model m = back.undo();
                System.out.println(m.toString());
                if (m != null) {
                	model = m;
                	updateTiles();
                }
            } else if (e.getCode() == KeyCode.X) {
                System.out.println("X");
                Model m = back.redo();
                if (m != null) {
                	model = m;
                	updateTiles();
                }
            }
        });
        
        pStage.setScene(scene);
        pStage.setResizable(false);
        pStage.show();
    }
	
	public static StackPane createTile(int value) {
		StackPane tile = new StackPane();
	
		Text text = new Text();
		Rectangle rectangle;
		
		if (value == 0) {		
			rectangle = new Rectangle(0, 0, 150, 150);
			rectangle.setFill(Color.WHITE);
		} else {
			text.setText(Integer.toString(value));
			text.setX(75); 
			text.setY(75);
			text.setStyle("-fx-font: 24 arial;");
			
			rectangle = new Rectangle(0, 0, 150, 150);
			rectangle.setFill(Model.colorMap.get(value));
		}			
		
		tile.getChildren().add(rectangle);
		tile.getChildren().add(text);
		tile.setPadding(new Insets(1));
		
		return tile;
	}
	
	public static void updateTiles() {
		Tile[][] tiles = model.getTiles();
		gridPane.getChildren().clear();
		for (int i = 0 ;  i<4 ; i++) {
        	for (int j = 0 ;  j<4 ; j++) {
        		if (tiles[i][j] != null) {
        			tiles[i][j].unMerged();
        			gridPane.add(tiles[i][j].getPane(), i, j, 1, 1);
        		} else {
        			gridPane.add(createTile(0), i, j, 1, 1);
        		}
        	}
        }
		tscore.setText("Score: " + Integer.toString(model.score));
	}
	
	public static void reset() {
		model = new Model();
		back.reset();
		updateTiles();
	}
	
	public static void wait(int ms)
	{
	    try
	    {
	        Thread.sleep(ms);
	    }
	    catch(InterruptedException ex)
	    {
	        Thread.currentThread().interrupt();
	    }
	}
	
	
}

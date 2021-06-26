package solver;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Display extends Application {
	private static GridPane gridPane;
	private static Text tscore;
	private static StackPane stack;
	public static Model model;
	AbstractMove move;
	public static Backup back = new Backup();
	TranslateTransition tt = new TranslateTransition();

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
        HBox hbox = new HBox();
        Text help = new Text();
        help.setText("\nUse the arrows to move the tiles\nPress R to Restart\nPress Z to undo\nPress X to redo\nPress A to use the autoplayer");
        help.setFocusTraversable(false);
        
        
        hbox.getChildren().add(tscore);
        hbox.getChildren().add(help);
        box.getChildren().add(hbox);
        
        stack = new StackPane();
        GridPane background = new GridPane();
        background.setAlignment(Pos.CENTER);
        
        for (int i = 0 ;  i<4 ; i++) {
        	for (int j = 0 ;  j<4 ; j++) {
        		background.add(createTile(-1), i, j, 1, 1);
        	}
        }
        stack.getChildren().add(background);
        stack.getChildren().add(gridPane);
        
        box.getChildren().add(stack);
        Scene scene = new Scene(box, 650, 750);
        scene.setFill(Color.grayRgb(220));
        
        
        scene.setOnKeyPressed(e -> {
        	tt = new TranslateTransition();
        	tt.setDuration(Duration.millis(50));
    		tt.setDelay(Duration.millis(20));
            if (e.getCode() == KeyCode.DOWN) {
                down();
            } else if (e.getCode() == KeyCode.UP) {
                up();
            } else if (e.getCode() == KeyCode.RIGHT) {
            	right();
            } else if (e.getCode() == KeyCode.LEFT) {
            	left();
            } else if (e.getCode() == KeyCode.R) {
                System.out.println("R");
                reset();
            } else if (e.getCode() == KeyCode.Z) {
                System.out.println("Z");
                Model m = back.undo();
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
            } else if (e.getCode() == KeyCode.A) {
            	System.out.println("A");
            	Autoplayer player = new Autoplayer();
            	int moveNumber = player.findBestMove(model);
            	
            	if (moveNumber == 1) {
            		up();
            	} else if (moveNumber == 2) {
            		down();
            	} else if (moveNumber == 3) {
            		right();
            	} else if (moveNumber == 4) {
            		left();
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
			rectangle.setFill(Color.TRANSPARENT);
		} else if (value == -1) {
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
        			gridPane.add(tiles[i][j].getPane(), j, i, 1, 1);
        		} else {
        			gridPane.add(createTile(0), j, i, 1, 1);
        		}
        	}
        }
		tscore.setText("Score: " + Integer.toString(model.score));
	}
	
	public void down() {
		System.out.println("DOWN");
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(new Double[]{
            250.0, 0.0,
            350.0, 0.0,
            300.0, 150.0 });
        triangle.setFill(Color.rgb(30, 30, 30, 0.1));
        stack.getChildren().add(triangle);
        tt.setToY(800);
        tt.setNode(triangle);
        tt.play();
        move = new MoveDown(model);
        move.perform();
        model.generateNewTile();
        updateTiles();
	}
	
	public void up() {
		System.out.println("UP");    
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(new Double[]{
            250.0, 600.0,
            350.0, 600.0,
            300.0, 450.0 });
        triangle.setFill(Color.rgb(30, 30, 30, 0.1));
        stack.getChildren().add(triangle);
        tt.setToY(-800);
        tt.setNode(triangle);
        tt.play();
        move = new MoveUp(model);
        move.perform();
        model.generateNewTile();
        updateTiles();
	}
	
	public void right() {
		System.out.println("RIGHT");
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(new Double[]{
            0.0, 250.0,
            0.0, 350.0,
            150.0, 300.0 });
        triangle.setFill(Color.rgb(30, 30, 30, 0.1));
        stack.getChildren().add(triangle);
        tt.setToX(800);
        tt.setNode(triangle);
        tt.play();
        move = new MoveRight(model);
        move.perform();
        model.generateNewTile();
        updateTiles();
	}
	
	public void left() {
		System.out.println("LEFT");
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(new Double[]{
            600.0, 250.0,
            600.0, 350.0,
            450.0, 300.0 });
        triangle.setFill(Color.rgb(30, 30, 30, 0.1));
        stack.getChildren().add(triangle);
        tt.setToX(-800);
        tt.setNode(triangle);
        tt.play();
        move = new MoveLeft(model);
        move.perform();
        model.generateNewTile();
        updateTiles();
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

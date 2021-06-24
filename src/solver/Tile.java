package solver;

import java.util.Random;

import javafx.scene.layout.StackPane;

public class Tile implements Cloneable {
	private int value;
	private StackPane pane;
	private boolean merged;

	public Tile() {
		Random rand = new Random();
        int v2 = rand.nextInt(10);
        
        if (v2 < 8) {
        	value = 2;
        } else {
        	value = 4;
        }
        
        pane = Display.createTile(value);
        merged = false;
		
	}
	
	public int getValue() {
		return value;
	}
	
	public StackPane getPane() {
		return pane;
	}
	
	public boolean isMerged() {
		return merged;
	}
	
	public void setMerged() {
		merged = true;
	}
	
	public void unMerged() {
		merged = false;
	}
	
	public static int mergeTiles(Tile t1, Tile t2) {
		if (t2 == null) {
			return 0;
		} else if (t1.value != t2.value) {
			return 0;
		} else {
		
			t1.value += t2.value;
			t1.pane = Display.createTile(t1.value);
			return t2.value;
		}
	}
	
	public Tile clone() {
		Tile t = new Tile();
		t.value = this.value;
		t.pane = Display.createTile(t.value);
		return t;
	}
	
	public String toString() {
		return Integer.toString(value);
	}

}

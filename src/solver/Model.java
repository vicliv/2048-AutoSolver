package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.util.Pair;

public class Model implements Cloneable {
	private Tile[][] tiles;
	public final static Map<Integer, Color> colorMap = new HashMap<Integer, Color>();
	public int score;
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public Model() {
		score = 0;
		colorMap.put(2, Color.GREY);
		colorMap.put(4, Color.TURQUOISE);
		colorMap.put(8, Color.BROWN);
		colorMap.put(16, Color.MAGENTA);
		colorMap.put(32, Color.YELLOW);
		colorMap.put(64, Color.CYAN);
		colorMap.put(128, Color.GREEN);
		colorMap.put(256, Color.ORANGE);
		colorMap.put(512, Color.LIME);
		colorMap.put(1024, Color.NAVY);
		colorMap.put(2048, Color.PINK);
		colorMap.put(4096, Color.RED);
		
		Random rand = new Random();
		tiles =  new Tile[4][4]; 
		
		int x1 = rand.nextInt(4);
		int y1 = rand.nextInt(4);
		int x2 = rand.nextInt(4);
		int y2 = rand.nextInt(4);
		
		while (x1==x2 && y1==y2) {
			x2 = rand.nextInt(4);
			y2 = rand.nextInt(4);
		}
		
		tiles[x1][y1] = new Tile();
		tiles[x2][y2] = new Tile();		
	}
	
	public void generateNewTile() {
		ArrayList<Pair<Integer,Integer>> l =  new ArrayList<Pair<Integer,Integer>>();
		for (int i = 0 ; i<4; i++) {
			for (int j = 0; j<4; j++) {
				if (tiles[i][j] == null) {
					l.add(new Pair<>(i, j));
				}
			}
		}
		Random rand = new Random();
		int index = rand.nextInt(l.size());
		Pair<Integer,Integer> p = l.get(index);
		tiles[p.getKey()][p.getValue()] = new Tile();
	}
	
	public Model clone() {
		
		try {
			Model clone = (Model) super.clone();
		
			for (int i = 0; i<4 ; i++) {
				for (int j = 0; j<4; j++) {
					if (tiles[i][j] != null) {
						clone.tiles[i][j] = tiles[i][j].clone();
					} else {
						clone.tiles[i][j] = null;
					}
				}
			}
			clone.score = score;
			return clone;
		} catch (CloneNotSupportedException e) {
			assert false;
			return null;
		}
	}
	
	public String toString() {
		return Arrays.deepToString(tiles);
	}
	
}

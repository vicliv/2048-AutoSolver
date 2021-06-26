package solver;

import java.util.ArrayList;

public class Benchmark {
	private static final int iterations = 20;

	public static void main(String[] args) {		
		for (int a = 1; a < 20 ; a++) {
			for (int b = 1; b < 20 ; b++) {
				for (int c = 1; c < 20 ; c++) {
					for (int d = 1; d < 20 ; d++) {
						for (int e = 1; e < 20 ; e++) {
							benchmark(a,b,c,d,e);
							System.gc();
						}
					}
				}
			}
		}


	}

	public static void benchmark(int empty, int merge, int corner, int struct, int bad_tile) {
		ArrayList<Integer> scores = new ArrayList<>();
		
		for (int i = 0; i<iterations ; i++) {
			Model model = new Model();
			AbstractMove m = new MoveUp(model);
			Autoplayer player = new Autoplayer(empty, merge, corner, struct, bad_tile);		
			
			while(true) {
				int move = player.nextMove(model);
			
				if (move == 1) {
					m = new MoveUp(model);
				} else if (move == 2) {
					m = new MoveDown(model);
				} else if (move == 3) {
					m = new MoveRight(model);
				} else {
					m = new MoveLeft(model);
				}
			
				m.perform();
				if (!model.generateNewTile()) {
					Model copy = model.clone();
					AbstractMove m1 = new MoveUp(copy);
					AbstractMove m2 = new MoveDown(copy);
					AbstractMove m3 = new MoveRight(copy);
					AbstractMove m4 = new MoveLeft(copy);
					if (!m1.perform() && !m2.perform() && !m3.perform() && !m4.perform()) {
						break;
					}
					copy = null;
				}		
			}
			scores.add(model.score);
			model = null;
			
		}
		
		int max = 0;
		int tot = 0;
		
		for (int i = 0; i < scores.size(); i++) {
			int v = scores.get(i);
			
			if (v>max) {
				max = v;
			}
			
			tot += v;
		}
		
		tot = tot/scores.size();
		System.out.println("Average: " + Integer.toString(tot));
		System.out.println("Max: " + Integer.toString(max));
		scores.clear();
		
	}

}

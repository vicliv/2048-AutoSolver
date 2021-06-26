package solver;

public class Player {
	private final int CORNER = 10;
	private final int EMPTY = 1;

	public Player() {
	}
	
	public int nextMove(Model model) {
		int score = 0;
		int move = 0;
		for (int m = 0; m< 4; m++) {
			int s = 0;
			AbstractMove mov = getMove(m, model);
			if (mov.perform()) {
				s = getScore(model, 0, 1);
			} else {
				s = 0;
			}
			if (s >= score) {
				score = s;
				move = m;
			}
		}
		
		return move;
	}
	
	public int getScore(Model model, int rec, int max) {
		
		if (rec >= max) {
			return score(model);
		}
		int total = 0;
	
		for (int i = 0; i<4 ; i++) {
			for (int j=0; j<4;j++) {
				Tile[][] tiles = model.getTiles();
				if (tiles[i][j] == null) {	
					Model copy1 = model.clone();
					copy1.getTiles()[i][j] = new Tile(2);
					total += moveScore(copy1, rec, max)*0.9;
					
					Model copy2 = model.clone();
					copy2.getTiles()[i][j] = new Tile(2);
					total += moveScore(copy2, rec, max)*0.1;
				}
			}
		}
		
		
		return total;
	}
	
	public int moveScore(Model model, int rec, int max) {
		long startTime = System.nanoTime();
		int score = 0;
		for (int m = 0; m<4; m++) {
			int s = 0;
			AbstractMove move = getMove(m, model);
			if (move.perform()) {
				s = getScore(model, rec+1, max);
				if (s >= score) {
					score = s;
				}
			}
		}
		long endTime = System.nanoTime();
		System.out.println(((double) (endTime - startTime))/1000000000.0);
		return score;
	}
	
	public int score(Model model) {
		
		int total = 0;
		Tile max = new Tile(2);
		int x = -1;
		int y = -1;
		for (int i = 0; i<4 ; i++) {
			for (int j=0; j<4;j++) {
				Tile[][] tiles = model.getTiles();
				if (tiles[i][j] == null) {
					total += EMPTY;
				} else if (tiles[i][j].getValue() > max.getValue()) {
					max = tiles[i][j];
					x = i;
					y = j;
				}
			}
		}
		if ((x == 3 || x== 0) && (y == 3 || y == 0)) {
			total += CORNER;
		}
		
		return total;
	}
	
	public AbstractMove getMove(int n, Model model) {
		assert n<=4;
		assert n>1;
		
		if (n==1) {
			return new MoveUp(model);
		} else if (n==2) {
			return new MoveDown(model);
		} else if (n==3) {
			return new MoveRight(model);
		} else {
			return new MoveLeft(model);
		}
		
	}

}

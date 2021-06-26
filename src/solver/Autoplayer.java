package solver;

import java.util.*;

public class Autoplayer {
	private final int ratio = 0;
	private int VOID = 200;
	private int MERGE = 80;
	private int CORNER = 300;
	private int STRUCT = 10;
	private int BAD_TILE = 30;

	public Autoplayer() {
	}
	
	public Autoplayer(int empty, int merge, int corner, int struct, int bad_tile) {
		VOID = empty;
		MERGE = merge;
		CORNER = corner;
		STRUCT = struct;
		BAD_TILE = bad_tile;
	}
	
	public int nextMove(Model model) {
		int score = 0;
		int move = 0;
		int pscore = model.score;
		int n = 5;
		if (pscore <1000) {
			n = 4;
		}
		for (int m = 2; m < n; m++) {
			Model clone = model.clone();
			int s = 0;
			AbstractMove mov = getMove(m, clone);
			if (mov.perform()) {
				int v1;
				int v2;
				if (clone.getTiles()[0][3] != null) {
					v1 = 100*clone.getTiles()[0][3].getValue();
				} else {
					v1 = 0;
				}
				if (clone.getTiles()[3][3] != null) {
					v2 = 100*clone.getTiles()[3][3].getValue();
				} else {
					v2 = 0;
				}
				s = findScore(clone, 0, 2, pscore) + (clone.score-pscore)*ratio + v1+v2;
			} else {
				s = 0;
			}
			if (s >= score) {
				score = s;
				move = m;
			}
			clone = null;
		}
		if (pscore <1000 && score==0) {
			return 4;
		}
		if (score==0) return 1;
		return move;
	}
	
	public int findScore(Model model, int rec, int max, int pscore) {
		if (rec== max) {
			return score2(model)*10 + score(model);
		}
		model.generateNewTile();
		int score = 0;
		for (int m = 2; m < 5; m++) {
			Model clone = model.clone();
			int s = 0;
			AbstractMove mov = getMove(m, clone);
			if (mov.perform()) {
				s = findScore(clone, rec+1, max, pscore) + (clone.score-pscore)*ratio;
			} else {
				s = 0;
			}
			if (s >= score) {
				score = s;
			}
			clone = null;
		}
		return score;
	}
	
	public int findBestMove2(Model model) {
		AbstractMove move;
		Model clone = model.clone();
		int pscore = model.score;
		
		int up;
		move = new MoveUp(clone);
		if (move.perform()) {
			up = score(clone) + (clone.score-pscore)*ratio;
		} else {
			up = 0;
		}
		
		clone = model.clone();
		
		int down;
		move = new MoveDown(clone);
		if (move.perform()) {
			down = score(clone) + (clone.score-pscore)*ratio+2;
		} else {
			down = 0;
		}
		
		clone = model.clone();
		
		int right;
		move = new MoveRight(clone);
		if (move.perform()) {
			right = score(clone) + (clone.score-pscore)*ratio+2;
		} else {
			right = 0;
		}
		
		clone = model.clone();
		
		int left;
		move = new MoveLeft(clone);
		if (move.perform()) {
			left = score(clone) + (clone.score-pscore)*ratio;
		} else {
			left = 0;
		}
		
		up = 1;
		
		return max(down, right, left, up);
	}
	
	public int findBestMove(Model model) {
		AbstractMove move;
		Model clone = model.clone();
		int pscore = model.score;
		
		int up = 1;
		
		int down;
		move = new MoveDown(clone);
		if (move.perform()) {
			down = score2(clone) + (clone.score-pscore)*ratio;
		} else {
			down = 0;
		}
		
		clone = model.clone();
		
		int right;
		move = new MoveRight(clone);
		if (move.perform()) {
			right = score2(clone) + (clone.score-pscore)*ratio;
		} else {
			right = 0;
		}
		
		clone = model.clone();
		
		int left;
		move = new MoveLeft(clone);
		if (move.perform()) {
			left = score2(clone) + (clone.score-pscore)*ratio;
		} else {
			left = 0;
		}
		
		return max(down, right, left, up);
	}
	
	private int score(Model model) {
		int total = 0;
		Tile[][] tiles = model.getTiles();
		PriorityQueue<Pair<Integer, Pair<Integer, Integer>>> highestTiles = new PriorityQueue<>();
		PriorityQueue<Pair<Integer, Pair<Integer, Integer>>> copy = new PriorityQueue<>(highestTiles);
		
		for (int i = 0 ; i<4; i++) {
			for (int j=0; j<4; j++) {
				if (tiles[i][j] != null) {
					
					highestTiles.add(new Pair<Integer, Pair<Integer, Integer>>(tiles[i][j].getValue(), new Pair<Integer, Integer>(i, j)));
				}
			}
		}
		Pair<Integer, Pair<Integer,Integer>> last = highestTiles.poll();
		Pair<Integer, Pair<Integer,Integer>> prev = last;
		ArrayList<Pair<Integer, Pair<Integer,Integer>>> previous =  new ArrayList<>();
		
		if ((last.second.first == 0 || last.second.first == 3) && (last.second.second == 0 || last.second.second == 3)) {
			total += 100*last.first;
		} else if ((last.second.first == 0 || last.second.first == 3) || (last.second.second == 0 || last.second.second == 3)) {
			total += 50;
		}
		previous.add(last);
		
		for (int i = 0; i< highestTiles.size(); i++) {
			Pair<Integer, Pair<Integer,Integer>> current = highestTiles.poll();
			boolean doChange = false;
			if (prev.first != current.first && last.first != prev.first) {
				doChange =  true;
			}
			if (isAdjacent(current.second, last.second)) {
				total += 10*last.first;
			}
			
			for (Pair<Integer, Pair<Integer,Integer>> p: previous) {
				if (p.first == current.first) {
					if (isAdjacent(p.second, current.second)) {
						total += 30*p.first;
						if (p.second.first == 3 || current.second.first == 3) {
							total += 20*p.first;
						}
					}
				}
			}
			
			if (doChange) {
				last = prev;
			}
			prev = current;
			
			previous.add(prev);
			
		}
		return total;
	}
		
	public int score2(Model model) {
		int total = 0;
		
		// get sorted arraylists per value of tiles
		Tile[][] tiles = model.getTiles();
		PriorityQueue<Pair<Integer, Pair<Integer, Integer>>> highestTiles = new PriorityQueue<>();
			
		for (int i = 0 ; i<4; i++) {
			for (int j=0; j<4; j++) {
				if (tiles[i][j] != null) {	
					highestTiles.add(new Pair<Integer, Pair<Integer, Integer>>(tiles[i][j].getValue(), new Pair<Integer, Integer>(i, j)));
				}
			}
		}
		
		total += VOID*(16-highestTiles.size());
		ArrayList<ArrayList<Pair<Integer, Pair<Integer,Integer>>>> ts = new ArrayList<>();
		Pair<Integer, Pair<Integer,Integer>> temp = new Pair<Integer, Pair<Integer, Integer>>(0, new Pair<Integer, Integer>(0,0));
		boolean toChange = false;
		
		while(true) {
			ArrayList<Pair<Integer, Pair<Integer,Integer>>> currents =  new ArrayList<>();
			Pair<Integer, Pair<Integer,Integer>> curr;
			if (toChange && highestTiles.isEmpty()) {
				currents.add(temp);
				ts.add(currents);
				break;
			} else if (toChange){
				curr = temp;
			} else {
				if (highestTiles.isEmpty()) break;
				curr = highestTiles.poll();
			}
			
			currents.add(curr);
			
			toChange = false;

			while(!highestTiles.isEmpty()) {
				curr = highestTiles.poll();
				if (curr.first == currents.get(0).first) {
					currents.add(curr);
					total += curr.first;				
				} else {
					temp = curr;
					toChange = true;
					break;
				}				
			}
			ts.add(currents);
		}
		
		// find a score
		
		for (int i = 0 ; i< ts.size(); i++) {
			int tscore = 0;
			ArrayList<Pair<Integer, Pair<Integer,Integer>>> currents = ts.get(i);
			
			for (int j = 0 ; j<currents.size() ; j++) {
				Pair<Integer, Pair<Integer,Integer>> curr = currents.get(j);
				
				if (i>4) {
					if (curr.second.first == 3) {
						total -= BAD_TILE;
					}
					if (total <= 0) {
						total = 2;
					}
				}
				
				if (i==0 && j==0) {
					if ((curr.second.first == 3) && (curr.second.second == 0 || curr.second.second == 3)) {
						tscore += CORNER;
					}
				}
				
				if (i>3) {
					if (curr.second.first == 3) {
						tscore += BAD_TILE;
					}
				}
				
				for (Pair<Integer, Pair<Integer,Integer>> other : ts.get(i)) {
					if (curr == other) {
						continue;
					} else {
						if (curr.second.first == other.second.first) {
							tscore += MERGE+10;
						} else if (curr.second.second == other.second.second) {
							tscore += MERGE;
						}
					}
				}
				
				if (i>0) {
					for (Pair<Integer, Pair<Integer,Integer>> other : ts.get(i-1)) {
						if (curr.second.first == other.second.first) {
							tscore += STRUCT+5;
						} else if (curr.second.second == other.second.second) {
							tscore += STRUCT;
						}
					}
				}
			}
			
			total += tscore;
		}
		
		

		return total;
		
	}
	
	private boolean isAdjacent(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
		if (p1.first == p2.first && (p1.second == p2.second+1 || p1.second == p2.second-1)) {
			return true;
		} else if (p1.second == p2.second && (p1.first == p2.first+1 || p1.first == p2.first-1)) {
			return true;
		} else {
			return false;
		}
	}
	
	private int max(int a, int b, int c, int d) {
		if (a >= b && a >= c && a >= d) {
			return 2;
		} else if (b >= a && b >= c && b >= d) { 
			return 3;
		} else if (c >= a && c >= b && c >= d) {
			return 4;
		} else if (d >= a && d >= b && d >= c) {
			return 1;
		}
		return 2;
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

class Pair<K, V extends Comparable<V>> implements Comparable<Pair<Integer, V>> {
	int first;
	V second;
	
	public Pair(int a, V b) {
		first = a;
		second = b;
	}

	@Override
	public int compareTo(Pair<Integer, V> pPair) {
		int c = pPair.first - first;
		if (c == 0) {
			if (pPair.second instanceof Integer) {
				return (int) pPair.second - (int) this.second;
			}
			return this.second.compareTo(pPair.second);
		} else {
			return c;
		}
	}
	
	@Override
	public String toString() {
		return Integer.toString(first) + " : (" + second.toString() + ")";
	}
	
}

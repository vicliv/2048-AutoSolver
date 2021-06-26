package solver;

public class MoveRight extends AbstractMove {

	public MoveRight(Model pModel) {
		super(pModel);
	}

	
	@Override
	protected boolean execute(int i, boolean change) {
		Tile[][] tiles = model.getTiles();
		for (int j = 3; j>0; j--) {
			if (tiles[i][j] == null) {
				tiles[i][j] = tiles[i][j-1];
				if (tiles[i][j-1] != null) {
					change = true;
				}
				tiles[i][j-1] = null;
			} else if (tiles[i][j-1] != null) {
				if (!tiles[i][j].isMerged() && !tiles[i][j-1].isMerged()){
					int v = tiles[i][j].getValue();
					model.score += Tile.mergeTiles(tiles[i][j], tiles[i][j-1]);
					if (tiles[i][j].getValue() != v) {
						tiles[i][j-1] = null;
						change = true;
						tiles[i][j].setMerged();
					}
				}
			}
		}
		return change;
		
	}

}

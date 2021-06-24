package solver;

public class MoveRight extends AbstractMove {

	public MoveRight(Model pModel) {
		super(pModel);
	}

	@Override
	protected boolean execute(int i, boolean change) {
		Tile[][] tiles = model.getTiles();
		for (int j = 3; j>0; j--) {
			if (tiles[j][i] == null) {
				tiles[j][i] = tiles[j-1][i];
				if (tiles[j-1][i] != null) {
					change = true;
				}
				tiles[j-1][i] = null;
			} else if (tiles[j-1][i] != null) {
				if (!tiles[j][i].isMerged() && !tiles[j-1][i].isMerged()){
					int v = tiles[j][i].getValue();
					model.score += Tile.mergeTiles(tiles[j][i], tiles[j-1][i]);
					if (tiles[j][i].getValue() != v) {
						tiles[j-1][i] = null;
						change = true;
						tiles[j][i].setMerged();
					}
				}
			}	
		}
		return change;
		
	}

}

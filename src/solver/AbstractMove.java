package solver;

public abstract class AbstractMove {
	protected Model model;

	
	public AbstractMove(Model pModel) {
		model = pModel;
	}
	
	public final boolean perform() {
		boolean change = false;
		Display.back.addModel(model.clone());
		for (int k = 0; k<4; k++) {
			for (int i = 0 ; i < 4 ; i++) {
				change = execute(i, change);
			}
		}
		
		Tile[][] tiles = model.getTiles();
		for (int i = 0 ;  i<4 ; i++) {
        	for (int j = 0 ;  j<4 ; j++) {  		
        		if (tiles[i][j] != null) {
        			tiles[i][j].unMerged();
        		} else {
        		}
        	}
        }

		return change;		
	}
	
	protected abstract boolean execute(int i, boolean change);

}


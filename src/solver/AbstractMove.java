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
		if (change) {
			model.generateNewTile();
			Display.updateTiles();
		}
		return change;		
	}
	
	protected abstract boolean execute(int i, boolean change);

}


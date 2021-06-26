package solver;

import java.util.Stack;

public class Backup {
	private Stack<Model> undoStack = new Stack<>();
	private Stack<Model> redoStack = new Stack<>();

	public Backup() {
	}
	
	public void addModel(Model model) {
		undoStack.add(model);
	}
	
	public Model undo() {
		Model model;
		if (!undoStack.isEmpty()) {
			model = undoStack.pop();
			redoStack.add(model);
			return model;
		} else {
			return null;
		}
	}
	
	public Model redo() {
		Model model;
		if (!redoStack.isEmpty()) {
			model = redoStack.pop();
			undoStack.add(model);
			return model;
		} else {
			return null;
		}
	}
	
	public void reset() {
		undoStack.clear();
		redoStack.clear();
	}
	
	public void clear() {
		if (undoStack.size() > 15) {
			undoStack.remove(0);
		}
		
		if (redoStack.size() > 15) {
			redoStack.remove(0);
		}

	}

}

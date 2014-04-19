package com.tylerpackard.edits;

import com.tylerpackard.ui.Window;

import java.util.Stack;

public class EditManager {
	Stack<Edit> edits = new Stack<>();
	Stack<Edit> redoables = new Stack<>();
	private Window parent;


	public EditManager (Window parent) {
		this.parent = parent;
	}


	public void undo() {
		if (edits.size() > 0) {
			redoables.push(edits.pop());
			redoables.peek().enact(false);
			parent.repaint();
		}
	}

	public void redo() {
		if (redoables.size() > 0) {
			edits.push(redoables.pop());
			edits.peek().enact(true);
			parent.repaint();
		}
	}

	public void push(Edit edit) {
		redoables.clear();
		edits.push(edit);
	}

	public Edit peek() {
		if (edits.size() > 0) {
			return edits.peek();
		}
		return null;
	}
}

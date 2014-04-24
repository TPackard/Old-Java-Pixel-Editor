package com.tylerpackard.edits;

import com.tylerpackard.ui.Window;

import java.util.Stack;

/**
 * Manages all of the edits made to an image.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 */
public class EditManager {

	/**
	 * All of the edits performed and not undone.
	 */
	Stack<Edit> edits = new Stack<>();

	/**
	 * All of the edits that were undone and can be redone.
	 */
	Stack<Edit> redoables = new Stack<>();

	/**
	 * The parent that owns the EditManager.
	 */
	private Window parent;


	/**
	 * Sets the parent to the given Window.
	 *
	 * @param parent The parent Window
	 */
	public EditManager (Window parent) {
		this.parent = parent;
	}


	/**
	 * Undoes the last edit and moves it into the stack of redoables if there are any edits left.
	 */
	public void undo() {
		if (edits.size() > 0) {
			redoables.push(edits.pop());
			redoables.peek().enact(false);
			parent.repaint();
		}
	}

	/**
	 * Redoes the last edit undone and moves it back into the stack of edits if there are any left.
	 */
	public void redo() {
		if (redoables.size() > 0) {
			edits.push(redoables.pop());
			edits.peek().enact(true);
			parent.repaint();
		}
	}

	/**
	 * Adds a new edit to the stack of edits and clears the stack of redoables.
	 *
	 * @param edit The edit to add
	 */
	public void push(Edit edit) {
		redoables.clear();
		edits.push(edit);
	}

	/**
	 * Returns the last edit pushed if there are any.
	 *
	 * @return The last edit pushed
	 */
	public Edit peek() {
		if (edits.size() > 0) {
			return edits.peek();
		}
		return null;
	}
}

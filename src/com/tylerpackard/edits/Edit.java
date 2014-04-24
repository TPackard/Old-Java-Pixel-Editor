package com.tylerpackard.edits;

import com.tylerpackard.tools.Tool;

/**
 * An edit is an undoable and redoable action that changed something.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 */
public abstract class Edit {
	/**
	 * The tool that performed the edit.
	 */
	final Tool tool;


	/**
	 * Sets the tool that made the edit to the one provided.
	 *
	 * @param tool The tool that made the edit
	 */
	public Edit(Tool tool) {
		this.tool = tool;
	}


	/**
	 * Returns the Tool that made the edit.
	 *
	 * @return The tool that made the edit
	 */
	public Tool getTool() {
		return tool;
	}

	/**
	 * Undoes or redoes the edit based on the given boolean.
	 *
	 * @param redo Whether or not to redo (true for redo, false for undo)
	 */
	public abstract void enact(boolean redo);
}

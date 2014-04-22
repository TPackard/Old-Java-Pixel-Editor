package com.tylerpackard.edits;

import com.tylerpackard.tools.Tool;

public abstract class Edit {
	final Tool tool;


	public Edit(Tool tool) {
		this.tool = tool;
	}


	public Tool getTool() {
		return tool;
	}

	public abstract void enact(boolean redo);
}

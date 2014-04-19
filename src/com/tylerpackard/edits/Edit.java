package com.tylerpackard.edits;

import com.tylerpackard.tools.Tool;

import java.util.ArrayList;

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

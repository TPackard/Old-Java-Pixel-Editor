package com.tylerpackard.command;

/**
 * Commands can be executed or unexecuted by calling unexecute.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 */
public interface Command {

	/**
	 * Perform the command.
	 */
	void execute();

	/**
	 * Undo the command.
	 */
	void unexecute();
}

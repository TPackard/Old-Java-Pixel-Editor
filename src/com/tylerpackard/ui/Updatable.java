package com.tylerpackard.ui;

/**
 * The Updatable contains useful methods for updatable GUI components.
 */
public interface Updatable {

	/**
	 * Update should contain code that does anything that the component needs to do periodically, such as repainting.
	 */
	void update();

	/**
	 * Reposition should contain code that repositions the component inside of its parent.
	 */
	void reposition();

	/**
	 * Defocus should strip focus from all child components that have focus.
	 */
	void defocus();
}
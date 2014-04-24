package com.tylerpackard.edits;

import com.tylerpackard.tools.Tool;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * The draw edit allows the drawing of a point or set of points to be undone or redone.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 */
public class DrawEdit extends Edit {

	/**
	 * The image which was edited
	 */
	private final BufferedImage image;

	/**
	 * A list of all changes made in the edit
	 */
	private ArrayList<Change> changes = new ArrayList<>();


	/**
	 * Creates a new edit from the Tool used and sets the image to edit
	 *
	 * @param tool The tool used to make the edit
	 * @param image The image edited
	 */
	public DrawEdit(Tool tool, BufferedImage image) {
		super(tool);
		this.image = image;
	}


	/**
	 * Undoes or redoes the edit based on the given boolean.
	 *
	 * @param redo Whether or not to redo (true for redo, false for undo)
	 */
	public void enact (boolean redo) {
		for (Change change : changes) {
			image.setRGB(change.getX(), change.getY(), change.getColor(redo));
		}
	}

	/**
	 * Adds a new change to the edit if it hasn't already been added and enacts it.
	 *
	 * @param x The X position of the change
	 * @param y The Y position of the change
	 * @param prevRGB The previous RGB value at the location
	 * @param newRGB The RGB value to replace it with
	 */
	public void addChange(int x, int y, int prevRGB, int newRGB) {
		Change change = new Change(x, y, prevRGB, newRGB);
		if (!changes.contains(change)) {
			changes.add(change);
			image.setRGB(x, y, newRGB);
			tool.getParent().getParent().repaint();
		}
	}


	/**
	 * Each change is an a subsection of an edit that is the modification of a single pixel. Each DrawEdit is composed
	 * of one or more Changes.
	 *
	 * @author Tyler Packard
	 * @version 1
	 * @since 0.0.1
	 */
	private class Change {

		/**
		 * The X position of the change.
		 */
		private int x;

		/**
		 * The Y position of the change.
		 */
		private int y;

		/**
		 * The previous RGB value at the location.
		 */
		private int prevRGB;

		/**
		 * The RGB value to replace it with.
		 */
		private int newRGB;


		/**
		 * Sets the position and colors of the Change.
		 *
		 * @param x The X position of the change
		 * @param y The Y position of the change
		 * @param prevRGB The previous RGB value at the location
		 * @param newRGB The RGB value to replace it with
		 */
		public Change(int x, int y, int prevRGB, int newRGB) {
			this.x = x;
			this.y = y;
			this.prevRGB = prevRGB;
			this.newRGB = newRGB;
		}


		/**
		 * Figures out whether or not the given object is equal to this change.
		 *
		 * @param object The object to see if equal
		 * @return Whether or not this is equal to the object
		 */
		@Override
		public boolean equals(Object object) {
			if (object instanceof Change) {
				Change change = (Change) object;
				return (x == change.getX() && y == change.getY());
			}
			return super.equals(object);
		}

		/**
		 * Returns the color of the Change. Returns the new RGB value if redo is true, or the previous RGB value if it's
		 * false.
		 *
		 * @param redo Whether or not to get the color for redoing the Change
		 * @return The color of the Change
		 */
		public int getColor(boolean redo) {
			if (redo) {
				return newRGB;
			}
			return prevRGB;
		}

		/**
		 * Returns the X position of the change
		 *
		 * @return The X position of the change
		 */
		public int getX() {
			return x;
		}

		/**
		 * Returns the Y position of the change
		 *
		 * @return The Y position of the change
		 */
		public int getY() {
			return y;
		}
	}
}

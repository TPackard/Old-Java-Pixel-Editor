package com.tylerpackard.edits;

import com.tylerpackard.tools.Tool;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DrawEdit extends Edit {
	private final BufferedImage image;
	private ArrayList<Change> edits = new ArrayList<>();


	public DrawEdit(Tool tool, BufferedImage image) {
		super(tool);
		this.image = image;
	}


	public void enact (boolean redo) {
		for (Change change : edits) {
			image.setRGB(change.getX(), change.getY(), change.getColor(redo));
		}
	}

	public void addChange(int x, int y, int prevRGB, int newRGB) {
		Change change = new Change(x, y, prevRGB, newRGB);
		if (!edits.contains(change)) {
			edits.add(change);
			image.setRGB(x, y, newRGB);
			tool.getParent().getParent().repaint();
		}
	}

	private class Change {
		private int x, y, prevRGB, newRGB;

		public Change(int x, int y, int prevRGB, int newRGB) {
			this.x = x;
			this.y = y;
			this.prevRGB = prevRGB;
			this.newRGB = newRGB;
		}

		@Override
		public boolean equals(Object object) {
			if (object instanceof Change) {
				Change change = (Change) object;
				return (x == change.getX() && y == change.getY());
			}
			return false;
		}

		public int getColor(boolean redo) {
			if (redo) {
				return newRGB;
			}
			return prevRGB;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}
}

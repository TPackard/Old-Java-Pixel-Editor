package com.tylerpackard.toolbox.toolchooser;

import com.tylerpackard.edits.EditManager;
import com.tylerpackard.toolbox.colorchooser.ColorChooser;
import com.tylerpackard.tools.*;
import com.tylerpackard.ui.Updatable;
import com.tylerpackard.ui.Window;

import javax.swing.*;
import java.awt.*;

/**
 * The ToolChooser contains all of the tools and creates an interface that lets the user select a tool.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 */
public class ToolChooser extends JPanel implements Updatable {

	/**
	 * The Tool that is currently selected. Points to one of the more specific Tool constants.
	 */
	private Tool selectedTool;

	/**
	 * The Window that contains the ToolChooser.
	 */
	private Window parent;

	/**
	 * The ToolChooser's Pencil tool constant.
	 */
	private final Pencil PENCIL;

	/**
	 * The ToolChooser's Eraser tool constant.
	 */
	private final Eraser ERASER;

	/**
	 * The ToolChooser's Bucket tool constant.
	 */
	private final Bucket BUCKET;

	/**
	 * The ToolChooser's ColorPicker tool constant.
	 */
	private final ColorPicker PICKER;

	/**
	 * The width of the ToolChooser.
	 */
	private final int width;

	/**
	 * Whether or not to temporarily return the color picker.
	 */
	private boolean tempPicker = false;


	/**
	 * Creates a new ToolChooser and its Tools and sets its dimensions and position.
	 *
	 * @param parent The containing Window
	 * @param colorChooser The ColorChooser to get colors from
	 */
	public ToolChooser(Window parent, ColorChooser colorChooser) {
		this.parent = parent;
		setBackground(new Color(0x444448));
		setLayout(null);
		width = parent.getRightWidth();

		PENCIL = new Pencil(this, 8, 8, colorChooser);
		add(PENCIL);
		ERASER = new Eraser(this, 8, 48);
		add(ERASER);
		BUCKET = new Bucket(this, 8, 88, colorChooser);
		add(BUCKET);
		PICKER = new ColorPicker(this, 8, 128, colorChooser);
		add(PICKER);
		selectedTool = PENCIL;
		PENCIL.select();

		reposition();
	}

	/**
	 * Called by the parent Window when it needs to be updated, unused.
	 */
	@Override
	public void update() {

	}

	/**
	 * Repositions the ToolChooser inside the Window. Spans the entire height of the Window and stays on the right side.
	 */
	@Override
	public void reposition() {
		setBounds(parent.width() - width, 0, width, parent.height());
	}

	/**
	 * Removes focus from any focusable objects. Unused.
	 */
	@Override
	public void defocus() {

	}

	/**
	 * @return The parent Window
	 */
	public Window getParent() {
		return parent;
	}

	/**
	 * Returns the Tool that the user last selected, or the color picker if the ToolChooser is told to temporarily
	 * return the color picker.
	 *
	 * @return The selected Tool
	 */
	public Tool getSelectedTool() {
		if (tempPicker) {
			return PICKER;
		} else {
			return selectedTool;
		}
	}

	/**
	 * @return The containing Window's EditManager
	 */
	public EditManager getEditManager() {
		return parent.getEditManager();
	}

	/**
	 * Switches selection from the selected tool to the given one.
	 *
	 * @param tool The Tool to make selected
	 */
	public void requestSelection(Tool tool) {
		selectedTool.deselect();
		selectedTool = tool;
		selectedTool.select();
		parent.repaint();
	}

	/**
	 * Draws the buttons for each of the tools.
	 *
	 * @param g The Graphics to paint with
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(PENCIL.getBG(), 8, 8, 32, 32, null);
		g.drawImage(PENCIL.getIcon(), 8, 8, 32, 32, null);
		g.drawImage(ERASER.getBG(), 8, 48, 32, 32, null);
		g.drawImage(ERASER.getIcon(), 8, 48, 32, 32, null);
		g.drawImage(BUCKET.getBG(), 8, 88, 32, 32, null);
		g.drawImage(BUCKET.getIcon(), 8, 88, 32, 32, null);
		g.drawImage(PICKER.getBG(), 8, 128, 32, 32, null);
		g.drawImage(PICKER.getIcon(), 8, 128, 32, 32, null);
	}

	/**
	 * True if the ToolChooser should temporarily return a color picker instead of the selected tool, false otherwise.
	 *
	 * @param tempPicker Whether or not to use the temporary picker
	 */
	public void setTempPicker(boolean tempPicker) {
		this.tempPicker = tempPicker;
		parent.repaint();
	}
}

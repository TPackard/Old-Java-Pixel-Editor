package com.tylerpackard.toolbox.toolchooser;

import com.tylerpackard.edits.EditManager;
import com.tylerpackard.toolbox.colorchooser.ColorChooser;
import com.tylerpackard.tools.*;
import com.tylerpackard.ui.Updatable;
import com.tylerpackard.ui.Window;

import javax.swing.*;
import java.awt.*;

public class ToolChooser extends JPanel implements Updatable {
	private Tool selectedTool;
	private Window parent;
	private final Pencil PENCIL;
	private final Eraser ERASER;
	private final Bucket BUCKET;
	private final ColorPicker PICKER;
	private final int width;
	private boolean tempPicker = false;


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

	@Override
	public void update() {

	}

	@Override
	public void reposition() {
		setBounds(parent.width() - width, 0, width, parent.height());
	}

	@Override
	public void defocus() {

	}

	public Window getParent() {
		return parent;
	}

	public Tool getSelectedTool() {
		if (tempPicker) {
			return PICKER;
		} else {
			return selectedTool;
		}
	}

	public EditManager getEditManager() {
		return parent.getEditManager();
	}

	public void requestSelection(Tool tool) {
		selectedTool.deselect();
		selectedTool = tool;
		parent.repaint();
	}

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

	public void setTempPicker(boolean tempPicker) {
		this.tempPicker = tempPicker;
		parent.repaint();
	}
}

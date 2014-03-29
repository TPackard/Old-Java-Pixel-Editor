package com.tylerpackard.toolbox.toolchooser;

import com.tylerpackard.toolbox.colorchooser.ColorChooser;
import com.tylerpackard.tools.*;
import com.tylerpackard.ui.Updatable;
import com.tylerpackard.ui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class ToolChooser extends JPanel implements Updatable {
	private Tool selectedTool;
	private Window parent;
	private final Pencil PENCIL;
	private final Eraser ERASER;
	private final int width;

	public ToolChooser(Window parent, ColorChooser colorChooser) {
		this.parent = parent;
		setBackground(new Color(0xEEEEF2));
		setLayout(null);
		width = parent.getRightWidth();

		PENCIL = new Pencil(this, 8, 8, colorChooser);
		add(PENCIL);
		ERASER = new Eraser(this, 8, 48);
		add(ERASER);
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

	public Tool getSelectedTool() {
		return selectedTool;
	}

	public void requestSelection(Tool tool) {
		selectedTool.deselect();
		selectedTool = tool;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(PENCIL.getBG(), 8, 8, 32, 32, null);
		g.drawImage(PENCIL.getIcon(), 8, 8, 32, 32, null);
		g.drawImage(ERASER.getBG(), 8, 48, 32, 32, null);
		g.drawImage(ERASER.getIcon(), 8, 48, 32, 32, null);
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (x >= 8 && x <= 40) {
			if (y >= 8 && y <= 40) {
				selectedTool = PENCIL;
			} else if (y >= 48 && y <= 80) {
				selectedTool = ERASER;
			}
		}
	}
}

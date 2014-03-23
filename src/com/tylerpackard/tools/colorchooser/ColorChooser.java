package com.tylerpackard.tools.colorchooser;

import com.tylerpackard.ui.Switch;
import com.tylerpackard.ui.Window;

import javax.swing.*;
import java.awt.*;

public class ColorChooser extends JPanel {
	private Window parent;
	private final Switch typeSwitch;
	private final ColorSlider slider1;
	private final ColorSlider slider2;
	private final ColorSlider slider3;
	private final ColorPreview preview;
	private Color foreground;
	private Color background;
	boolean foreSelected = true;


	public ColorChooser(Window parent) {
		this.parent = parent;
		parent.add(this);
		setSize(192, 224);
		setLocation(0, parent.getHeight() - getHeight());
		setVisible(true);
		setOpaque(true);
		setBackground(new Color(0xEEEEF2));
		setFocusable(false);
		typeSwitch = new Switch("HSB", "RGB", 60, 16, 72, 16, true);
		add(typeSwitch);
		slider1 = new ColorSlider(this, 16, 48, "Hue", 360);
		slider2 = new ColorSlider(this, 16, 80, "Saturation", 100);
		slider3 = new ColorSlider(this, 16, 112, "Brightness", 100);
		preview = new ColorPreview(this, 152);
		foreground = new Color(0xFFFFFF);
		background = new Color(0);
		setForeSelected(true);
		setLayout(null);
	}

	public void update() {
		typeSwitch.update();
		if (typeSwitch.flagged()) {
			switchType();
		}
		slider1.update();
		slider2.update();
		slider3.update();
		preview.update();
		setLocation(0, parent.getHeight() - getHeight());
		repaint();
	}

	public void updateColor() {
		int color;
		if (typeSwitch.getState()) {
			color = Color.HSBtoRGB(slider1.getValue(), slider2.getValue(), slider3.getValue());
		} else {
			color = new Color(slider1.getValue(), slider2.getValue(), slider3.getValue()).getRGB();
		}

		if (foreSelected) {
			foreground = new Color(color);
		} else {
			background = new Color(color);
		}
	}

	public Color getForegroundColor() {
		return foreground;
	}

	public Color getBackgroundColor() {
		return background;
	}

	public void setForeSelected(boolean foreSelected) {
		this.foreSelected = foreSelected;
		if (foreSelected) {
			setSliders(foreground);
		} else {
			setSliders(background);
		}
	}

	void setSliders(Color color) {
		if (typeSwitch.getState()) {
			float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
			slider1.setValue(hsb[0]);
			slider2.setValue(hsb[1]);
			slider3.setValue(hsb[2]);
		} else {
			slider1.setValue(color.getRed() / 255.0);
			slider2.setValue(color.getGreen() / 255.0);
			slider3.setValue(color.getBlue() / 255.0);
		}
	}

	void switchType() {
		if (typeSwitch.getState()) {
			slider1.setLimit(360);
			slider1.setToolTipText("Hue");
			slider2.setLimit(100);
			slider2.setToolTipText("Saturation");
			slider3.setLimit(100);
			slider3.setToolTipText("Brightness");
		} else {
			slider1.setLimit(255);
			slider1.setToolTipText("Red");
			slider2.setLimit(255);
			slider2.setToolTipText("Green");
			slider3.setLimit(255);
			slider3.setToolTipText("Blue");
		}
		setForeSelected(foreSelected);
	}
}
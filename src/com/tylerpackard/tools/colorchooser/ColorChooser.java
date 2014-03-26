package com.tylerpackard.tools.colorchooser;

import com.tylerpackard.ui.Switch;
import com.tylerpackard.ui.Updatable;
import com.tylerpackard.ui.Window;

import javax.swing.*;
import java.awt.*;

public class ColorChooser extends JPanel implements Updatable{
	private Window parent;
	private final Switch typeSwitch = new Switch("HSB", "RGB", 60, 16, 72, 16, true);
	private final ColorSlider slider1 = new ColorSlider(this, 10, 48, "Hue", 360);
	private final ColorSlider slider2 = new ColorSlider(this, 10, 72, "Saturation", 100);
	private final ColorSlider slider3 = new ColorSlider(this, 10, 96, "Brightness", 100);
	private final ColorSlider sliderA = new ColorSlider(this, 10, 120, "Alpha", 100);
	private final ColorPreview preview = new ColorPreview(this, 152);
	private Color foreground = new Color(0x000000);
	private Color background = new Color(0xFFFFFF);
	boolean foreSelected = true;


	public ColorChooser(Window parent) {
		super();
		this.parent = parent;
		setSize(192, 224);
		setLocation(0, parent.height() - getHeight());
		setVisible(true);
		setOpaque(true);
		setBackground(new Color(0xEEEEF2));
		setFocusable(false);
		add(typeSwitch);
		setForeSelected(true);
		setLayout(null);
	}


	@Override
	public void update() {
		if (typeSwitch.flagged()) {
			switchType();
		}
		slider1.update();
		slider2.update();
		slider3.update();
		sliderA.update();
	}

	@Override
	public void reposition() {
		setLocation(0, parent.height() - getHeight());
	}

	public void updateColor() {
		Color color;

		if (typeSwitch.getState()) {
			float[] rgb = Color.getHSBColor(slider1.getValue(), slider2.getValue(), slider3.getValue()).getRGBColorComponents(null);
			color = new Color(rgb[0], rgb[1], rgb[2], sliderA.getValue());
		} else {
			color = new Color(slider1.getValue(), slider2.getValue(), slider3.getValue(), sliderA.getValue());
		}

		if (foreSelected) {
			foreground = color;
		} else {
			background = color;
		}

		slider1.paintBar();
		slider2.paintBar();
		slider3.paintBar();
		sliderA.paintBar();

		repaint();
	}

	public Color getForegroundColor() {
		return foreground;
	}

	public Color getBackgroundColor() {
		return background;
	}

	public Color getColor() {
		if (foreSelected) {
			return foreground;
		} else {
			return background;
		}
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
		sliderA.setValue(color.getAlpha() / 255.0);
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

	Color colorAt(float value, ColorSlider slider) {
		float value1 = slider1.getValue();
		float value2 = slider2.getValue();
		float value3 = slider3.getValue();
		float valueA = sliderA.getValue();
		if (slider.equals(slider1)) {
			value1 = value;
		} else if (slider.equals(slider2)) {
			value2 = value;
		} else if (slider.equals(slider3)) {
			value3 = value;
		} else if (slider.equals(sliderA)) {
			valueA = value;
		}

		if (typeSwitch.getState()) {
			float[] rgb = Color.getHSBColor(value1, value2, value3).getRGBColorComponents(null);
			return new Color(rgb[0], rgb[1], rgb[2], valueA);
		} else {
			return new Color(value1, value2, value3, valueA);
		}
	}

	public void defocus() {
		slider1.defocus();
		slider2.defocus();
		slider3.defocus();
		sliderA.defocus();
	}
}
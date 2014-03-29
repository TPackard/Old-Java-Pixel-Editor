package com.tylerpackard.toolbox.colorchooser;

import com.tylerpackard.ui.TextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class ColorSlider extends JPanel implements MouseListener, MouseMotionListener {
	private final ColorChooser parent;
	private int value = 0;
	private double scale;
	private int limit;
	private double doubleLimit;
	private KeyAdapter keyAdapter;
	private FocusAdapter focusAdapter;
	private final TextField textField = new TextField("0", TextField.NUMS_ONLY);
	private final JLabel label;
	private final int barStart = 16;
	private final int barEnd = 112;
	private final int barWidth = 8;
	private boolean mouseDown;
	private int mouseX;
	private int mouseY;
	private long lastMouseUpdate = 0;
	private final int[] arrowX;
	private final int[] arrowY1 = new int[]{6, 0, 6};
	private final int[] arrowY2 = new int[]{10, 16, 10};
	private final int[] sliderYPoints = new int[] {4, 0, 0, 4, 12, 15, 15, 12};
	private BufferedImage bar;
	private BufferedImage barBackground;
	private Graphics barG;


	public ColorSlider(ColorChooser parent, int x, int y, String toolTip, final int limit) {
		super();
		this.parent = parent;
		parent.add(this);
		setBounds(x, y, 172, 18);
		bar = new BufferedImage(barEnd - barStart, 8, BufferedImage.TYPE_INT_ARGB);
		barG = bar.getGraphics();
		makeBarBackground();
		setVisible(true);
		setOpaque(false);
		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		setLayout(null);
		setLimit(limit);
		setCursor(new Cursor(Cursor.HAND_CURSOR));

		textField.setBounds(barEnd + 8, 0, 34, 16);
		textField.setFont(new Font("SansSerif", Font.PLAIN, 10));
		textField.addKeyListener(keyAdapter);
		textField.addFocusListener(focusAdapter);
		add(textField);

		label = new JLabel(toolTip.substring(0, 1));
		label.setFont(new Font("SansSerif", Font.PLAIN, 10));
		label.setBounds(0, 0, barStart, 16);
		add(label);

		setToolTipText(toolTip);

		arrowX = new int[] {getWidth() - 12, getWidth() - 6, getWidth()};
	}


	public void update() {
		updateMouse();
	}

	private void updateMouse() {
		if (mouseDown && System.nanoTime() - lastMouseUpdate >= 200000000) {
			if (mouseX >= arrowX[0]) {
				if (mouseY <= 8) {
					if (value < limit) {
						value++;
						textField.setText(Integer.toString(value));
						parent.updateColor();
					} else {
						Toolkit.getDefaultToolkit().beep();
					}
				} else if (mouseY >= 12) {
					if (value > 0) {
						value--;
						textField.setText(Integer.toString(value));
						parent.updateColor();
					} else {
						Toolkit.getDefaultToolkit().beep();
					}
				}
			}
			lastMouseUpdate = System.nanoTime();
		}
	}

	public float getValue() {
		return value / (float)doubleLimit;
	}

	public void setValue(double value) {
		this.value = (int) Math.round(value * limit);
		textField.setText(Integer.toString(this.value));
		parent.updateColor();
	}

	public void setLimit(int limit) {
		this.limit = limit;
		this.doubleLimit = limit;
		scale = (barEnd - barStart) / doubleLimit;
		createAdapters(limit);
	}

	private void createAdapters(final int limit) {
		keyAdapter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					double value = Float.parseFloat(textField.getText());
					if (value > limit) {
						value = (double) limit;
					}
					value /= (double) limit;
					setValue(value);
				}
			}
		};

		focusAdapter = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				double value = Float.parseFloat(textField.getText());
				if (value > limit) {
					value = (double) limit;
				}
				value /= (double) limit;
				setValue(value);
			}
		};
	}

	public void defocus() {
		textField.setFocusable(false);
		textField.setFocusable(true);
	}

	@Override
	public void setToolTipText(String text) {
		super.setToolTipText(text);
		label.setText(text.substring(0, 1));
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		g.drawImage(bar, barStart, 4, null);

		/* SLIDER */
		g.setColor(new Color(0x444448));
		int point = (int)Math.round(value * scale) + barStart;
		int offset = barWidth / 2;
		int[] sliderXPoints = new int[]{point, point - offset, point + offset, point, point, point - offset + 1, point + offset - 1, point};
		g.fillPolygon(sliderXPoints, sliderYPoints, 8);

		/* ARROWS */
		g.fillPolygon(arrowX, arrowY1, 3);
		g.fillPolygon(arrowX, arrowY2, 3);
	}

	public void paintBar() {
		final int sliverWidth = 3;
		barG.drawImage(barBackground, 0, 0, null);
		for (int i = 0; i < barEnd - barStart; i += sliverWidth) {
			barG.setColor(parent.colorAt((i / (float) scale) / (float) doubleLimit, this));
			barG.fillRect(i, 0, sliverWidth, 8);
		}
	}

	public void makeBarBackground() {
		barBackground = new BufferedImage(barEnd - barStart, 8, BufferedImage.TYPE_INT_ARGB);
		int width = barBackground.getWidth();
		int height = barBackground.getHeight();

		Graphics g = barBackground.getGraphics();
		g.setColor(new Color(0xCCCCD2));
		g.fillRect(0, 0, width, 8);

		int squareSize = 4;
		g.setColor(Color.WHITE);
		for (int x = 0; x < width; x += squareSize * 2) {
			for (int y = 0; y < height; y += squareSize * 2) {
				g.fillRect(x, y, squareSize, squareSize);
				g.fillRect(x + squareSize, y + squareSize, squareSize, squareSize);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseDown = true;
		mouseX = e.getX();
		mouseY = e.getY();
		if (mouseX > barStart && mouseX <= barEnd) {
			value = (int)Math.round((e.getX() - barStart) / scale);
			textField.setText(Integer.toString(value));
			parent.updateColor();
		} else if (mouseX > barEnd && mouseX <= barEnd) {
			value = limit;
			textField.setText(Integer.toString(value));
			parent.updateColor();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseDown = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (mouseX >= barStart - barWidth / 2 && mouseX <= barEnd + barWidth / 2) {
			int x = e.getX();
			if (x <= barStart) {
				value = 0;
			} else if (x > barEnd) {
				value = limit;
			} else {
				value = (int)Math.round((e.getX() - barStart) / scale);
			}
			textField.setText(Integer.toString(value));
			parent.updateColor();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}
}
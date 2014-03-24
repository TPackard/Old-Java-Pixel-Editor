package com.tylerpackard.ui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class TextField extends JTextField {
	public static final int NUMS_ONLY = 0;


	public TextField(String text) {
		super(text);
	}

	public TextField(String text, int filter) {
		super(text);
		if (filter == NUMS_ONLY) {
			((AbstractDocument)getDocument()).setDocumentFilter(new NumsOnly());
		}
	}


	private class NumsOnly extends DocumentFilter {
		@Override
		public void insertString(DocumentFilter.FilterBypass bypass, int offset, String string, AttributeSet aSet) throws BadLocationException {
			if (string.matches(".*\\D++.*")) {
				Toolkit.getDefaultToolkit().beep();
			}
			bypass.insertString(offset, string.replaceAll("\\D++", ""), aSet);
		}

		@Override
		public void replace(DocumentFilter.FilterBypass bypass, int offset, int length, String string, AttributeSet aSet) throws BadLocationException {
			if (string.matches(".*\\D++.*")) {
				Toolkit.getDefaultToolkit().beep();
			}
			bypass.replace(offset, length, string.replaceAll("\\D++", ""), aSet);
		}
	}
}
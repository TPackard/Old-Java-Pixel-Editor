package com.tylerpackard.ui;

import javax.swing.*;
import javax.swing.text.*;

public class TextField extends JTextField {
	public static final int NUMS_ONLY = 0;
	public static final int HEX_ONLY = 1;


	public TextField(String text) {
		super(text);
	}

	public TextField(String text, int filter) {
		super(text);
		if (filter == NUMS_ONLY) {
			((AbstractDocument)getDocument()).setDocumentFilter(new NumsOnly());
		} else if (filter == HEX_ONLY) {
			((AbstractDocument)getDocument()).setDocumentFilter(new HexOnly());
		}
	}


	private class NumsOnly extends DocumentFilter {
		@Override
		public void insertString(DocumentFilter.FilterBypass bypass, int offset, String string, AttributeSet aSet) throws BadLocationException {
			bypass.insertString(offset, string.replaceAll("\\D++", ""), aSet);
		}

		@Override
		public void replace(DocumentFilter.FilterBypass bypass, int offset, int length, String string, AttributeSet aSet) throws BadLocationException {
			bypass.replace(offset, length, string.replaceAll("\\D++", ""), aSet);
		}
	}

	private class HexOnly extends DocumentFilter {
		@Override
		public void insertString(DocumentFilter.FilterBypass bypass, int offset, String string, AttributeSet aSet) throws BadLocationException {
			bypass.insertString(offset, string.replaceAll("\\[^A-Fa-f0-9]++", ""), aSet);
		}

		@Override
		public void replace(DocumentFilter.FilterBypass bypass, int offset, int length, String string, AttributeSet aSet) throws BadLocationException {
			bypass.replace(offset, length, string.replaceAll("\\[^A-Fa-f0-9]++", ""), aSet);
		}
	}
}
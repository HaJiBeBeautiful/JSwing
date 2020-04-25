package sd.jswing.pro.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JTextPane;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import sd.jswing.pro.common.Constants;

/**
 * A class illustrating running line number count on JTextPane. Nothing is
 * painted on the pane itself, but a separate JPanel handles painting the line
 * numbers.<br>
 *
 * @author Daniel Sj?blom<br>
 *         Created on Mar 3, 2004<br>
 *         Copyright (c) 2004<br>
 * @version 1.0<br>
 */
public class LineNr extends JPanel {
	// for this simple experiment, we keep the pane + scrollpane as members.
	// JTextPane pane;
	private JTextPane pane;
	private JScrollPane scrollPane;
	
	private int initFontSize = Constants.Font.TextAREA_FONT_SIZE;
	
	private int linesLen = 1;
	
	public LineNr(JScrollPane scrollPane) {
		super();
		//setBackground(Color.white);
		setPreferredSize(new Dimension(20, 20));
		this.scrollPane = scrollPane;
		this.pane = (JTextPane)scrollPane.getViewport().getView();
	}

	public void paint(Graphics g) {
		super.paint(g);
		// We need to properly convert the points to match the viewport
		// Read docs for viewport
		int start = pane.viewToModel(scrollPane.getViewport().getViewPosition()); // starting pos in document
		int end = pane.viewToModel(new Point(scrollPane.getViewport().getViewPosition().x + pane.getWidth(),
				scrollPane.getViewport().getViewPosition().y + pane.getHeight()));
		// end pos in doc
		// translate offsets to lines
		Document doc = pane.getDocument();
		int startline = doc.getDefaultRootElement().getElementIndex(start) + 1;
		int endline = doc.getDefaultRootElement().getElementIndex(end) + 1;
		int fontHeight = g.getFontMetrics(pane.getFont()).getHeight();
		int fontDesc = g.getFontMetrics(pane.getFont()).getDescent();
		int starting_y = -1;
		try {
			starting_y = pane.modelToView(start).y - scrollPane.getViewport().getViewPosition().y + fontHeight
					- fontDesc;
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		g.setColor(new Color(180, 180, 180));
		g.setFont(new Font(null, Font.PLAIN, initFontSize));
		for (int line = startline, y = starting_y; line <= endline; y += fontHeight, line++) {
			int len = String.valueOf(line).length();
			setWidth(len,(len+1)*10,(len+1)*10);
			g.drawString(Integer.toString(line),2, y);
		}
	}
	
	public void setWidth(int len,int width,int height) {
		if(len>linesLen) {
			linesLen = len;
			setPreferredSize(new Dimension(width,height));
			updateUI();
		}else if(len<linesLen){
			linesLen = len;
			setPreferredSize(new Dimension(width, height));
			updateUI();
		}
	}
}

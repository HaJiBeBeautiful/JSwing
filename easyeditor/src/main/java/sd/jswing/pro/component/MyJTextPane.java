package sd.jswing.pro.component;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.swing.JTextPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import sd.jswing.pro.common.Constants;
import sd.jswing.pro.common.FileInfo;
import sd.jswing.pro.common.FileUtils;

public class MyJTextPane extends  JTextPane{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LineNr lineNumber;
	
	public MyJTextPane() {
		this.setFont(new Font(null, Font.PLAIN,Constants.Font.TextAREA_FONT_SIZE));
		//文本改变 监听器
		this.getDocument().addUndoableEditListener(new UndoableEditListener() {
					
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				setChange(true);
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		if(null != lineNumber) {
			lineNumber.repaint();
		}
	}
	
	/**
	 * 内容是否改变
	 */
	private boolean isChange = false;

	public boolean isChange() {
		return isChange;
	}

	public void setChange(boolean isChange) {
		this.isChange = isChange;
	}

	public void setLineNumber(LineNr lineNumber) {
		this.lineNumber = lineNumber;
	}
}

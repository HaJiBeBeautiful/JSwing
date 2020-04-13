package sd.jswing.pro.component;

import java.awt.Font;

import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;

import sd.jswing.pro.common.Constants;

public class MyJTextArea extends JTextArea{
	/**
	 * 内容是否改变
	 */
	private boolean isChange = false;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyJTextArea() {
		this(null, null, 0, 0);
	}
	
	public MyJTextArea(String text) {
		this(null, text, 0, 0);
    }
	
	 public MyJTextArea(int rows, int columns) {
		 this(null, null, rows, columns);
	 }
	 
	 public MyJTextArea(Document doc) {
		 this(doc, null, 0, 0);
	 }
	
	public MyJTextArea(Document doc, String text, int rows, int columns) {
		super(doc,text,rows,columns);
		this.setLineWrap(true);//自动换行
		this.setFont(new Font(null, Font.PLAIN,Constants.Font.TextAREA_FONT_SIZE));
		//文本改变 监听器
		this.getDocument().addUndoableEditListener(new UndoableEditListener() {
					
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				setChange(true);
			}
		});
	}

	public boolean isChange() {
		return isChange;
	}

	public void setChange(boolean isChange) {
		this.isChange = isChange;
	}
}

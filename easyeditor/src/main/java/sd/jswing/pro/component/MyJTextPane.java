package sd.jswing.pro.component;

import java.awt.Font;

import javax.swing.JTextPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import sd.jswing.pro.common.Constants;

public class MyJTextPane extends  JTextPane{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MyJTextPane() {
		this.setFont(new Font(null, Font.PLAIN,Constants.Font.TextAREA_FONT_SIZE));
		//文本改变 监听器
		this.getDocument().addUndoableEditListener(new UndoableEditListener() {
					
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				System.out.println("22222");
				setChange(true);
			}
		});
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
	
	
}

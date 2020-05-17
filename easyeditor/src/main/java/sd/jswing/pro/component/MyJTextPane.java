package sd.jswing.pro.component;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.text.BreakIterator;

import javax.swing.JTextPane;
import javax.swing.SizeRequirements;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.InlineView;
import javax.swing.undo.UndoManager;
import javax.swing.text.html.HTMLEditorKit.HTMLFactory;

import sd.jswing.pro.common.Constants;
import sd.jswing.pro.common.FileInfo;
import sd.jswing.pro.common.FileUtils;

public class MyJTextPane extends  JTextPane{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MyJFrame pFrame;
	
	private LineNr lineNumber;
	
	private class UndoManagerExtend extends UndoManager{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void undoableEditHappened(UndoableEditEvent e) {
			// TODO Auto-generated method stub
			super.undoableEditHappened(e);
			setChange(true);
			pFrame.setTitleChange();
			//System.out.println("可撤销的..");
		}
	}
	
	private UndoManager undoManager;
	
	public UndoManager getUndoManager() {
		return undoManager;
	}
	
	private class WarpEditorKit extends StyledEditorKit {  
	      
        private ViewFactory defaultFactory = new WarpColumnFactory();  
  
        @Override  
        public ViewFactory getViewFactory() {  
            return defaultFactory;  
        }  
    }  
  
    private class WarpColumnFactory implements ViewFactory {  
  
        public View create(Element elem) {  
            String kind = elem.getName();  
            if (kind != null) {  
                if (kind.equals(AbstractDocument.ContentElementName)) {  
                    return new WarpLabelView(elem);  
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {  
                    return new ParagraphView(elem);  
                } else if (kind.equals(AbstractDocument.SectionElementName)) {  
                    return new BoxView(elem, View.Y_AXIS);  
                } else if (kind.equals(StyleConstants.ComponentElementName)) {  
                    return new ComponentView(elem);  
                } else if (kind.equals(StyleConstants.IconElementName)) {  
                    return new WarpIconView(elem);  
                }  
            }  
  
            // default to text display  
            return new LabelView(elem);  
        }  
    }  
  
    private class WarpLabelView extends LabelView {  
  
        public WarpLabelView(Element elem) {  
            super(elem);  
        }  
  
        @Override  
        public float getMinimumSpan(int axis) {  
            switch (axis) {  
                case View.X_AXIS:  
                    return 0;  
                case View.Y_AXIS:  
                    return super.getMinimumSpan(axis);  
                default:  
                    throw new IllegalArgumentException("Invalid axis: " + axis);  
            }
        }  
    }
    
    private class WarpIconView extends IconView {
    	 
        public WarpIconView(Element elem) {
            super(elem);
        }
 
        @Override
        public float getMinimumSpan(int axis) {
            switch (axis) {
                case View.X_AXIS:
                    return 0;
                case View.Y_AXIS:
                    return getPreferredSpan(View.Y_AXIS);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }
    }
	
	public MyJTextPane() {
		WarpEditorKit warpKit = new WarpEditorKit();
		this.setEditorKit(warpKit); 
		this.setFont(new Font(null, Font.PLAIN,Constants.Font.TextAREA_FONT_SIZE));
		//文本改变 监听器
		this.undoManager = new UndoManagerExtend();
		this.getDocument().addUndoableEditListener(this.undoManager);
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

	public MyJFrame getpFrame() {
		return pFrame;
	}

	public void setpFrame(MyJFrame pFrame) {
		this.pFrame = pFrame;
	}
}

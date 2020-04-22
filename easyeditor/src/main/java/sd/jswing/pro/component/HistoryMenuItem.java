package sd.jswing.pro.component;

import javax.swing.JMenuItem;

public class HistoryMenuItem extends JMenuItem{
	
	public HistoryMenuItem(String text) {
		super(text);
	}
	
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	private String index;
	
	
	
}

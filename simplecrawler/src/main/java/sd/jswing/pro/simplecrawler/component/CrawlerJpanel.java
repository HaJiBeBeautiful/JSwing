package sd.jswing.pro.simplecrawler.component;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

public class CrawlerJpanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LeftPanel leftPl;
	
	private RightPanel rightPl;
	
	public CrawlerJpanel() {
		leftPl = new LeftPanel();
		rightPl = new RightPanel();
		this.setLayout(new BorderLayout());
		this.add(leftPl, BorderLayout.WEST);
		this.add(rightPl, BorderLayout.CENTER);
	}
	
}

package sd.jswing.pro.simplecrawler.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
/**
 * 主窗口
 * @author huangjing
 *2020年5月21日
 */
public class CrawlerJframe extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CrawlerJpanel mainPanel;

	public CrawlerJframe(String name) {
		this(name,"/images/icon.jpg");
	}
	
	public CrawlerJframe(String name,String icon) {
		super(name);
		try {
			//設置window風格
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
		}
		this.setSize(new Dimension(1000, 800));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				CrawlerJframe.this.dispose();
			}
		});
		Image image = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(icon));
		this.setIconImage(image);
	}
	
	public void init() {
		mainPanel = new CrawlerJpanel();
		this.setContentPane(mainPanel);
	}
}

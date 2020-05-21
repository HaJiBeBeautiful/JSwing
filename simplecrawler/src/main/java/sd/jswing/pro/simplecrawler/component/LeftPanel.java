package sd.jswing.pro.simplecrawler.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class LeftPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JScrollPane scrollPl;
	
	private CenterPanel centerViewPl;
	
	private TopPanel topPL;
	
	public LeftPanel() {
		this.setPreferredSize(new Dimension(300, 0));
		this.setLayout(new BorderLayout());
		topPL = new TopPanel();
		centerViewPl = new CenterPanel();
		scrollPl = new JScrollPane(centerViewPl,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(topPL, BorderLayout.NORTH);
		this.add(scrollPl,BorderLayout.CENTER);
	}
	
	
	private class CenterPanel extends JPanel{
		
		final Vector<String> showData = new Vector<>(10);
		final JList<String> list = new JList<String>();
		
		public CenterPanel() {
			this.setBackground(Color.WHITE);
			showData.add("推女郎网站爬虫");
			showData.add("推女郎网站爬虫");
			showData.add("推女郎网站爬虫");
			showData.add("推女郎网站爬虫");
			showData.add("推女郎网站爬虫");
			showData.add("推女郎网站爬虫");
			showData.add("推女郎网站爬虫");
			showData.add("推女郎网站爬虫");
			showData.add("推女郎网站爬虫");
			showData.add("推女郎网站爬虫");
			showData.add("推女郎网站爬虫");
			showData.add("推女郎网站爬虫");
			showData.add("推女郎网站爬虫");
			showData.add("推女郎网站爬虫");
			list.setListData(showData);
			list.setPreferredSize(new Dimension(260,1000));
			list.addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					// TODO Auto-generated method stub
					if(e.getValueIsAdjusting()) {
						int[] indexs = list.getSelectedIndices();
						ListModel<String> listModel = list.getModel();
						for(int index : indexs) {
							System.out.println("选中: "+index+" = "+listModel.getElementAt(index));
						}
					}
				}
			});
			this.add(list);
			
		}
		
		public void addModel(String title) {
			showData.add(title);
			list.setListData(showData);
		}
	}
	
	private class TopPanel extends JPanel{
		
		private JButton btn;
		
		public TopPanel() {
			this.setBackground(Color.WHITE);
			btn = new JButton("新增爬虫");
			this.add(btn);
			btn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					centerViewPl.addModel("新增爬虫");
				}
			});
		}
	}
}

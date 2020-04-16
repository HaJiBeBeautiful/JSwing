package sd.jswing.pro;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import sd.jswing.pro.common.Constants;
import sd.jswing.pro.common.DialogUtils;
import sd.jswing.pro.component.MyJFrame;
import sd.jswing.pro.component.MyJTextPane;

/**
 * Hello world!
 *
 */
public class App 
{	
	public static void main(String[] args) {
		//此处处于 主线程 提交任务到 事件调度线程 创建窗口
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//此处属于时间调度线程
				createGUI();			
			}
			
		});
	}
	
	public static void createGUI() {
		
		try {
			//設置window風格
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
		}
		//此处属于 时间调度线程
		MyJFrame jf = new MyJFrame();
		//创建菜单栏
		createMenuBar(jf);
		//创建滚动内容版
		createJScrollAreaText(jf);
		
		jf.setVisible(true);
	}
	
	//创建内容面板
	public static void createJScrollAreaText(MyJFrame pFrame) {
		Image icon = Toolkit.getDefaultToolkit().getImage(pFrame.getClass().getResource("/images/logo.gif"));
		pFrame.setIconImage(icon);
		MyJTextPane text = new MyJTextPane();
		JScrollPane pane = new JScrollPane( 
				text,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, //垂直滚动条的显示策略
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED); //水平滚动条的显示策略
		
		pFrame.setContentPane(pane); //设置到窗口
		pFrame.setjTextPane(text);
		//text.insertIcon(new ImageIcon("E:\\我的照片\\绝对领域\\003.jpg"));
		//添加键盘监听事件
		text.addKeyListener(new KeyListener() {
			//有字符输入触发
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				//System.out.println(e.getKeyCode());
				pFrame.getjTextPane().setChange(true);
			}
			//按键弹起触发
			@Override
			public void keyReleased(KeyEvent e) {
				//System.out.println(e.getKeyCode());
			}
			//按键下压触发
			@Override
			public void keyPressed(KeyEvent e) {
				//System.out.println(e.getKeyCode());
				if (e.getModifiers() == 2) {
					if(e.getKeyCode() == Constants.KeyCode.KEY_S) {
						//System.out.println("CTRL+S......");
						try {
							pFrame.showFileSaveDialog();
						} catch (Exception e2) {
							DialogUtils.alertMessage("新建文件异常", pFrame);
						}
					}else if(e.getKeyCode() == Constants.KeyCode.KEY_V){
						//System.out.println("CTRL+V......");
						//粘貼
						pFrame.pasteClipboard();
					}
				}
				
			}
		});
	}
	

	//创建菜单栏
	public static void createMenuBar(MyJFrame pFrame) {
		//创建一个菜单栏
		JMenuBar menuBar = new JMenuBar();
		pFrame.setJMenuBar(menuBar);
		//创建一级菜单
		JMenu menuFile  = new JMenu("文件(F)");
		menuFile.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		//一级菜单添加到菜单栏
		menuBar.add(menuFile);
		//子菜单添加到一级菜单
		//菜单"文件"的子菜单
		JMenuItem newItem = new JMenuItem("       新建(N)                             Ctrl+N   ");
		newItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		JMenuItem newWindow = new JMenuItem("       新窗口(W)              Ctrl+Shift+N   ");
		newWindow.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		newWindow.addActionListener(new ActionListener() {
			//监听 新窗口
			@Override
			public void actionPerformed(ActionEvent e) {
				//此处处于 主线程 提交任务到 事件调度线程 创建窗口
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//此处属于时间调度线程
						createGUI();			
					}
					
				});
			}
		});
		JMenuItem openItem = new JMenuItem("       打开(O)                             Ctrl+O   ");
		openItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		openItem.addActionListener(new ActionListener() {
			//打开文件
			@Override
			public void actionPerformed(ActionEvent e) {
				JScrollPane jScrollPane =  (JScrollPane)pFrame.getContentPane();
				MyJTextPane text =  (MyJTextPane)jScrollPane.getViewport().getView();
				if(null != text) {
					try {
						pFrame.showFileOpenDialog();
					} catch (FileNotFoundException e1) {
						// TODO: handle exception
						DialogUtils.alertMessage("文件不存在", pFrame);
					}catch (IOException e2) {
						DialogUtils.alertMessage("文件读取异常", pFrame);
					}
				}
			}
		});
		//保存
		JMenuItem saveItem = new JMenuItem("       保存(S)                             Ctrl+S   ");
		saveItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		saveItem.addActionListener(new ActionListener() {
			//监听保存
			@Override
			public void actionPerformed(ActionEvent e) {
				JScrollPane jScrollPane =  (JScrollPane)pFrame.getContentPane();
				MyJTextPane text =  (MyJTextPane)jScrollPane.getViewport().getView();
				if(null != text) {
					try {
						pFrame.showFileSaveDialog();
					} catch (IOException e2) {
						DialogUtils.alertMessage("新建文件异常", pFrame);
					}
				}
			}
		});
		
		JMenuItem anotherItem = new JMenuItem("       另存为(A)               Ctrl+Shift+S   ");
		anotherItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		JMenuItem exitItem = new JMenuItem("       退出");
		exitItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		exitItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pFrame.closeWindow();
			}
		});
		menuFile.add(newItem);
		menuFile.add(newWindow);
		menuFile.add(openItem);
		menuFile.add(saveItem);
		menuFile.add(anotherItem);
		menuFile.addSeparator();
		menuFile.add(exitItem);
				
		JMenu menuEdit  = new JMenu("编辑(E)");
		menuEdit.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		menuBar.add(menuEdit);
		//菜单"编辑"的子菜单
		JMenuItem cancelItem = new JMenuItem("       撤销(U)                             Ctrl+Z   ");
		cancelItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		JMenuItem cutItem = new JMenuItem("       剪切(T)                             Ctrl+X   ");
		cutItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		JMenuItem copyItem = new JMenuItem("       复制(C)                             Ctrl+C   ");
		copyItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		JMenuItem pasteItem = new JMenuItem("       粘贴(P)                             Ctrl+V   ");
		pasteItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		JMenuItem delItem = new JMenuItem("       删除(L)                                  Del   ");
		delItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		menuEdit.add(cancelItem);
		menuEdit.add(cutItem);
		menuEdit.add(copyItem);
		menuEdit.add(pasteItem);
		menuEdit.add(delItem);
		
		JMenu menuFormat  = new JMenu("格式(O)");
		menuFormat.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		menuBar.add(menuFormat);
		//菜单"格式"子菜单
		JMenuItem wLineItem = new JMenuItem("       自动换行(W)                 ");
		wLineItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		JMenuItem fontItem = new JMenuItem("       字体(F)                 ");
		fontItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		menuFormat.add(wLineItem);
		menuFormat.add(fontItem);
		
		JMenu menuView  = new JMenu("查看(V)");
		menuView.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		menuBar.add(menuView);
		//菜单"查看"子菜单
		JMenuItem zItem = new JMenuItem("       缩放(Z)                 ");
		zItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		JMenuItem stateItem = new JMenuItem("       状态栏(S)                 ");
		stateItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		menuView.add(zItem);
		menuView.add(stateItem);
		
		JMenu menuHelp  = new JMenu("帮助(H)");
		menuHelp.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		menuBar.add(menuHelp);
		//菜单"帮助"子菜单
		JMenuItem helpItem = new JMenuItem("       查看帮助(H)                 ");
		helpItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		JMenuItem fItem = new JMenuItem("       发送反馈(F)                 ");
		fItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		JMenuItem aboutItem = new JMenuItem("       关于记事本(A)                 ");
		aboutItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		menuHelp.add(helpItem);
		menuHelp.add(fItem);
		menuHelp.addSeparator();
		menuHelp.add(aboutItem);
		
	
	}
}

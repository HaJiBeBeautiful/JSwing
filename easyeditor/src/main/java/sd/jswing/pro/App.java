package sd.jswing.pro;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import sd.jswing.pro.common.Constants;
import sd.jswing.pro.common.DialogUtils;
import sd.jswing.pro.common.FileUtils;
import sd.jswing.pro.component.MyJTextArea;

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
		//此处属于 时间调度线程
		JFrame jf = new JFrame("记吧笔记本");
		jf.setSize(new Dimension(1000, 800));
		
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jf.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeWindow(jf);
			}
		});
		//创建菜单栏
		createMenuBar(jf);
		//创建滚动内容版
		createJScrollAreaText(jf);
		
		jf.setVisible(true);
	}
	
	//创建内容面板
	public static void createJScrollAreaText(JFrame pFrame) {
		
		MyJTextArea text = new MyJTextArea();
		JScrollPane pane = new JScrollPane( 
				text,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, //垂直滚动条的显示策略
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); //水平滚动条的显示策略
		
		pFrame.setContentPane(pane); //设置到窗口
		//添加键盘监听事件
		text.addKeyListener(new KeyListener() {
			//有字符输入触发
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				//System.out.println(e.getKeyCode());
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
							showFileSaveDialog(text,pFrame);
						} catch (Exception e2) {
							DialogUtils.alertMessage("新建文件异常", pFrame);
						}
					}
				}
				
			}
		});
	}
	
	//保存编辑文本
	public static int showFileSaveDialog(MyJTextArea text,Component parent) throws IOException {
		//创建一个默认的文件选择器
		FileSystemView  fileSystemView = FileSystemView.getFileSystemView();
		JFileChooser fileChooser = new JFileChooser();
		//设置默认打开的路径是 桌面路径
		fileChooser.setCurrentDirectory(fileSystemView.getHomeDirectory()); 
		//设置打开文件选择框后,默认输入的名字
		fileChooser.setSelectedFile(new File("*.note"));
		//只能选文件 不能选文件夹
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		//打开选择框线程被阻塞,直到选择框被关闭
		int result = fileChooser.showSaveDialog(parent);
		if(result == JFileChooser.APPROVE_OPTION) {
			//如果点击了保存，则获取选择的保存路径
			File saveFile = fileChooser.getSelectedFile();
			//System.out.println(saveFile.getAbsolutePath());
			String content = text.getText();
			if(saveFile.exists()) {
				//System.out.println("文件存在");
				if(DialogUtils.showConfirmDialog("文件已存在!是否覆盖该文件", parent) == JOptionPane.NO_OPTION) {
					//否
					return 0;
				}
			}else {
				try {
					saveFile.createNewFile();
				} catch (IOException e) {
					throw e;
				}
			}
			boolean isSucess =  FileUtils.saveFile(saveFile, content);
			if(!isSucess) {
				DialogUtils.alertMessage("保存失败", parent);
				return 0;
			}else {
				text.setChange(false);
				return 1;
			}
		}else {
			return 0;
		}
	}
	
	//打开文件你
	public static int showFileOpenDialog(MyJTextArea textArea,Component parent) throws IOException,FileNotFoundException {
		//创建默认的文件选择器
		JFileChooser fileChooser = new JFileChooser();
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		fileChooser.setCurrentDirectory(fileSystemView.getHomeDirectory());
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.txt,*.note","txt", "note"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt,*.note","txt", "note"));
		// 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"确定", 则获取选择的文件路径
            File file = fileChooser.getSelectedFile();

            // 如果允许选择多个文件, 则通过下面方法获取选择的所有文件
            // File[] files = fileChooser.getSelectedFiles();
            try {
            	
            	//内容处理
            	textArea.setText(FileUtils.openFile(file));
			} catch (FileNotFoundException e1) {
				throw e1;
			}catch (IOException e2) {
				throw e2;
			}
            return 1;
        }else {
        	return 0;
        }
	}
	
	//创建菜单栏
	public static void createMenuBar(JFrame pFrame) {
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
				MyJTextArea text =  (MyJTextArea)jScrollPane.getViewport().getView();
				if(null != text) {
					try {
						showFileOpenDialog(text,pFrame);
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
				MyJTextArea text =  (MyJTextArea)jScrollPane.getViewport().getView();
				if(null != text) {
					try {
						showFileSaveDialog(text,pFrame);
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
				closeWindow(pFrame);
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
	/**
	 * 关闭窗口
	 * @param frame
	 */
	public static void closeWindow(JFrame frame) {
		MyJTextArea jTextArea = (MyJTextArea)((JScrollPane)frame.getContentPane()).getViewport().getView();
		if(jTextArea.isChange()) {
			int result = DialogUtils.showConfirmDialog("是否保存已改变的内容", frame);
			if(result == JOptionPane.YES_OPTION) {
				try {
					int flag =  showFileSaveDialog(jTextArea, frame);
					if(flag == 1) {
						frame.dispose();
					}
				} catch (IOException e) {
					DialogUtils.alertMessage("新建文件异常", frame);
				}
			}else {
				frame.dispose();
			}
		}else {
			frame.dispose();
		}
	}
}

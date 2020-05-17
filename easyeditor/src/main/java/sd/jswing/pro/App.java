package sd.jswing.pro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoManager;

import sd.jswing.pro.common.Constants;
import sd.jswing.pro.common.DialogUtils;
import sd.jswing.pro.common.FileInfo;
import sd.jswing.pro.common.XmlOpenhistoryUtil;
import sd.jswing.pro.component.HistoryMenuItem;
import sd.jswing.pro.component.LineNr;
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
				if(args.length>0) {
					File file = new File(args[0]);
					if(!file.getAbsolutePath().endsWith(".txt")
							&& !file.getAbsolutePath().endsWith(".note")) {
						DialogUtils.alertMessage("打开文件格式不对", null);
					}else {
						createGUI(file);	
					}
				}else {
					createGUI(null);	
				}
			}
			
		});
	}
	
	public static void createGUI(File file) {
		
		try {
			//設置window風格
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
		}
		
		MyJFrame jf = new MyJFrame();
		jf.setOpenHisttoryFile(XmlOpenhistoryUtil.loadXmlFile());
		Image icon = Toolkit.getDefaultToolkit().getImage(jf.getClass().getResource("/images/favicon.jpg"));
		jf.setIconImage(icon);
		//创建菜单栏
		createMenuBar(jf);
		//创建滚动内容版
		jf.setTargetFile(file);
		createJScrollAreaText(jf);
		jf.setVisible(true);
	}
	
	
	//创建内容面板
	public static void createJScrollAreaText(MyJFrame pFrame) {
		MyJTextPane contentPanel = new MyJTextPane();
		JScrollPane scrollPael = new JScrollPane( 
				contentPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, //垂直滚动条的显示策略
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED); //水平滚动条的显示策略
		scrollPael.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1,new Color(200,200,200)));
		JPanel borderPanel = new JPanel(new BorderLayout());
		/*LineNr lineNumber = new LineNr(scrollPael);
		contentPanel.setLineNumber(lineNumber);
		borderPanel.add(lineNumber,BorderLayout.WEST);*/
		borderPanel.add(scrollPael,BorderLayout.CENTER);
		JPanel bottomPl = new JPanel();
		bottomPl.add(new JLabel("底部"));
		borderPanel.add(bottomPl,BorderLayout.SOUTH);
		
		pFrame.setContentPane(borderPanel); //设置到窗口
		pFrame.setjTextPane(contentPanel);
		try {
			pFrame.readTargetFile(pFrame.getTargetFile());
		} catch (IOException e1) {
			DialogUtils.alertMessage("文件读取失败", pFrame);
		}
		//添加键盘监听事件
		contentPanel.addKeyListener(new KeyListener() {
			//有字符输入触发
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				//System.out.println(e.getKeyCode());
				if (e.getModifiers() != 2) {
					pFrame.getjTextPane().setChange(true);
					pFrame.setTitleChange();
				}
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
					if(e.getKeyCode() == Constants.KeyCode.KEY_V){
						//System.out.println("CTRL+V......");
						//粘貼
						pFrame.pasteClipboard(1);
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
		JMenuItem newItem = new JMenuItem("新建(N)");
		newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK));//ctrl+N
		newItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		newItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(pFrame.getFileInfo().isNewCreate()) {
					int result = DialogUtils.showConfirmDialog("是否保存当前文件", pFrame);
					if(result == JOptionPane.YES_OPTION) {
						try {
							pFrame.showFileSaveDialog(Constants.SaveModel.SAVE);
						} catch (IOException e1) {
							DialogUtils.alertMessage("保存文件失败", pFrame);
						}
						return;
					}
				}else if(pFrame.getjTextPane().isChange()) {
					int result = DialogUtils.showConfirmDialog("是否保存当前改变的内容", pFrame);
					if(result == JOptionPane.YES_OPTION) {
						try {
							pFrame.showFileSaveDialog(Constants.SaveModel.SAVE);
						} catch (IOException e1) {
							DialogUtils.alertMessage("保存文件失败", pFrame);
						}
					}
				}
				pFrame.clearContent();
			}
		});
		JMenuItem newWindow = new JMenuItem("新窗口(W)");
		newWindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,InputEvent.CTRL_MASK));//Ctrl+W
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
						createGUI(null);			
					}
					
				});
			}
		});
		JMenuItem openItem = new JMenuItem("打开(O)");
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_MASK));//ctrl+o
		openItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		openItem.addActionListener(new ActionListener() {
			//打开文件
			@Override
			public void actionPerformed(ActionEvent e) {
				MyJTextPane text =  pFrame.getjTextPane();
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
		JMenu history = new JMenu("打开历史 ");
		history.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		List<FileInfo> listFileInfo =  pFrame.getOpenHisttoryFile();
		for (int i = 0; i < listFileInfo.size(); i++) {
			HistoryMenuItem hsItem = new HistoryMenuItem(String.format("   %s      ", listFileInfo.get(i).getFileName()));
			hsItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
			hsItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(pFrame.getFileInfo().isNewCreate()) {
						int result = DialogUtils.showConfirmDialog("是否保存当前文件", pFrame);
						if(result == JOptionPane.YES_OPTION) {
							try {
								pFrame.showFileSaveDialog(Constants.SaveModel.SAVE);
							} catch (IOException e1) {
								DialogUtils.alertMessage("保存文件失败", pFrame);
							}
							return;
						}
					}else if(pFrame.getjTextPane().isChange()) {
						int result = DialogUtils.showConfirmDialog("是否保存当前改变的内容", pFrame);
						if(result == JOptionPane.YES_OPTION) {
							try {
								pFrame.showFileSaveDialog(Constants.SaveModel.SAVE);
							} catch (IOException e1) {
								DialogUtils.alertMessage("保存文件失败", pFrame);
							}
						}
					}
					HistoryMenuItem source = (HistoryMenuItem)e.getSource();
					File sourceFile  = new File(source.getIndex());
					if(sourceFile.exists()) {
						try {
							pFrame.readTargetFile(sourceFile);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else {
						pFrame.removeHistory(source.getIndex());
						DialogUtils.alertMessage("文件不存在", pFrame);
					}
				}
			});
			hsItem.setIndex(listFileInfo.get(i).getFilePath());
			history.add(hsItem);
		}
		menuFile.add(history);
		
		//保存
		JMenuItem saveItem = new JMenuItem("保存(S)");
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK));//Ctrl+S
		saveItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		saveItem.addActionListener(new ActionListener() {
			//监听保存
			@Override
			public void actionPerformed(ActionEvent e) {
				MyJTextPane text =  pFrame.getjTextPane();
				if(null != text) {
					try {
						pFrame.showFileSaveDialog(Constants.SaveModel.SAVE);
					} catch (IOException e2) {
						DialogUtils.alertMessage("新建文件异常", pFrame);
					}
				}
			}
		});
		
		JMenuItem anotherItem = new JMenuItem("另存为(A)");
		anotherItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		anotherItem.addActionListener(new ActionListener() {
			//监听另存为
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					pFrame.showFileSaveDialog(Constants.SaveModel.ANOTHER);
				} catch (IOException e2) {
					DialogUtils.alertMessage("新建文件异常", pFrame);
				}
			}
		});
		JMenuItem exitItem = new JMenuItem("退出                                   ");
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
		menuFile.add(history);
		menuFile.add(saveItem);
		menuFile.add(anotherItem);
		menuFile.addSeparator();
		menuFile.add(exitItem);
				
		JMenu menuEdit  = new JMenu("编辑(E)");
		menuEdit.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		menuBar.add(menuEdit);
		//菜单"编辑"的子菜单
		JMenuItem cancelItem = new JMenuItem("撤销(U)");
		cancelItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,InputEvent.CTRL_MASK));// Ctrl+Z
		cancelItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		cancelItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MyJTextPane text =  pFrame.getjTextPane();
				UndoManager manager = text.getUndoManager();
				if(manager.canUndo()) {
					manager.undo();
				}
			}
		});
		JMenuItem cutItem = new JMenuItem("剪切(T)");
		cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.CTRL_MASK));// Ctrl+X
		cutItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		cutItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MyJTextPane text =  pFrame.getjTextPane();
				if(null != text.getSelectedText()) {
					pFrame.copyClipboard(text.getSelectedText());
					try {
						int p0 = text.getSelectionStart();
						text.getDocument().remove(p0,text.getSelectedText().length());
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		JMenuItem copyItem = new JMenuItem("复制(C)");
		copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_MASK));// Ctrl+C
		copyItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		copyItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MyJTextPane text =  pFrame.getjTextPane();
				if(null != text.getSelectedText()) {
					pFrame.copyClipboard(text.getSelectedText());
				}
			}
		});
		JMenuItem pasteItem = new JMenuItem("粘贴(P)                                  ");
		pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,InputEvent.CTRL_MASK));// Ctrl+V
		pasteItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		pasteItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pFrame.pasteClipboard(2);
			}
		});
		menuEdit.add(cancelItem);
		menuEdit.add(cutItem);
		menuEdit.add(copyItem);
		menuEdit.add(pasteItem);
		
		JMenu menuFormat  = new JMenu("格式(O)");
		menuFormat.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		menuBar.add(menuFormat);
		//菜单"格式"子菜单
		JMenuItem wLineItem = new JMenuItem("     自动换行(W)                   ");
		wLineItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		JMenuItem fontItem = new JMenuItem("     字体(F)                   ");
		fontItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		menuFormat.add(wLineItem);
		menuFormat.add(fontItem);
		
		JMenu menuView  = new JMenu("查看(V)");
		menuView.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		menuBar.add(menuView);
		//菜单"查看"子菜单
		JMenuItem zItem = new JMenuItem("     缩放(Z)                   ");
		zItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		JMenuItem stateItem = new JMenuItem("     状态栏(S)                   ");
		stateItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		menuView.add(zItem);
		menuView.add(stateItem);
		
		JMenu menuHelp  = new JMenu("帮助(H)");
		menuHelp.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		menuBar.add(menuHelp);
		//菜单"帮助"子菜单
		JMenuItem helpItem = new JMenuItem("     查看帮助(H)                 ");
		helpItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		JMenuItem fItem = new JMenuItem("     发送反馈(F)                 ");
		fItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		JMenuItem aboutItem = new JMenuItem("     关于记事本(A)                   ");
		aboutItem.setFont(new Font(null, Font.PLAIN, Constants.Font.MENUBAR_FONT_SIZE));
		menuHelp.add(helpItem);
		menuHelp.add(fItem);
		menuHelp.addSeparator();
		menuHelp.add(aboutItem);
		
	
	}
}

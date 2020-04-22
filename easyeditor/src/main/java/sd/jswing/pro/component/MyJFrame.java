package sd.jswing.pro.component;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import sd.jswing.pro.common.Constants;
import sd.jswing.pro.common.DialogUtils;
import sd.jswing.pro.common.FileInfo;
import sd.jswing.pro.common.FileUtils;
import sd.jswing.pro.common.XmlOpenhistoryUtil;

public class MyJFrame extends JFrame{

	private FileInfo fileInfo;
	
	private static String APP_NAME = "笔记本";
	
	private MyJTextPane jTextPane;
	
	private File targetFile;
	
	private List<FileInfo> openHisttoryFile;
	
	public MyJTextPane getjTextPane() {
		return jTextPane;
	}

	public void setjTextPane(MyJTextPane jTextPane) {
		this.jTextPane = jTextPane;
	}

	public MyJFrame() {
		this(APP_NAME);
	}
	
	public MyJFrame(String name) {
		super(name);
		this.setSize(new Dimension(1000, 800));
		this.setFileInfo(new FileInfo(true));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(MyJFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
		});
	}

	
	public FileInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
		List<FileInfo> fileInfos = getOpenHisttoryFile();
		if(null != fileInfos) {
			boolean cf = false;
			for(FileInfo info :fileInfos) {
				if(info.compareTo(fileInfo) > 0) {
					cf = true;
					break;
				}
			}
			if(!cf) {
				fileInfos.add(fileInfo);
				if(null != fileInfo.getFileName() && null != fileInfo.getFilePath())
				XmlOpenhistoryUtil.writeXmlFile(fileInfo);
			}
		}
	}
	
	//打开文件你
	public int showFileOpenDialog() throws IOException,FileNotFoundException {
		MyJTextPane textArea = this.getjTextPane();
		//打开新文件前 先保存当前文本内容
    	if(textArea.isChange()
    			&& DialogUtils.showConfirmDialog("是否保存文件内容", this) == JOptionPane.YES_OPTION) {
    		this.showFileSaveDialog();
    		return 0;
    	}
    	
		//创建默认的文件选择器
		JFileChooser fileChooser = new JFileChooser();
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		fileChooser.setCurrentDirectory(fileSystemView.getHomeDirectory());
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.txt,*.note","txt", "note"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt,*.note","txt", "note"));
		// 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"确定", 则获取选择的文件路径
            File file = fileChooser.getSelectedFile();
            
            // 如果允许选择多个文件, 则通过下面方法获取选择的所有文件
            // File[] files = fileChooser.getSelectedFiles();
            this.setTitle(file.getName()+" - "+APP_NAME);
          //新建一個跟打開文件對應的FileInfo對象
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(file.getName());
			fileInfo.setEncoding(Constants.ENCODING_UTF_8);
			fileInfo.setFilePath(file.getAbsolutePath());
			fileInfo.setNewCreate(false);
			//fileInfo.setFileContent(textArea.getText());
			fileInfo.setSuffix(fileInfo.getFilePath().substring(fileInfo.getFilePath().lastIndexOf(".")+1));
        	if("txt".equals(fileInfo.getSuffix())) {
        		textArea.setText(FileUtils.openFile(file));
        	}else {
        		FileUtils.readStyledDocumentFromFile(file, this,jTextPane);
        	}
        	this.setFileInfo(fileInfo);
			textArea.setChange(false);
            return 1;
        }else {
        	return 0;
        }
	}
	
	//保存编辑文本
	public int showFileSaveDialog() throws IOException {
		MyJTextPane text = this.getjTextPane();
		FileInfo fileInfo = this.getFileInfo();
		if(fileInfo.isNewCreate()) {
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
			int result = fileChooser.showSaveDialog(this);
			if(result == JFileChooser.APPROVE_OPTION) {
				//如果点击了保存，则获取选择的保存路径
				File saveFile = fileChooser.getSelectedFile();
				//System.out.println(saveFile.getAbsolutePath());
				if(saveFile.exists()) {
					//System.out.println("文件存在");
					if(fileInfo.isNewCreate() 
							&& DialogUtils.showConfirmDialog("文件已存在!是否覆盖该文件", this) == JOptionPane.NO_OPTION) {
						//否
						return 0;
					}
				}else {
					try {
						saveFile.createNewFile();
					} catch (IOException e) {
						if(fileInfo.getFileName() != null) {
							fileInfo.setNewCreate(false);
						}
						throw e;
					}
				}
				fileInfo.setFileName(saveFile.getName());
				fileInfo.setEncoding(Constants.ENCODING_UTF_8);
				fileInfo.setFilePath(saveFile.getAbsolutePath());
//				fileInfo.setFileContent(content);
				fileInfo.setSuffix(fileInfo.getFilePath().substring(fileInfo.getFilePath().lastIndexOf(".")+1));
				if(fileInfo.isNewCreate()) {
					fileInfo.setNewCreate(false);
				}
				text.setChange(false);
				this.setTitle(saveFile.getName()+" - "+APP_NAME);
				if("txt".equals(fileInfo.getSuffix())) {
					FileUtils.saveFile(saveFile, text.getText());
				}else {
					FileUtils.saveStyledDocumentAsFile(saveFile, jTextPane);
				}
				return 1;
			}else {
				if(fileInfo.getFileName() != null) {
					fileInfo.setNewCreate(false);
				}
				return 0;
			}
		}else {
			//直接保存
			File file = new File(fileInfo.getFilePath());
			String content = text.getText();
			String suffix = file.getAbsolutePath().substring(fileInfo.getFilePath().lastIndexOf(".")+1);
			if("txt".equals(suffix)) {
				FileUtils.saveFile(file, content);
			}else {
				FileUtils.saveStyledDocumentAsFile(file,jTextPane);
			}
			text.setChange(false);
			return 1;
		}
	}
	
	
	/**
	 * 粘貼操作
	 * @param jframe
	 */
	public void pasteClipboard() {
		MyJTextPane jTextArea = this.getjTextPane();
		int insertPosition = jTextArea.getCaretPosition();
		// 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
     // 获取剪贴板中的内容
        Transferable trans = clipboard.getContents(null);
        
        if (trans != null) {
            // 判断剪贴板中的内容是否支持文本
            if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    // 获取剪贴板中的文本内容
                    //String text = (String) trans.getTransferData(DataFlavor.stringFlavor);
                    //StyledDocument doc = jTextArea.getStyledDocument();
                    //doc.insertString(insertPosition, text, a);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //是否支持圖片
            else if(trans.isDataFlavorSupported(DataFlavor.imageFlavor)){
            	//System.out.println("圖片");
            	BufferedImage image=null; 
            	try {
					Object clipData = trans.getTransferData(DataFlavor.imageFlavor);
					if(clipData != null) {
						if(clipData instanceof BufferedImage) {
							image = (BufferedImage)clipData;
							//File imagefile = new File(this.getClass().getResource("/").getPath(),"easyeditor/images");
							//if(!imagefile.exists()) {
								//imagefile.mkdirs();
							//}
							//String imageName = UUID.randomUUID().toString()+"."+Constants.PictureFormat.JPG;
							//ImageIO.write(image,Constants.PictureFormat.JPG,new File(imagefile,imageName)); 
							
							//jTextArea.insertIcon(new ImageIcon(imagefile.getPath()+"/"+imageName));
							jTextArea.insertIcon(new ImageIcon(image));
						}
					}
				} catch (UnsupportedFlavorException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            }
            //是否支持文件
            else if(trans.isDataFlavorSupported(DataFlavor.javaFileListFlavor)){
            	//System.out.println("文件");
            	BufferedInputStream inFile = null;
            	BufferedOutputStream outFile = null;
            	try {
					List<File> listFile = (List<File>)trans.getTransferData(DataFlavor.javaFileListFlavor);
					if(listFile.size() == 1) {
						File file = listFile.get(0);
						if(file.getAbsolutePath().endsWith(".txt")) {
							
						}else if(file.getAbsolutePath().endsWith(".note")) {
							
						}else if(file.getAbsolutePath().endsWith(".jpg")
								|| file.getAbsolutePath().endsWith(".png")
								|| file.getAbsolutePath().endsWith(".gif")) {
							inFile = new BufferedInputStream(new FileInputStream(file));
							File imagefile = new File(this.getClass().getResource("/").getPath(),"easyeditor/images");
							if(!imagefile.exists()) {
								imagefile.mkdirs();
							}
							String imageName = UUID.randomUUID().toString()+"."+Constants.PictureFormat.JPG;
							outFile = new BufferedOutputStream(new FileOutputStream(new File(imagefile,imageName)));
							byte[] buffer = new byte[1024];
							int len = -1;
							while((len= inFile.read(buffer, 0, buffer.length)) != -1 ) {
								outFile.write(buffer, 0, buffer.length);
							}
							outFile.flush();
							jTextArea.insertIcon(new ImageIcon(imagefile.getPath()+"/"+imageName));
						}
					}
					
				} catch (UnsupportedFlavorException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					try {
						if(inFile != null)
							inFile.close();
						if(outFile != null)
							outFile.close();
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
            	
            }
        }
	}
	
	/**
	 * 关闭窗口
	 * @param frame
	 */
	public void closeWindow() {
		MyJTextPane jTextArea = this.getjTextPane();
		if(jTextArea.isChange()) {
			int result = DialogUtils.showConfirmDialog("是否保存已改变的内容", this);
			if(result == JOptionPane.YES_OPTION) {
				try {
					int flag =  showFileSaveDialog();
					if(flag == 1) {
						this.dispose();
					}
				} catch (IOException e) {
					DialogUtils.alertMessage("新建文件异常", this);
				}
			}else {
				this.dispose();
			}
		}else {
			this.dispose();
		}
	}

	public File getTargetFile() {
		return targetFile;
	}

	public void setTargetFile(File targetFile){
		this.targetFile = targetFile;
		if(targetFile != null && targetFile.exists()) {
			this.setTitle(targetFile.getName()+" - "+APP_NAME);
		}
	}
	
	public void readTargetFile(File targetFile) throws IOException {
		if(targetFile != null && targetFile.exists()) {
			FileInfo fileInfo = new FileInfo(); 
			fileInfo.setFileName(targetFile.getName());
			fileInfo.setEncoding(Constants.ENCODING_UTF_8);
			fileInfo.setFilePath(targetFile.getAbsolutePath());
//			fileInfo.setFileContent(content);
			fileInfo.setSuffix(fileInfo.getFilePath().substring(fileInfo.getFilePath().lastIndexOf(".")+1));
			fileInfo.setNewCreate(false);
        	if("txt".equals(fileInfo.getSuffix())) {
        		this.getjTextPane().setText(FileUtils.openFile(targetFile));
        	}else {
        		FileUtils.readStyledDocumentFromFile(targetFile, this,this.getjTextPane());
        	}
        	this.getjTextPane().setChange(false);
        	this.setFileInfo(fileInfo);
		}
	}

	public List<FileInfo> getOpenHisttoryFile() {
		if(null == openHisttoryFile)
			return new ArrayList<FileInfo>(0);
		return openHisttoryFile;
	}

	public void setOpenHisttoryFile(List<FileInfo> openHisttoryFile) {
		this.openHisttoryFile = openHisttoryFile;
	}
}

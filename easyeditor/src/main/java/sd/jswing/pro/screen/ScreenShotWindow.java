package sd.jswing.pro.screen;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JWindow;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class ScreenShotWindow extends JWindow{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int orgx,orgy,endx,endy;
	
	/**image的作用:
	 * 1.获取整个屏幕的截图*/
	private BufferedImage image=null;
	private BufferedImage tempImage=null;
	private BufferedImage saveImage=null;
	
	private ToolsWindow tools=null;
	
	
	public ScreenShotWindow() throws AWTException {
		
		//获取默认屏幕设备
		GraphicsEnvironment environment=GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice screen=environment.getDefaultScreenDevice();
		
        //获取屏幕尺寸
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, d.width, d.height);
		//获取屏幕截图
		Robot robot=new Robot(screen);
//		Robot robot=new Robot();
		
//		image=new BufferedImage((int)d.getWidth(),(int)d.getHeight(),BufferedImage.TYPE_4BYTE_ABGR);
		image=robot.createScreenCapture(new Rectangle(0, 0, d.width, d.height));
		
		//设置鼠标敲击的时间监听
		this.addMouseListener(new MouseAdapter() {
			
			//鼠标按下的事件监听
			@Override
			public void mousePressed(MouseEvent e) {
				
				//
				orgx=e.getX();
				orgy=e.getY();
				
				if(tools!=null){
					tools.setVisible(false);
				}
			}
			
			//鼠标抬起的事件监听
			@Override
			public void mouseReleased(MouseEvent e) {

				if(tools==null){
					tools=new ToolsWindow(ScreenShotWindow.this,e.getX(),e.getY());
					
				}else{
					tools.setLocation(e.getX(), e.getY());
				}
				tools.setVisible(true);
				tools.toFront();
				
			}
			
		});
		
		//对于鼠标移动的监听
		this.addMouseMotionListener(new MouseMotionAdapter() {
			
			//鼠标滑动的监听
			//在滑动过程中会被反复调用
			@Override
			public void mouseDragged(MouseEvent e) {

				endx=e.getX();
				endy=e.getY();
				
				//临时图像，用于缓冲屏幕区域放置屏幕闪烁
				Image tempImage2=createImage(ScreenShotWindow.this.getWidth(),ScreenShotWindow.this.getHeight());
				//用于绘图
				Graphics g=tempImage2.getGraphics();
				g.drawImage(tempImage, 0, 0,null);
				
				int x=Math.min(orgx, endx);
				int y=Math.min(orgy, endy);
				
				int width=Math.abs(endx-orgx)+1;
				int height=Math.abs(endy-orgy)+1;
				
				g.setColor(Color.RED);
				//保证图片矩形不被边框覆盖
				g.drawRect(x-1, y-1, width+1, height+1);
				
				//getSubimage(int x,int y,int w,int h)用于返回规定位置中的矩形图像到BufferedImag对象中
				saveImage=image.getSubimage(x, y, width, height);
				//用于画当前图像中的可用图像
				g.drawImage(saveImage, x, y, null);
				
				ScreenShotWindow.this.getGraphics().drawImage(tempImage2,
						0, 0,ScreenShotWindow.this);
	
			}
			
			
		});
		
	}
	
	//重写了绘画的方法
	@Override
	public void paint(Graphics g) {

		//new RescaleOp(float[] scaleFactors, float[] offsets, RenderingHints hints)
		//构造一个具有所希望的缩放因子和偏移量的新 RescaleOp。
		//RescaleOp 是有关图像缩放的类
		//RescaleOp.filter(BufferedImage src,BufferedImage dest)
		//用于对源图像src进行缩放
		RescaleOp ro=new RescaleOp(0.8f,0.8f, null);
		tempImage=ro.filter(image, null);
		g.drawImage(tempImage, 0, 0, this);
			
	}
	
	//保存图像到文件
	public void saveImage() throws IOException{
		JFileChooser jfc=new JFileChooser();
		jfc.setDialogTitle("保存");
		
		//文件过滤器，用户过滤可选择的文件
		FileNameExtensionFilter filter=new FileNameExtensionFilter("JPG", "jpg");
		jfc.setFileFilter(filter);
		
		//初始化一个默认文件(此文件会生成在桌面)
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyymmddHHmmss");
		String filename=sdf.format(new Date());
		
		File filePath=FileSystemView.getFileSystemView().getHomeDirectory();
		File defaultFile=new File(filePath+File.separator+filename+".jpg");
		jfc.setSelectedFile(defaultFile);
		
		int flag=jfc.showSaveDialog(this);
		if(flag==JFileChooser.APPROVE_OPTION){
			File file=jfc.getSelectedFile();
			String path=file.getPath();
			//检查文件后缀，放置用户忘记输入后缀或输入不正确的后缀
			if(!(path.endsWith(".jpg")||path.endsWith("JPG"))){
				path+=".jpg";
			}
			//写入文件
			ImageIO.write(saveImage, "jpg", new File(path));
			System.exit(0);
		}
	}
}

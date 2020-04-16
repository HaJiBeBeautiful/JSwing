package sd.jswing.pro.common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.StyledDocument;

import sd.jswing.pro.component.MyJTextPane;

public class FileUtils {
	
	/**
	 * 保存文件内容
	 * @param outfile
	 * @param content
	 * @return
	 * @throws IOException 
	 */
	public static boolean saveFile(File outfile,String content) throws IOException {
		OutputStreamWriter streamWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			streamWriter = new OutputStreamWriter(new FileOutputStream(outfile),Constants.ENCODING_UTF_8);
			bufferedWriter = new BufferedWriter(streamWriter);
			bufferedWriter.write(content);
			bufferedWriter.flush();
			return true;
		} catch (IOException e) {
			throw e;
		}finally {
			try {
				if(bufferedWriter != null) {
					bufferedWriter.close();
				}
			} catch (IOException e) {
				
			}
		}
	}
	
	public static String openFile(File infile) throws IOException {
		BufferedReader bufferedReader = null;
		StringBuilder sb = new StringBuilder();
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),Constants.ENCODING_UTF_8));
			String line = null;
			while((line = bufferedReader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (IOException e) {
			throw e;
		}finally {
			try {
				if(bufferedReader != null)
					bufferedReader.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return sb.toString();
	}
	
	/**
	 * StyledDocument 對象保存文檔
	 * @param jTextPane
	 * @param out
	 */
	public static void saveStyledDocumentAsFile(File out,MyJTextPane jTextPane) {
		ObjectOutputStream oos = null;
		try {
			StyledDocument doc = (StyledDocument) jTextPane.getDocument();
			FileOutputStream fos = new FileOutputStream(out);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(doc);
			oos.flush();
		} catch (FileNotFoundException e) {
			// TODO: handle exception
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(oos != null)
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
		}
	}
	/**
	 * 從文件中讀取 StyledDocument 對象
	 * @param in
	 * @param frame
	 */
	public static void readStyledDocumentFromFile(File in,JFrame frame,MyJTextPane jTextPane) {
		ObjectInputStream ois = null;
		try {
			FileInputStream fis = new FileInputStream(in);
			ois = new ObjectInputStream(fis);
			StyledDocument doc = (StyledDocument) ois.readObject();
			jTextPane.setStyledDocument(doc);
			frame.validate();
		} catch (Exception e) {
			
		}finally {
			if(ois != null)
				try {
					ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}

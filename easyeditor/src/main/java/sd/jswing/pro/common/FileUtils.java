package sd.jswing.pro.common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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
			streamWriter = new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8");
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
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
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
}

package sd.jswing.pro.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXWriter;
import org.dom4j.io.XMLWriter;

public class XmlOpenhistoryUtil {
	
	private static final String OUT_DIRECT = "/xml";
	
	private static final String XML_PATH = "/openhistory.xml";
	
	private static Document document;
	
	static {
		SAXReader xmlReader = new SAXReader();
		try {
			
			File docPath = new File(XmlOpenhistoryUtil.class.getResource("/").getPath()+OUT_DIRECT);
			if(!docPath.exists()) {
				docPath.mkdir();
			}
			File docFile = new File(docPath,XML_PATH);
			if(!docFile.exists()) {
				document = DocumentHelper.createDocument();
				Element root = DocumentHelper.createElement("files");
				document.setRootElement(root);
				writeDocument(document,docFile);
			}else {
				document = xmlReader.read(docFile);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<FileInfo> loadXmlFile(){
		List<FileInfo> listFile = new ArrayList<FileInfo>();
		List<Node> listFileNode =  document.selectNodes("/files/file");
		for(Node fileNode :listFileNode) {
			Element fileEle = (Element)fileNode;
			Element nameEle = fileEle.element("name");
			Element pathEle = fileEle.element("path");
			FileInfo info = new FileInfo();
			info.setFileName(nameEle.getText().trim());
			info.setFilePath(pathEle.getText().trim());
			listFile.add(info);
		}
		return listFile;
	}
	
	public static void writeXmlFile(FileInfo fileInfo) {
		
		Element root = document.getRootElement();
		//System.out.println(root.getName());
		List<Element> listFilesNode = root.elements();
		Element fileEle = DocumentHelper.createElement("file");
		listFilesNode.add(fileEle);
		Element nameEle = DocumentHelper.createElement("name");
		nameEle.setText(fileInfo.getFileName());
		fileEle.add(nameEle);
		Element pathEle = DocumentHelper.createElement("path");
		pathEle.setText(fileInfo.getFilePath());
		fileEle.add(pathEle);
		//回写xml
		File docFile = new File(XmlOpenhistoryUtil.class.getResource("/").getPath()+OUT_DIRECT+XML_PATH);
		writeDocument(document,docFile);
	}
	
	public static void writeDocument(Document doc,File out) {
		try {
	    	OutputFormat format = OutputFormat.createPrettyPrint();
	    	format.setEncoding(Constants.ENCODING_UTF_8);
		    XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(out),format);
		    xmlWriter.write(doc);
		    xmlWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

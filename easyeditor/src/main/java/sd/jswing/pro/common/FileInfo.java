package sd.jswing.pro.common;
/**
 * 文件信息
 */
public class FileInfo {
	
	public FileInfo() {
	}
	
	public FileInfo(boolean isNewCreate) {
		this.isNewCreate = isNewCreate;
	}

	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 文件所在路径
	 */
	private String filePath;
	/**
	 * 是否新建
	 */
	private boolean isNewCreate;
	/**
	 * 文件内容
	 */
	private String fileContent;
	/**
	 * 文件内容编码格式
	 */
	private String encoding;
	/**
	 * 文件后缀
	 */
	private String suffix;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public boolean isNewCreate() {
		return isNewCreate;
	}
	public void setNewCreate(boolean isNewCreate) {
		this.isNewCreate = isNewCreate;
	}
	public String getFileContent() {
		return fileContent;
	}
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}

package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.dom4j.Document;
import org.dom4j.Element;



public class FtpService {
	private FTPClient ftpClient = null;
	// ftp服务器地址
	private String hostName;
	// ftp服务器默认端口
	public static int defaultport = 21;

	// 登录名
	private String userName;

	// 密码
	private String password;
	private String localPath;

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	// 需要访问的远程目录
	private String remoteDir;

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public static int getDefaultport() {
		return defaultport;
	}

	public static void setDefaultport(int defaultport) {
		FtpService.defaultport = defaultport;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemoteDir() {
		return remoteDir;
	}

	public void setRemoteDir(String remoteDir) {
		this.remoteDir = remoteDir;
	}

	public FtpService() {

	}

	//public static Log log;
public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
	public static void main(String[] args) throws UnsupportedEncodingException {
		

	}

	/**
	 * ftp下载文件
	 * @param url
	 * @param port
	 * @param username
	 * @param password
	 * @param remotePath
	 * @param fileName
	 * @param localPath
	 * @return
	 */
	public static boolean downFile(String url, int port,String username, String password, String remotePath,String localPath) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
	
		try {
			int reply;
			ftp.connect(url, port);
			//如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);//登录
			reply = ftp.getReplyCode();
			System.out.println("reply="+reply);
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(remotePath);//转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for(FTPFile ff:fs){
				if(ff.getName().substring(0, ff.getName().indexOf(".")).equalsIgnoreCase("auditlog")){
					File localFile = new File(localPath+"/"+ff.getName());
					
					OutputStream is = new FileOutputStream(localFile); 
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}
			
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}

	

	/**
	 * 
	 * @param port
	 *            端口号
	 * @param hostName
	 *            主机地址
	 * @param userName
	 * @param password
	 * @param remoteDir
	 *            默认工作目录
	 * @param iszhTimeZone
	 *            :是否中文FTP Server端
	 */
	public FtpService(int port, String hostName, String userName,
			String password, String remoteDir, String localpath,
			boolean iszhTimeZone) {

		this.hostName = hostName;
		this.userName = userName;
		this.password = password;
		this.remoteDir = remoteDir == null ? "" : remoteDir;
		this.ftpClient = new FTPClient();
		this.localPath = localpath;
		if (iszhTimeZone) {
			this.ftpClient.configure(FtpService.Config());
			this.ftpClient.setControlEncoding("UTF-8");

		}
		
		this.login(this.userName,this.password);
		
		// Check rsponse after connection attempt.
        int reply = this.ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            close();
            try {
				throw new java.io.IOException("Can't connect to server '" + hostName + "'");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		// this.changeDir(remoteDir);

		this.setFileType(FTPClient.ASCII_FILE_TYPE);

	}
	

	/**
	 * 设置传输文件的类型[文本文件或者二进制文件]
	 * 
	 * @param fileType
	 *            BINARY_FILE_TYPE,ASCII_FILE_TYPE
	 */
	public void setFileType(int fileType) {
		try {
			ftpClient.setFileType(fileType);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到config
	 * 
	 * @return
	 */
	private static FTPClientConfig Config() {
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
		conf.setRecentDateFormatStr("MM月dd日 HH:mm");
		return conf;
	}

	/**
	 * 登录ftp服务器
	 */
	@SuppressWarnings("static-access")
	public void login(String username,String password) {
		
		try {
			ftpClient.setConnectTimeout(1000*60*3);//设置3分钟超时
			ftpClient.connect(this.hostName, this.defaultport);
			ftpClient.login(this.userName, this.password);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			System.out.println("连接到ftp服务器：" + this.hostName + "成功..开始登录");
//			log.getLogs("连接到ftp服务器：" + this.hostName + "用户名:" + this.userName
//					+ "密码:" + this.getPassword() + "成功..开始登录");
		} catch (Exception e) {
			e.printStackTrace();
//			log.getLogs("连接到ftp服务器：" + this.hostName + "用户名:" + this.userName
//					+ "密码:" + this.getPassword() + "失败");
		}

	}

	/**
	 * 变更工作目录
	 * 
	 * @param remoteDir目录路径
	 */
	public void changeDir(String remoteDir) {
		try {
			this.remoteDir = remoteDir;
			ftpClient.changeWorkingDirectory(remoteDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("变更工作目录为" + remoteDir);
	}

	/**
	 * 返回上一级目录
	 */
	public void toParentDir() {
		try {
			ftpClient.changeToParentDirectory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 列出当前工作目录下的所有文件
	 * 
	 * @return
	 */
	public String[] ListAllFiles() {
		String[] names = this.ListFiles("*");

		System.out.println(names[0]);
		return this.sort(names);
	}

	/**
	 * 列出指定工作目录下的匹配文件
	 * 
	 * @param dir
	 *            工作目录 /.../
	 * @param file_regEx通配符为
	 *            *
	 * @return
	 */
	public String[] ListAllFiles(String dir, String file_regEx) {
		String[] names = this.ListFiles(dir + file_regEx);
		return this.sort(names);
	}

	/**
	 * 冒泡排序字符串（从大到小）
	 * 
	 * @param str_Array
	 * @return
	 */
	public String[] sort(String[] str_Array) {
		if (str_Array == null) {
			throw new NullPointerException("the str_Array 为空");
		}
		String tmp = "";
		for (int i = 0; i < str_Array.length; i++) {
			for (int j = 0; j < str_Array.length - i - 1; j++) {
				if (str_Array[i].compareTo(str_Array[j + 1]) < 0) {
					tmp = str_Array[j];
					str_Array[j] = str_Array[j + 1];
					str_Array[j + 1] = tmp;
				}
			}
		}
		return str_Array;
	}

	/**
	 * 列出匹配文件
	 * 
	 * @param file_regEx
	 *            匹配字符，通配符为*
	 * @return
	 */
	public String[] ListFiles(String file_regEx) {
		try {
			String[] name = ftpClient.listNames(file_regEx);
			if (name == null) {
				return new String[0];
			} else {
				return this.sort(name);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new String[0];
	}

	/**
	 * 得到所有的文件集合
	 * 
	 * @return
	 */
	public FTPFile[] getAllFilesByFtp() {
		FTPFile[] files = null;
		try {
			files = ftpClient.listFiles(remoteDir);
			if (files != null && files.length > 0) {
//				log.getLogs("成功获取" + remoteDir + "下的所有文件");
			} else {
//				log.getLogs("获取" + remoteDir + "下的所有文件失败，文件大小为空");
			}

		} catch (Exception e) {
//			log.getLogs("获取" + remoteDir + "下的所有文件失败");
			// TODO: handle exception
			e.printStackTrace();
		}
		return files;
	}

	/**
	 * 列出匹配文件
	 * 
	 * @param file_regEx
	 *            匹配字符，通配符为*
	 * @return
	 */
	public void getElement() {
		// FTPFile[] files = getAllFilesByFtp();
		Document doc = null;
		try {

			// files = ftpClient.listFiles(remoteDir);
			doc = dom4jdao.getParse("F:/ftptest/auditlogs.20111225094201");
			Element root = doc.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> list = root.elements("log-entry");
			for (Element e : list) {
				Element message = e.element("message");
				System.out.println(message.getText());
			}
			// for(int i = 0; i<files.length;i++ ){
			// if(files[i].isFile())
			// doc = dom4jdao.getParse("F:/ftptest/auditlogs.20111225094201");
			// Element root= doc.getRootElement();
			// List list= root.elements("log-entry ");
			// System.out.println(list.size());
			// }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * 上传文件
	 * 
	 * @param localFilePath
	 *            --本地文件路径+文件名
	 * @param newFileName
	 *            --新文件名
	 */
	public void uploadFile(String localFilePath, String newFileName) {
		BufferedInputStream bis = null;
		File file = new File(localFilePath);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			ftpClient.storeFile(newFileName, fis);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null)
					bis.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 下载服务器上的文件到本地服务器
	 * 
	 * @param localPath
	 *            : 本地下载路径
	 */

	public boolean downloadFile(String remoteFileName, String localFileName) {
		boolean flug = false;
		BufferedOutputStream bos = null;
		
		if(FileUtills.createFile(localFileName)){
			try {
				bos = new BufferedOutputStream(new FileOutputStream(localFileName));
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.enterLocalPassiveMode();
				if(ftpClient.retrieveFile(remoteFileName, bos)){
//					log.getLogs("下载" + localFileName + "成功");
					ftpClient.deleteFile(remoteFileName);
					flug = true;
				}else{
//					log.getLogs("下载" + localFileName + "失败,操作卡死");
				}
				
			} catch (Exception e) {
//				log.getLogs("下载" + remoteFileName + "失败");
				e.printStackTrace();
			} finally {
				try {
					if (bos != null)
						bos.close();
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
return flug;
	}

	/**
	 * 关闭ftp
	 */
	public void close() {
		try {
			if (ftpClient != null) {
				ftpClient.logout();
				ftpClient.disconnect();
			}
//			log.getLogs("FTP连接关闭于:" + sdf.format(new Date()));
			
		} catch (Exception e) {
			e.printStackTrace();
//			log.getLogs("FTP连接关闭失败于:"+sdf.format(new Date()));
		}
	}

}

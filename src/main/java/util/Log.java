package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	

	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat formatter_date = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss ");

	/**
	 * 通过流生成日志
	 * 
	 * @param username
	 */
	public  void getLogs(String context) {
		String content = getLogContent(context);
		String LogsPath = getCreetLogsPath();
		File file = new File(LogsPath);
		if (!file.exists()) {
			file.mkdirs();
		}

		BufferedWriter br = null;
		FileWriter fiw = null;

		File fNew = new File(LogsPath + formatter.format(new Date())+".log");
		if(!fNew.exists()){
			try {
				fNew.createNewFile();
				System.out.println("日志文件生成");
			} catch (IOException e) {
				System.out.println("日志文件生成失败");
				e.printStackTrace();
			}
		}else{
			System.out.println("日志文件已存在");
			System.out.println("日志绝对路径="+fNew.getAbsolutePath());
		}
		//System.out.println(LogsPath + formatter.format(new Date())+".log");
		try {
			fiw = new FileWriter(fNew, true);
			br = new BufferedWriter(fiw);
			br.write(content + "\r\n");
			br.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			fiw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成处理日志
	 * 
	 * @param file
	 * @param logContent
	 * @return
	 */
	public static String getLogContent(String logContent) {
		String login_date = formatter_date.format(new Date());

		logContent = logContent + "-" + login_date + ";";

		return logContent;
	}
public static void main(String[] args) {

	
}
	/**
	 * 生成日志路径
	 * 
	 * @return
	 */
	public static String getCreetLogsPath() {
		String logsPath = "";
		String root = util.getRootPath();
		try {
			root = URLDecoder.decode(root,"UTF-8");
			logsPath = root + "Log/";
			File ff = new File(logsPath);
			if (!ff.isDirectory()) {
				ff.mkdirs();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return logsPath;

	}

}

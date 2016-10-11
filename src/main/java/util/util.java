package util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonValueProcessor;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.Element;
import entity.YDData;
import entity.YDService;
/**
 * 获得访问者ip
 * 
 * @author riverplant
 * 
 */
public class util {

	public static JsonConfig getjsonConfig() {
		JsonConfig jsonConfig = new JsonConfig();
		JsDateJsonValueProcessor jsonDateValueProcessor = new JsDateJsonValueProcessor();
		jsonConfig.registerJsonValueProcessor(Date.class,
				jsonDateValueProcessor);
		return jsonConfig;
	}

	private WebServiceContext wsContext;

	/**
	 * 获取请求来的ips
	 * 
	 * @param request
	 * @return
	 */
	public String getIpAddr() {
		String ipAddress = null;
		MessageContext mc = wsContext.getMessageContext();
		@SuppressWarnings("static-access")
		HttpServletRequest request = (HttpServletRequest) (mc
				.get(mc.SERVLET_REQUEST));
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();

			// 这里主要是获取本机的ip,可有可无
			if (ipAddress.equals("127.0.0.1")
					|| ipAddress.endsWith("0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}

		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		// 或者这样也行,对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		// return
		// ipAddress!=null&&!"".equals(ipAddress)?ipAddress.split(",")[0]:null;
		return ipAddress;
	}

	/**
	 * 通过url获取ip
	 * 
	 * @param url
	 * @return
	 */
	public static String getIpFromUrl(String url) {

		String hostName = url.substring(url.indexOf(":") + 3);
		String ip = hostName.substring(0, hostName.indexOf("/"));
		if (ip.contains(":"))
			ip = ip.substring(0, ip.indexOf(":"));
		return ip;
	}

	public static void getMonitoringIcon4pass(HttpServletRequest request,
			HttpServletResponse response, String port) throws IOException {

		response.setDateHeader("Expires", -1);
		response.setHeader("Cache-Control", "no-cache");
		// 通知客户端以图片的形势打开发送过去的数据
		response.setHeader("Content-Type", "image/jpg");
		String root = getRootPath();
		// 在内存中创建图片
		String dir2 = root + "img/";
		File f = new File(dir2);
		if (!f.isDirectory()) {
			f.mkdir();
		}
		String fileName = "ok.png";
		String Path = dir2 + fileName;
		// System.out.println(Path);
		// 防止servlet文件下载
		response.addHeader("Content-Disposition", "inline;filename=" + fileName);

		BufferedImage image = ImageIO.read(new FileInputStream(Path));
		// 得到随机数字
		// Color c =new Color(211, 233, 241);

		// Color c2 =new Color(90, 147, 176);
		// Font font_st=new Font("宋体",Font.PLAIN,20);
		// Font font_Arial=new Font("宋体",Font.BOLD ,12);
		// Graphics g = image.getGraphics();
		// Graphics2D g2 = (Graphics2D) g;
		// if(port!=null && !"".equalsIgnoreCase(port)){
		// // 设置字体
		// g2.setFont(font_Arial);
		// g2.setColor(c2);
		// g2.drawString(port, 5, 44);
		// }

		// 将写好的数字图片输出给浏览器
		ImageIO.write(image, "png", response.getOutputStream());
	}

	/**
	 * 获取远程连接失败的图标
	 * 
	 * @param arg0
	 * @param response
	 * @param port
	 * @throws IOException
	 */
	public static void getMonitoringIcon4error(HttpServletRequest request,
			HttpServletResponse response, String port) throws IOException {

		response.setDateHeader("Expires", -1);
		response.setHeader("Cache-Control", "no-cache");
		// 通知客户端以图片的形势打开发送过去的数据
		response.setHeader("Content-Type", "image/jpg");
		String root = getRootPath();
		// 在内存中创建图片
		String dir2 = root + "img/";
		File f = new File(dir2);
		if (!f.isDirectory()) {
			f.mkdir();
		}
		String fileName = "error.png";
		String Path = dir2 + fileName;
		// System.out.println(Path);
		// 防止servlet文件下载
		response.addHeader("Content-Disposition", "inline;filename=" + fileName);

		BufferedImage image = ImageIO.read(new FileInputStream(Path));
		// 得到随机数字
		// Color c =new Color(211, 233, 241);

		// Color c2 =new Color(90, 147, 176);
		// Font font_st=new Font("宋体",Font.PLAIN,20);
		// Font font_Arial=new Font("宋体",Font.BOLD ,12);
		// Graphics g = image.getGraphics();
		// Graphics2D g2 = (Graphics2D) g;
		// 设置字体
		// g2.setFont(font_Arial);
		// g2.setColor(c2);
		// g2.drawString(port, 16, 42);
		// 将写好的数字图片输出给浏览器
		ImageIO.write(image, "png", response.getOutputStream());
	}

	public static HashMap<Integer, String> getdicMonoriting() {
		HashMap<Integer, String> dic = new HashMap<Integer, String>();
		dic.put(0, "防火墙设置或者网络问题");
		dic.put(100, "继续请求");
		dic.put(101, "切换协议");
		dic.put(200, "访问成功");
		dic.put(201, "资源已创建");
		dic.put(202, "请求已接收，尚未处理");

		dic.put(203, "非授权信息");
		dic.put(204, "无返回内容");
		dic.put(403, "服务器拒绝请求");
		dic.put(404, "未找到服务");
		dic.put(405, "请求方法禁用");

		dic.put(406, "请求不接收");
		dic.put(407, "需要代理授权");
		dic.put(408, "请求超时");
		dic.put(409, "冲突");
		dic.put(500, "服务器内部错误");
		dic.put(502, "错误网关");
		dic.put(503, "服务不可用");
		dic.put(504, "网关超时");
		dic.put(505, "http 版本不受支持");
		return dic;
	}

	public static HashMap<Integer, String> getYDUserType() {
		HashMap<Integer, String> dic = new HashMap<Integer, String>();
		dic.put(0, "普通用户");
		dic.put(200, "数据供应商");
		dic.put(300, "终端用户");
		dic.put(100, "创新工厂用户");
		dic.put(101, "创新工厂用户");
		dic.put(102, "创新工厂用户");
		dic.put(103, "创新工厂用户");
		dic.put(104, "创新工厂用户");
		return dic;
	}

	/**
	 * 调去远程服务
	 * 
	 * @param request
	 * @param response
	 * @param user
	 * @param filename
	 * @return
	 */

	public static boolean getMonitoringfromWeb(String ur) {
		boolean flag = false;
		try {
			URL url = new URL(ur);// 远程url
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(30000); // 设置连接主机超时（单位：毫秒）
			con.setReadTimeout(30000); // 设置从主机读取数据超时（单位：毫秒）
			if (con.getResponseCode() == 200) {
				flag = true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return flag;
	}

	/**
	 * 获得调去远程服务返回代码
	 * 
	 * @param request
	 * @param response
	 * @param user
	 * @param filename
	 * @return
	 */

	public static Integer getCodefromWeb(String ur) {

		try {
			URL url = new URL(ur);// 远程url
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(30000); // 设置连接主机超时（单位：毫秒）
			con.setReadTimeout(30000); // 设置从主机读取数据超时（单位：毫秒）
			return con.getResponseCode();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return 0;
		} catch (ConnectException e) {
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获得调去远程服务返回代码
	 * 
	 * @param request
	 * @param response
	 * @param user
	 * @param filename
	 * @return
	 */

	public static String getStatutfromWeb(Integer code, Map<Integer, String> map) {

		if (map.containsKey(code)) {
			return map.get(code);
		} else {
			return null;
		}

	}

	/**
	 * 得到文件内容
	 * 
	 * @param f
	 * @return
	 */
	public static String getFileContent(File f) {
		String content = "";
		StringBuilder sb = new StringBuilder(2042);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			sb = new StringBuilder();
			while ((content = br.readLine()) != null) {

				sb.append(content.trim());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 得到DP日志内容
	 * 
	 * @param f
	 * @return
	 */
	public static String getDPLogContent(File f) {

		StringBuilder sb = new StringBuilder(2042);
		int id = 1;
		try {
			Document doc = dom4jdao.getParse(f.getAbsolutePath());
			Element log = doc.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> elements = log.elements("log-entry");
			for (Element e : elements) {
				sb.append(String.valueOf(id++) + "-----" + "date:"
						+ e.element("date").getTextTrim() + " time:"
						+ e.element("time").getTextTrim() + "  level:"
						+ e.element("level").getTextTrim() + "  client:"
						+ e.element("client").getTextTrim() + "  message:"
						+ e.element("message").getTextTrim() + "\n\r");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param sourceFilePath待压缩的文件路径
	 * @param zipFilePath
	 *            压缩后存放路径
	 * @param fileName
	 *            压缩后文件的名称
	 */
	public void fileToZip(ArrayList<File> al, String zipFilePath,
			String fileName) {

		FileInputStream fis = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		if (al == null) {
			System.out.println("没有传递要压缩的文件");
		} else {
			File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
			if (zipFile.exists()) {
				System.out.println(zipFilePath + "目录下存在名为" + fileName + ".zip");
			} else {
				if (al.isEmpty()) {
					System.out.println("待压缩的文件为空");

				} else {
					try {
						fos = new FileOutputStream(zipFile);
						bos = new BufferedOutputStream(fos);
						zos = new ZipOutputStream(bos);
						byte[] bufs = new byte[1024 * 10];
						for (File file : al) {
							// 创建对应文件的zip实体
							ZipEntry zipEntry = new ZipEntry(file.getName());
							// 放入zip包里
							zos.putNextEntry(zipEntry);
							// 将文件放入对应的文件读取流中
							fis = new FileInputStream(file);
							// 将文件流放入输入缓存中
							bis = new BufferedInputStream(fis, 1024 * 10);
							int num = 0;
							while ((num = bis.read(bufs, 0, 1024 * 10)) != -1) {
								zos.write(bufs, 0, num);
							}
						}
						// 文件压缩完毕
					} catch (Exception e) {
						e.printStackTrace();
					} finally {

						try {
							if (bis != null) {
								bis.close();
							}
							if (zos != null) {
								zos.close();
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			}
		}
	}

	

	/**
	 * 得到各分中心发布的数据数量
	 * 
	 * @param vs
	 * @return
	 */
	public static Map<String, Integer> geDataByOrg(List<YDData> vs) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (YDData v : vs) {
			if (v.getProvider() != null) {
				if (map.containsKey(v.getProvider().getUname())) {
					int time = map.get(v.getProvider().getUname());
					map.put(v.getProvider().getUname(), time + 1);
				} else {

					map.put(v.getProvider().getUname(), 1);
				}
			}

		}
		return map;
	}

	/**
	 * 得到各分中心发布的服务数量
	 * 
	 * @param vs
	 * @return
	 */
	public static Map<String, Integer> geServiceByOrg(List<YDService> vs) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (YDService v : vs) {
			if (v.getProvider() != null) {
				if (map.containsKey(v.getProvider().getUname())) {
					int time = map.get(v.getProvider().getUname());
					map.put(v.getProvider().getUname(), time + 1);
				} else {

					map.put(v.getProvider().getUname(), 1);
				}
			}

		}
		return map;
	}

	
	


	public static String initcronExpression() {
		String rootPath = util.getRootPath();
		String path = rootPath + "classes/beans.xml";
		Document doc = dom4jdao.getParse(path);

		Element root = doc.getRootElement();
		String cronExp = "";
		@SuppressWarnings("unchecked")
		List<Element> beans = root.elements("bean");
		for (Element e : beans) {
			if (e.attribute("id").getText().trim().equalsIgnoreCase("doTime")) {
				Element cronExpression = (Element) e.elements("property")
						.get(1);
				cronExp = cronExpression.element("value").getTextTrim();
			}
		}
		return cronExp;
	}

	/*
	 * public static List<String> getLevelList( List<YDVisiterService> vs ){ Map
	 * map = new HashMap(); List<String> sids = new ArrayList();
	 * for(YDVisiterService v : vs){
	 * if(!map.containsKey(v.getService().getsLevel())){
	 * map.put(v.getService().getsLevel(), v);
	 * sids.add(v.getService().getsLevel()); } } return sids; }
	 */
	public static void main(String[] args) throws ParseException {
		String temp = "G:\\apache-tomcat-7.0.65\\webapps\\youedataAudite\\WEB-INF\\templogs\\";
		System.out.println(temp.substring(0, temp.indexOf("WEB-INF\\")));
	}

	/**
	 * 获得某年某月的最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastDayOfMonth = sdf.format(cal.getTime());

		return lastDayOfMonth;
	}

	/**
	 * 获得某年某月的第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getFirstDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String firstDayOfMonth = sdf.format(cal.getTime());

		return firstDayOfMonth;
	}

	/**
	 * 分页
	 * 
	 * @param contents
	 * @param page
	 * @param row
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList getFenyeFiles(List list, int row, int page) {
		ArrayList contents = new ArrayList();
		int minnumber = 0;
		int maxnumber = 0;

		int pageCount = (list.size() % row == 0) ? (list.size() / row) : (list
				.size() / row + 1);
		if (page >= pageCount) {
			minnumber = (page - 1) * row;
			maxnumber = list.size();
		} else {
			minnumber = (page - 1) * row;
			maxnumber = page * row;
		}
		for (int i = minnumber; i < maxnumber; i++) {
			contents.add(list.get(i));
		}
		return contents;

	}

	/**
	 * 读取hitTime
	 * 
	 * @param hitTimer
	 * @param request
	 * @return
	 */
	public static int readHitTimer(String path) {
		File f = new File(path);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// System.out.println("path="+path);
		BufferedReader br = null;
		StringBuffer buffer = null;
		try {
			br = new BufferedReader(new FileReader(path));
			String line = "";
			buffer = new StringBuffer();
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}
			if (!"".equalsIgnoreCase(buffer.toString())) {
				System.out.println("当前数字为:" + buffer.toString());
				return Integer.parseInt(buffer.toString());
			} else {
				System.out.println("当前数字为:0");
				return 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			try {
				br.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 保存hitTime
	 * 
	 * @param hitTimer
	 * @param request
	 * @return
	 */

	public static void saveHitTimer(int hitTimer, String savePath) {

		// 给每一个用户创建自己的文件夹
		OutputStream os = null;
		String hitTime = String.valueOf(hitTimer);
		try {
			File f = new File(savePath);
			if (!f.exists()) {
				f.createNewFile();
			}

			byte bytes[] = new byte[512];
			bytes = hitTime.getBytes(); // 新加的
			int b = hitTime.length(); // 改

			os = new FileOutputStream(savePath);
			os.write(bytes, 0, b);
			os.close();
			System.out.println("当前访问量为" + hitTimer);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				os.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 获得当前系统时间
	 * 
	 * @return
	 */
	public static String getDate() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		return time;
	}

	/**
	 * 获得当前系统时间
	 * 
	 * @return
	 */
	public static String getDate4fle() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(date);
		return time;
	}

	/**
	 * 得到访问用户ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP�?
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}

	/**
	 * 得到浏览次数
	 * 
	 * @return
	 */
	int j = 5;

	public static String getHitIme(int tim) {

		StringBuffer sb = new StringBuffer();
		String timstr = String.valueOf(tim);
		for (int i = 0; i < 9 - timstr.length(); i++) {
			sb.append("0");
		}
		timstr = sb.toString() + timstr;
		return timstr;
	}

	/**
	 * 异步数据输出,使用JSON数据(i.e., {@link JSONArray}, {@link JSONObject})格式.
	 * <p>
	 * 
	 * @param result
	 * @param isSuccess
	 * @author caoxl
	 * @since V1.0 2014-8-10
	 */
	public static void writeJson(Object result, boolean isSuccess,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType("text/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			if (isSuccess) {
				writer.write("{\"success\":true,\"result\":" + result + "}");
			} else {
				writer.write("{\"success\":false"
						+ (result != null ? ",\"result\":" + result + "}" : "}"));
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param result
	 * @author caoxl
	 * @since V1.0 2014-8-10
	 */
	public static void writeJson(Object result, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			response.setContentType("text/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.write(result.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(String result, boolean isSuccess,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType("text/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			if (isSuccess) {
				System.out.println("{\"success\":true,\"result\":" + result
						+ "}");
				writer.write("{\"success\":true,\"result\":" + result + "}");
			} else {
				writer.write("{\"success\":false,\"result\":\"" + result
						+ "\"}");
			}

			writer.flush();
			writer.close();
		} catch (java.io.IOException exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * 获得linux下的绝对路径
	 * 
	 * @return
	 */
	public static String getRootPath() {
		String classpath = util.class.getResource("/").getPath();
		String root = classpath.substring(0, classpath.indexOf("classes"));
		root = root.replace("\\", "/");
		return root;
	}

	/**
	 * 将俱乐部球员列表导出
	 * 
	 * @param list
	 * @param club
	 */
	public static File dategrid2excel4ServiceOntime(
			@SuppressWarnings("rawtypes") List list) {
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook();
		String temp = getRootPath();
		File file = null;
		try {
			temp = URLDecoder.decode(temp, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String desPath = temp + "excel/";
		File f = new File(desPath);
		if (!f.isDirectory()) {
			f.mkdirs();
		}

		try {
			file = new File(desPath + "ServiceOnTimes.xls");

			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				file.delete();
			}
			FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
			XSSFSheet sheet1 = workbook.createSheet(file.getName());
			XSSFRow row0 = sheet1.createRow(0);

			XSSFCell cell_0_0 = row0.createCell(0);
			cell_0_0.setCellValue("序号");
			XSSFCell cell_0_1 = row0.createCell(1);
			cell_0_1.setCellValue("服务id");
			XSSFCell cell_0_2 = row0.createCell(2);
			cell_0_2.setCellValue("服务类型");
			XSSFCell cell_0_3 = row0.createCell(3);
			cell_0_3.setCellValue("服务地址");
			XSSFCell cell_0_4 = row0.createCell(4);
			cell_0_4.setCellValue("累计被访问次数");

			for (int i = 0; i < list.size(); i++) {
				XSSFRow row = sheet1.createRow(i + 1);
				YDService service = (YDService) list.get(i);
				XSSFCell cell0 = row.createCell(0);
				cell0.setCellValue(String.valueOf(service.getId()));
				XSSFCell cell1 = row.createCell(1);
				cell1.setCellValue(service.getSid());
				// cell2.setCellValue(service.getServiceType().getName());
				XSSFCell cell3 = row.createCell(3);
				cell3.setCellValue(service.getSurl());
				XSSFCell cell4 = row.createCell(4);
				cell4.setCellValue(service.getVisitedTime());

			}

			workbook.write(fos);
			fos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return file;
	}

	/**
	 * 获取文件最后修改时间
	 * 
	 * @param filePath
	 */
	public static Date getModifiedTime(String filePath) {
		long time = new File(filePath).lastModified();
		return new Date(time);

	}

	/**
	 * 判断查询条件
	 * 
	 * @param filePath
	 */
	public static boolean isdansintervall(Date start, Date end, Date dateTime) {
		boolean flag = true;
		if (start != null && dateTime.before(start)) {
			flag = false;
		} else if (end != null && dateTime.after(end)) {
			flag = false;
		}
		return flag;
	}
}

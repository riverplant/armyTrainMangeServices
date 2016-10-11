package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;




//import org.apache.axis2.AxisFault;
//import org.dom4j.Element;
//
//import com.audilogs.domain.VisitService;
//import com.ms.websvc.client.CatalogStub;
//import com.ms.websvc.client.CatalogStub.MetadataDocument;
//import com.ms.websvc.client.CatalogStub.Query;
//import com.ms.websvc.server.response.QueryResponse;



public class FileUtills {
	//public static Log log;

/**
 * 创建文件
 * @param destFileName
 * @return
 */
	public static boolean createFile(String destFileName) {
		File file = new File(destFileName);
		if (file.exists()) {
			System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
			return false;
		}
		if (destFileName.endsWith(File.separator)) {
			System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
			return false;
		}
		// 判断目标文件所在的目录是否存在
		if (!file.getParentFile().exists()) {
			// 如果目标文件所在的目录不存在，则创建父目录
			System.out.println("目标文件所在目录不存在，准备创建它！");
			if (!file.getParentFile().mkdirs()) {
				System.out.println("创建目标文件所在目录失败！");
				return false;
			}
		}
		// 创建目标文件
		try {
			if (file.createNewFile()) {
				System.out.println("创建单个文件" + destFileName + "成功！");
				return true;
			} else {
				System.out.println("创建单个文件" + destFileName + "失败！");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out
					.println("创建单个文件" + destFileName + "失败！" + e.getMessage());
			return false;
		}
	}
/**
 * 创建路径
 * @param destDirName
 * @return
 */
	public boolean createDir(String destDirName) {
		String localPath = this.getClass().getResource("/").toString();
		File dir = new File(localPath + "/" + destDirName);
		if (dir.exists()) {
			System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		// 创建目录
		if (dir.mkdirs()) {
			System.out.println("创建目录" + destDirName + "成功！");
			return true;
		} else {
			System.out.println("创建目录" + localPath + destDirName + "失败");
			return false;
		}
	}

//	public String getDir() {
//		String localPath = this.getClass().getResource("/").toString();
//		String path = localPath.substring(0, localPath.lastIndexOf("/"));
//		System.out.println(this.getClass().getClassLoader().getResource("")
//				.getPath());
//		return path.substring(0, path.lastIndexOf("/") + 1);
//	}
	
//	/**
//	 * 
//	 * @param start
//	 * @param end
//	 * @param DatabaseId
//	 * @param diff
//	 * @param out_path
//	 * @throws AxisFault
//	 * @throws RemoteException
//	 */
//		@SuppressWarnings("unused")
//		private static void getDBInfos(int start, int end, String DatabaseId,
//				int diff,String out_path) throws AxisFault, RemoteException {
//		
//			CatalogStub stub = new CatalogStub(
//					"http://59.255.40.11:4888/gateway/services/catalog?wsdl");
//
//			Query query = new Query();
//			CatalogStub.QueryRequest queryRequest = new CatalogStub.QueryRequest();
//
//			/**
//			 * 设置用户名密码、版本协议号
//			 */
//			queryRequest.setUsername("guest");
//			queryRequest.setPassword("guest");
//			queryRequest.setProtocolVersion("3.1");
//
//			/**
//			 * 设置元数据库信息
//			 */
//			CatalogStub.Databases databases = new CatalogStub.Databases();
//			databases.addDatabaseId(DatabaseId);
//			queryRequest.setDatabases(databases);
//			FileOutputStream fs = null;
//			try {
//			for (int i = 0; i < 7; i++) {
//
//				queryRequest.setRecordSetEndPoint(end);
//				queryRequest.setRecordSetStartPoint(start);
//				query.setReq(queryRequest);
//				com.ms.websvc.client.CatalogStub.QueryResponse response = stub.query(query);
//				CatalogStub.QueryResult result = response.get_return();
//				MetadataDocument[] mes = result.getRecordSet().getRecord();
//				
//				for (MetadataDocument me : mes) {
//					
//
//					 System.out.println(me.getData());
//				}
//
//				end += diff;
//				start += diff;
//
//			}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}

	/**
	 * 剪切文件
	 * 
	 * @param localPath
	 *            file:/F:/Workspaces/MyEclipse%2010/AudiLogs/WebRoot/WEB-INF/
	 *            templogs/
	 * @param path
	 *            file:/F:/Workspaces/MyEclipse%2010/AudiLogs/WebRoot/WEB-INF/
	 *            baklogs/20150310
	 */
	public static void removeFiles2(String localPath, String Despath) {
		//Date date = new GregorianCalendar().getTime();
		//java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		//String dateString = df.format(date);
		File ff = new File(Despath);
		if (!ff.isDirectory()) {
			ff.mkdirs();
		}
		File f = new File(localPath);
		File desFile = new File(Despath + f.getName());
		f.renameTo(desFile);
		System.out.println(f.getName() + "备份完毕");
	}


	/**
	 * 剪切文件
	 * 
	 * @param localPath
	 *            file:/F:/Workspaces/MyEclipse%2010/AudiLogs/WebRoot/WEB-INF/
	 *            templogs/
	 * @param path
	 *            file:/F:/Workspaces/MyEclipse%2010/AudiLogs/WebRoot/WEB-INF/
	 *            baklogs/20150310
	 */
	public static void removeFiles(String localPath, String Despath) {
		Date date = new GregorianCalendar().getTime();
		java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String dateString = df.format(date);
		File ff = new File(Despath + dateString);
		if (!ff.isDirectory()) {

			ff.mkdirs();
			// System.out.println("创建路径" + Despath + dateString + "完毕");
		}
		Despath = Despath + dateString + "/";
		File localFiles = new File(localPath);

		File desFile = new File(Despath + localFiles.getName());
		System.out.println("copy path=" + desFile);
		localFiles.renameTo(desFile);
		//log.getLogs(localFiles.getName() + "copy is over");

	}

/**
 * 获得时间类型
 * @param start_time
 * @param end_time
 * @return
 */
	public String getTimeType(String start_time, String end_time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d1 = sdf.parse(start_time);

			Date d2 = sdf.parse(end_time);

			long diff = Math.abs(d1.getTime() - d2.getTime());

			long days = diff / (1000 * 60 * 60 * 24);
			if (days == 0) {
				return "1";
			} else if (days > 0 && days < 31) {
				return "2";
			} else {
				return "3";
			}

		}

		catch (Exception e)

		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void show(File f){
        //声明流对象
        FileInputStream fis = null;                 
        try{
                 //创建流对象
                 fis = new FileInputStream("e:\\a.txt");
                 //读取数据，并将读取到的数据存储到数组中
                 byte[] data = new byte[1024]; //数据存储的数组
                 int i = 0; //当前下标
                 //读取流中的第一个字节数据
                 int n = fis.read();
                 //依次读取后续的数据
                 while(n != -1){ //未到达流的末尾
                          //将有效数据存储到数组中
                          data[i] = (byte)n;
                          //下标增加
                          i++;
                          //读取下一个字节的数据
                           n = fis.read();
                 }
                
                 //解析数据
                 String s = new String(data,0,i);
                 //输出字符串
                 System.out.println(s);
        }catch(Exception e){
                 e.printStackTrace();
        }finally{
                 try{
                          //关闭流，释放资源
                          fis.close();
                 }catch(Exception e){}
        }
}
		
	
	/**
	 * 将日志读取内容存入visit_service表
	 * 
	 * @param fu
	 * @param sdf
	 * @param ftpservice
	 * @param ff
	 * @throws UnsupportedEncodingException
	 */

/*	@SuppressWarnings("static-access")
	private boolean getserviceVisiterByLocalFiles(FileUtills fu,
			SimpleDateFormat sdf, File ff) {
		boolean flag = false;
		Document doc;
		doc = dom4jdao.getParse(ff.getAbsolutePath());
		if (doc != null) {
			log.getLogs(ff.getName() + "file is ok , now parsing...");
			Element root = doc.getRootElement();
			List<Element> entrys = root.elements("log-entry");
			for (Element entry : entrys) {

				Element type = entry.element("type");
				if (type.getTextTrim().equalsIgnoreCase("evtlog")) {
					continue;
				} else {

					Element message = entry.element("message");
					String[] contents = message.getText().split(";");
					VisitService visitService = new VisitService();
					try {
						if (contents.length == 2
								&& contents[0].substring(0,
										contents[0].indexOf(":"))
										.equalsIgnoreCase("DN")) {
							String url = URLDecoder
									.decode(contents[1], "UTF-8");
							log.getLogs(url + "parsing...");
							if (url.endsWith("]")) {
								url = url.substring(url.indexOf("[") + 1,
										url.indexOf("]"));
							} else {
								url = url.substring(url.indexOf("[") + 1);
							}
							System.out.println("url=" + url);
							visitService = fu.getvisitService(fu, sdf, entry,
									contents, url);
							visitService.setServiceUrl(url);
							if (visitService.getServiceCurrName() != null
									&& visitService.getServiceAbstract() != null) {

								visitServiceInter.save(visitService);
								log.getLogs(ff.getName()
										+ "parse is ok,now inserting...");

							} else {
								log.getLogs(ff.getName() + "parse is bad...");
							}

						} else if (contents.length > 2
								&& contents[0].substring(0,
										contents[0].indexOf(":"))
										.equalsIgnoreCase("DN")) {

							String u = "";
							for (int i = 1; i < contents.length; i++) {
								if (i == contents.length - 1) {
									u += URLDecoder
											.decode(contents[i], "UTF-8");
								} else {
									u += URLDecoder.decode(contents[i] + ";",
											"UTF-8");

								}

							}
							log.getLogs(u + "parsing...");
							if (u.endsWith("]")) {
								u = u.substring(u.indexOf("[") + 1,
										u.indexOf("]"));
							} else {
								u = u.substring(u.indexOf("[") + 1);
							}

							visitService = fu.getvisitService(fu, sdf, entry,
									contents, u);
							visitService.setServiceUrl(u);
							if (visitService.getServiceCurrName() != null
									&& visitService.getServiceAbstract() != null) {

								visitServiceInter.save(visitService);
								log.getLogs(ff.getName()
										+ "parse is ok,now inserting...");

							} else {
								log.getLogs(ff.getName() + "parse is bad...");

							}

						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}
			flag = true;
		} else {
			log.getLogs(ff.getName() + "无法解析xml,文件为空或者格式错误");
		}
		return flag;

	}*/

//	public static VisitService getvisitService(FileUtills fu,
//			SimpleDateFormat sdf, Element entry, String[] contents, String url) {
//		VisitService visitService = new VisitService();
//		String[] urls = url.split("/");
//		try {
//			log.getLogContent(url);
//			visitService.setCentertype("测绘分中心");
//			for (String str : urls) {
//
//				if (str.equalsIgnoreCase("MapServer?wsdl")) {
//					visitService.setServiceCurrName("地图服务 ( SOAP )");
//					visitService.setServiceAbstract(fu
//							.getservice_abstract("地图服务 ( SOAP )"));
//					System.out.println(url + "type ( SOAP )");
//					break;
//
//				} else if (str.equalsIgnoreCase("downservice?wsdl")) {
//					System.out.println(url + ":数据下载服务");
//					visitService.setServiceCurrName("数据下载服务");
//					visitService.setServiceAbstract(fu
//							.getservice_abstract("数据下载服务"));
//					break;
//				} else if (str.equalsIgnoreCase("viewservice?wsdl")) {
//					System.out.println(url + ":数据预览服务");
//					visitService.setServiceCurrName("数据预览服务");
//					visitService.setServiceAbstract(fu
//							.getservice_abstract("数据预览服务"));
//					break;
//				} else if (str.equalsIgnoreCase("rest")) {
//					System.out.println(url + ":地图服务( REST )");
//					visitService.setServiceCurrName("地图服务( REST )");
//					visitService.setServiceAbstract(fu
//							.getservice_abstract("地图服务( REST )"));
//					break;
//				} else if (str.equalsIgnoreCase("gridmap")
//						|| str.equalsIgnoreCase("tms")) {
//					//System.out.println(url + ":地图影像服务");
//					visitService.setServiceCurrName("地图影像服务");
//					visitService.setServiceAbstract(fu
//							.getservice_abstract("地图影像服务"));
//					break;
//				} else if (str.equalsIgnoreCase("WMSServer")
//						|| str.equalsIgnoreCase("wms")) {
//					//System.out.println(url + ":网络地图服务( WMS )");
//					visitService.setServiceCurrName("网络地图服务( WMS )");
//					visitService.setServiceAbstract(fu
//							.getservice_abstract("网络地图服务( WMS )"));
//					break;
//				} else if (str.equalsIgnoreCase("ImageServer")) {
//					//System.out.println(url + ":网络覆盖服务（WCS）");
//					visitService.setServiceCurrName("网络覆盖服务（WCS）");
//					visitService.setServiceAbstract(fu
//							.getservice_abstract("网络覆盖服务（WCS）"));
//					break;
//				} else if (str.equalsIgnoreCase("iosservice?wsdl")) {
//					//System.out.println(url + ":信息订单服务");
//					visitService.setServiceCurrName("信息订单服务");
//					visitService.setServiceAbstract(fu
//							.getservice_abstract("信息订单服务"));
//					break;
//				} else if (str.equalsIgnoreCase("weatherReport")) {
//					//System.out.println(url + ":天气预报服务");
//					visitService.setServiceCurrName("天气预报服务");
//					visitService.setServiceAbstract(fu
//							.getservice_abstract("天气预报服务"));
//					break;
//				} else if (str.equalsIgnoreCase("WORLD_WFS")) {
//					//System.out.println(url + ":网络要素服务");
//					visitService.setServiceCurrName("网络要素服务(WFS");
//					visitService.setServiceAbstract(fu
//							.getservice_abstract("网络要素服务( WFS )"));
//					break;
//				}
//			}
//			String ip = entry.element("client").getTextTrim();
//			visitService.setVisIp(ip);
//			visitService.setIpdesc("其他");
//			visitService.setCurrDate(sdf.format(new Date()));
//			String dateString = entry.element("date").getTextTrim();
//
//			SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMM dd yyyy",
//					new Locale("ENGLISH", "CHINA"));
//			Date myDate = sdf2.parse(dateString);
//			String d = sdf.format(new Date());
//			visitService.setServiceTime(sdf.format(myDate) + " "
//					+ entry.element("time").getTextTrim());
//			visitService.setCurrDate(d);
//			visitService.setModifyTime(d);
//			visitService.setQydesc("其它");
//
//			if (contents[1].substring(contents[1].indexOf("[") + 1,
//					contents[1].indexOf("//")).equalsIgnoreCase("http:")) {
//				visitService.setVisUser("匿名用户");
//			} else if (contents[1].substring(contents[1].indexOf("[") + 1,
//					contents[1].indexOf("//")).equalsIgnoreCase("https:")) {
//				visitService.setVisUser("认证用户");
//			}
//		} catch (Exception e) {
//			log.getLogContent("获取visitService失败" + url);
//			e.printStackTrace();
//		}
//		return visitService;
//	}


}

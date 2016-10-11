/*
 *操作mysql数据�?*/
package util;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class SqlHelper {
	private static String driver = "";

	public static Connection getCnn() {
		return cnn;
	}

	public static ResultSet getRs() {
		return rs;
	}

	public static Statement getPs() {
		return ps;
	}

	/*
	 * 针对工具类sqlhelp.java 1.连接数据库的变量都是静�?的，潜在危险在于如果访问量大，可能�?成一些用户等待超�?
	 * ---->把static改成非static,调用时先创建�?��sqlhelp对象，然后再调用相应的方法；！！！！�?
	 * 
	 * 2.
	 */

	private static String url="";
	private static String userid="";
	private static String passwd="";
	private static Connection cnn=null;
	private static ResultSet rs=null;
	private static PreparedStatement ps=null;
	private static CallableStatement cs=null;
	private static Properties pp=null;
	private static InputStream is=null;

	static {
		try {
			 pp= new Properties();

			/*
			 * 使用类加载器！！！！！！！！！！！！！！getClassLoader()因为类加载器读取资源的时候默认的主目录是src目录！！！！�?
			 * ！！！！ 例如如果将文件放在了dao下面的话，就应该写成is=SqlHelper1.class.getClassLoader().
			 * getResourceAsStream("com/jie/dbinfo1.properties");
			 */
			 is=SqlHelper.class.getClassLoader().getResourceAsStream("./database.properties");
			 pp.load(is);
			 driver=pp.getProperty("yy.database.driverClass");
			 url=pp.getProperty("yy.database.url");
			 userid=pp.getProperty("yy.database.username");
			 passwd=pp.getProperty("yy.database.password");
			
			Class.forName(driver);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		} finally {
		}
	};// static

	public static Connection getConnection() {
		try {
			cnn = DriverManager.getConnection(url, userid, passwd);
			cnn.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("连接错误");
		}
		return cnn;
	}
/**
 * 增删改操作
 * @param sql
 * @param parameters
 */
	public static void executeUpdate(String sql, String[] parameters) {
		try {
			cnn = getConnection();
			ps = cnn.prepareStatement(sql);
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);
				}
			}
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());

		} finally {
			close(rs, ps, cnn);
		}
	}
/**
 * 查询
 * @param sql
 * @param parameters
 */
	public static void executeQuery(String sql, String[] parameters) {
		try {
			cnn = getConnection();
			ps = cnn.prepareStatement(sql);

			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);
				}
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				 //System.out.println(rs.getInt("ID")+":"+rs.getString("USERID"));
				 System.out.println(rs.getString("URL"));

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());

		} finally {
			close(rs, ps, cnn);
		}
		System.out.println("ok");
	}
/**
 * 登录验证
 * @param sql
 * @param parameters
 * @return
 */
	public static boolean login(String sql, String[] parameters) {
		boolean juge = false;
		System.out.println(sql);
		try {
			cnn = getConnection();
			ps = cnn.prepareStatement(sql);
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);
					System.out.println("para" + parameters[i]);
				}
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				juge = true;
			} else {
				juge = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException(e.getMessage());
			juge = false;
		} finally {
			close(rs, ps, cnn);
		}
		return juge;
	}

	/**
	 * 查询，返回resultSet
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public static ResultSet getResultSet(String sql, String[] parameters) {
		ResultSet rs1 = null;
		try {
			cnn = getConnection();
			ps = cnn.prepareStatement(sql);

			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);
				}
			}
			rs1 = ps.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException(e.getMessage());
		} finally {
			// close(rs,ps,cnn);
		}

		return rs1;
	}

	/*
	 * 对查询语句升级，做到哪里创建哪里关闭!!!!!!!!!!!!!!!!!!!!!!
	 */
	public static ArrayList<Object[]> getArrayList(String sql,
			String[] parameters) {
		/*
		 * 写成�?��变量更加安全
		 */
		Connection cnn = null;
		PreparedStatement ps = null;
		ResultSet rs1 = null;
		ArrayList<Object[]> al = null;
		try {
			cnn = getConnection();
			ps = cnn.prepareStatement(sql);

			if (parameters != null && !parameters.equals("")) {
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);// 给问�?
				}
			}
			rs1 = ps.executeQuery();
			al = new ArrayList<Object[]>();
			ResultSetMetaData rsmd = rs1.getMetaData();
			int colnum = rsmd.getColumnCount();// 得到查询语句会得到多�?
			while (rs1.next()) {
				System.out.println(rs1.getInt(1));
				Object[] ob = new Object[colnum];// 对象数组。表示一行数�?
				for (int i = 1; i <= colnum; i++) {// 将rs这一行中的所有列取出来撞到一个object数组中去
					ob[i - 1] = rs1.getObject(i);
				}
				al.add(ob);
			}
			return al;
		} catch (Exception e) {
			System.out.println("查询结果失败");
			e.printStackTrace();
			return null;
		} finally {
			close(rs1, ps, cnn);
		}

	}
/**
 * 过程查询操作
 * @param sql
 * @param parameters
 */
	public static void callpro1(String sql, String[] parameters) {
		try {
			cnn = getConnection();
			cs = cnn.prepareCall(sql);

			if (parameters != null && !"".equals(parameters)) {
				for (int i = 0; i < parameters.length; i++) {
					cs.setObject(i + 1, parameters[i]);
				}
			}
			// 鎵ц銆�
			cs.execute();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
			// TODO: handle exception
		} finally {
			close(rs, cs, cnn);
		}
	}

	// /*杈撳叆涓�釜鏁扮粍�?楃涓诧紝寰�?��杈撳嚭鐨勪竴涓暟缁勫瓧绗︿覆锛侊紒锛侊紒锛侊紒锛侊紒锛侊紒锛侊紒锛�?*
	// out=Integer[]out={oracle.jdbc.OracleTye.cursor}
	// */
	// public static CallableStatement callpro2
	// (String sql ,String[]inparas,String[]outparas){
	// try {
	// cnn=getConnection();
	// cs=cnn.prepareCall(sql);
	// if(inparas!=null){
	// for(int i=0;i<inparas.length;i++){
	// cs.setObject(i+1, inparas[i]);
	// }
	//
	// }
	// if(outparas!=null){
	// for(int i=0;i<outparas.length;i++){
	// //杈撳嚭鍊奸渶瑕佹敞鍐岋紝鍙傛暟娉ㄥ唽浣嶇疆鍦ㄨ緭鍏ュ弬鏁板悗
	// cs.registerOutParameter(inparas.length+1+i,oracle.jdbc.OracleTypes.VARCHAR
	// );
	// }
	// }
	// cs.execute();
	// for(int i=0;i<inparas.length+1+outparas.length;i++){
	// String tem=cs.getString(i);
	// System.out.println(tem);
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new RuntimeException(e.getMessage());
	// // TODO: handle exception
	// }finally{
	//
	// }
	// return cs;
	// }
/**
 * 关闭
 * @param rs
 * @param ps
 * @param cnn
 */
	public static void close(ResultSet rs, PreparedStatement ps, Connection cnn) {

		if (rs != null)
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		if (ps != null)
			try {
				ps.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		ps = null;

		if (cnn != null) {
			try {
				cnn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cnn = null;
		}
	}
/**
 * 得到一共有多少行
 * @param tableName
 * @return
 */
	public static int getrowCount(String tableName) {
		String sql = "select count(*)from " + tableName;
		int rowCount = 0;
		try {
			cnn = getConnection();
			cnn.setAutoCommit(false);
			ps = cnn.prepareStatement(sql);
			rs = ps.executeQuery();// 目前处于表头
			rs.next();// 移到
			rowCount = rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs, ps, cnn);
		}
		return rowCount;
	}
/**
 * 得到一共有多少页
 * @param tableName
 * @param pageSize
 * @return
 */
	public static int getpageCount(String tableName, int pageSize) {
		int pageCount = 0;
		int rowCount = 0;
		rowCount = SqlHelper.getrowCount(tableName);
		pageCount = (rowCount - 1) / pageSize + 1;
		return pageCount;

	}
/**
 * 增删改双位数组
 * @param sql
 * @param parameters
 */
	public static void executeUpdate(String[] sql, String[][] parameters) {
		try {

			cnn = getConnection();
			cnn.setAutoCommit(false);
			if (sql != null) {

				for (int i = 0; i < sql.length; i++) {

					if (parameters[i] != null) {
						ps = cnn.prepareStatement(sql[i]);
						// System.out.println(sql[i]);
						for (int j = 0; j < parameters[i].length; j++) {
							ps.setString(j + 1, parameters[i][j]);

						}
						ps.executeUpdate();
					} else {
						System.out.println("");
					}
				}
			} else {
				System.out.println("");
			}

			cnn.commit();
			System.out.println("");

		} catch (Exception e) {
			e.printStackTrace();
			try {
				cnn.rollback();
				System.out.println("");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new RuntimeException(e.getMessage());
		}
		close(rs, ps, cnn);
	}
/**
 * 得到sql语句
 * @param id
 * @param name
 * @param tableName
 * @return
 */
	public static String getsql(String id, String name, String tableName) {
		String sql = "";
		if ("".equals(name)) {
			sql = "select * from" + tableName + " where deptno=?";
		} else if ("".equals(id)) {
			sql = "select * from" + tableName + " where ename=?";
		} else {
			sql = "select * from" + tableName + " where empno=? and ename=?";
		}
		return sql;
	}
/**
 * 得到分页，返回resultset
 * @param tableName
 * @param pageSize
 * @param pageNow
 * @return
 */
	public static ResultSet getfenye(String tableName, int pageSize, int pageNow) {
		String sql = "select e2.* from (select rownum run,e1.* from (select * from "
				+ tableName
				+ " order by id ) e1 where rownum <="
				+ pageNow
				* pageSize + ")e2 where run>=" + ((pageNow - 1) * pageSize + 1);
		// System.out.println("aaaa"+sql);
		ResultSet rs1 = null;

		try {
			cnn = getConnection();
			ps = cnn.prepareStatement(sql);
			rs1 = ps.executeQuery();
			// System.out.print("查询成功");

		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException(e.getMessage());
		}

		return rs1;
	}
/**
 * 得到分页，返回arrayList
 * @param tableName
 * @param pageSize
 * @param pageNow
 * @return
 */
	public static ArrayList<Object[]> getfenye2(String tableName, int pageSize,
			int pageNow) {
		String sql = "select e2.* from (select rownum run,e1.* from (select * from "
				+ tableName
				+ " order by id ) e1 where rownum <="
				+ pageNow
				* pageSize + ")e2 where run>=" + ((pageNow - 1) * pageSize + 1);
		// System.out.println("aaaa"+sql);
		ResultSet rs1 = null;
		ArrayList<Object[]> list = null;
		try {
			cnn = getConnection();
			ps = cnn.prepareStatement(sql);
			rs1 = ps.executeQuery();
			// System.out.print("查询成功");
			list = new ArrayList<Object[]>();
			ResultSetMetaData rsmd = rs1.getMetaData();
			int colnum = rsmd.getColumnCount();// 得到查询语句会得到多少列
			while (rs.next()) {
				Object[] ob = new Object[colnum];// 对象数组。表示一行数�?
				for (int i = 1; i <= colnum; i++) {// 将rs这一行中的所有列取出来撞到一个object数组中去
					ob[i - 1] = rs.getObject(i);
				}
				list.add(ob);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			close(rs, ps, cnn);
		}

	}
public static void main(String[] args) {
	String sql = "select * from ASPENBEHAVE";
	executeQuery(sql,null);
}
}
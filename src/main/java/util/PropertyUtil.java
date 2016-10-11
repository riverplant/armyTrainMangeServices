package util;

import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
	//private Properties p = null;
	public synchronized static Properties initP(String propertyName)  {  
        //if (p == null) {  
		Properties p = new Properties();  
        try {
        	 InputStream inputstream = PropertyUtil.class.getClassLoader().getResourceAsStream(propertyName);  
             if (inputstream == null) {  
                 throw new Exception("inputstream " + propertyName   + " open null");  
             }  
             p.load(inputstream);  
             inputstream.close();  
             inputstream = null;  
		} catch (Exception e) {
			e.printStackTrace();
		}
       
       // } 
        return p;
    }  
	
	public static String getValueByKey(String propertyName, String key) {  
        String result = "";  
        try {  
        	Properties p  =  initP(propertyName);  
            result = p.getProperty(key);  
            return result;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return "";  
        }  
    }  
	
	
//	
//	public static void main(String[] args) {
//		System.out.println(getValueByKey("./catalog.properties","yd.DatabaseId"));
//		String[]results = getValueByKey("./catalog.properties","yd.DatabaseId").split(",");
//		
//	}
}

package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonDateValueProcessor implements JsonValueProcessor {
	
private String format = "yyyy-MM-dd HH:mm:ss";
public String getFormat() {
	return format;
}
public void setFormat(String format) {
	this.format = format;
}
public JsonDateValueProcessor(){
	super();
}
public JsonDateValueProcessor(String format){
	super();
	if(format != null && "".equals(format))
	this.format = format;
}
	@Override
	public Object processArrayValue(Object arg0, JsonConfig arg1) {
		// TODO Auto-generated method stub
		return process(arg0);
	}

	@Override
	public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
		// TODO Auto-generated method stub
		return process(arg1);
	}
	
	private Object process(Object value){
		try {
			if( value instanceof Date){
				SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.CHINA);
				return sdf.format(value);
			}
			return value == null ? "" : value.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
	}

}

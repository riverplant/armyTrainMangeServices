package util;

import java.security.MessageDigest;

public class md5Encoding {
	public static void main(String[] args) {
		System.out.println(getMd5Encrypter("YEDAPIPUB001"));
	}
	public static String getMd5Encrypter(String password){
		if(password==null){
			return null;
		}else{
			//a1f925a7b5b70b7b3f7fe2208513e10f
//			String value=null;
//			MessageDigest md5=null;
//			try {
//				md5=MessageDigest.getInstance("MD5");
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			BASE64Encoder baseEncoder=new BASE64Encoder();
//			
//			
//			try {
//				
//				value=baseEncoder.encode(md5.digest(password.getBytes("utf-8")));
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println("��������:"+value);
//			return value;
			char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
			try {
				byte[]strTemp=password.getBytes();
				MessageDigest mdTemp=MessageDigest.getInstance("MD5");
				mdTemp.update(strTemp);
				byte[]md=mdTemp.digest();
				int j=md.length;
				char str[]=new char[j*2];
				int k=0;
				for(int i=0;i<j;i++){
					byte byte0=md[i];
					str[k++]=hexDigits[byte0>>>4 & 0xf];
					str[k++]=hexDigits[byte0 & 0xf];
				}
				return new String(str);
			} catch (Exception e) {
				return null;
			}
		}
		
	}



	}



package util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.*;
import org.dom4j.io.*;
public class dom4jdao {
	
/**
 * 添加预览图
 * @param path
 */
	@SuppressWarnings("unused")
	private static void addElementInXml(String path) {
		File file =new File(path);
		File[] files = file.listFiles();
		for(int i =0;i< files.length;i++){
			File f = files[i];
			Document doc = getParse (f.getPath());
			addElement(doc , f.getName());
		}
	}
	/**
	 * 添加下载
	 * @param path
	 */
	@SuppressWarnings("unused")
	private static void addElementfordownInXml(String path) {
		File file =new File(path);
		File[] files = file.listFiles();
		for(int i =0;i< files.length;i++){
			File f = files[i];
			Document doc = getParse (f.getPath());
			adddownloadElement(doc , f.getName());
		}
	}
	public static void main(String[] args) {
		String path = "G:\\apache-tomcat-6.0.33\\webapps\\AudiLogs\\WEB-INF\\templogs\\auditlog.20120710131315";
		getParse( path);
	}

public static Document getParse(String path){
	Document doc=null;//此处的document是dom4j定义的document,要引入dom4j.document
	//1.得到dom4j解析器
	SAXReader saxReader=new SAXReader();
	//2.指定解析的文件
	File file = new File(path);
	try {
		doc=saxReader.read(file);
		return doc;
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println(file.getAbsolutePath()+"文件格式错误");
		return null;
	} 
	
	
	
}

/*
 * 遍历xml,为空值填上空
 * 
 */
public static void list(Element e){//Element 为dom4j中定义的
	//System.out.println(e.getName()+":"+e.getTextTrim());//直接得到节点和节点的值！！！！！！！！
	if(e.getText().equals("")){
		e.setText("无");
	}
	Iterator<?> iter=e.elementIterator();//得到e的子孩子！！！！！！
	while(iter.hasNext()){
		Element e2=(Element) iter.next();
		//递归得到其他的子孩子
		list(e2);
	}
}
/*
 * 修改安全级别
 */

public static void setSL(Document doc){
	Element root=doc.getRootElement();
	Element FeatureTypeInfo=root.element("FeatureTypeInfo");
	Element FeatureType=FeatureTypeInfo.element("FeatureType");
	Element FeatureTypeSummary=FeatureType.element("FeatureTypeSummary");
	Element securityLevel=FeatureTypeSummary.element("securityLevel");
	if(securityLevel.getText().equals("无")||securityLevel.getText().equals("")){
		securityLevel.setText("公开");
	}
}


/*
 * 修改日期
 */
public static void setDate(Document doc){
	Element root=doc.getRootElement();
	Element FC_Summary=root.element("FC_Summary");
	Element versionDate=FC_Summary.element("versionDate");
	String []dates=versionDate.getText().split("-");
		for(int i=0;i<dates.length;i++){
			if(Integer.parseInt(dates[i])<10&&!dates[i].startsWith("0")){
				dates[i]="0"+dates[i];
				
			}
		}
		versionDate.setText(dates[0]+"-"+dates[1]+"-"+dates[2]);
	
}

public static void setcardinality(Document doc){
	Element root=doc.getRootElement();
	Element FeatureTypeInfo=root.element("FeatureTypeInfo");
	Element FeatureType=FeatureTypeInfo.element("FeatureType");
	Element FeatureAssociationInfo=FeatureType.element("FeatureAssociationInfo");
	Element FeatureAssociation=FeatureAssociationInfo.element("FeatureAssociation");
	Element cardinality=FeatureAssociation.element("cardinality");
	if(cardinality.getText().equals("无")||cardinality.getText().equals("")){
		cardinality.setText("1:1");
	}
}
/*
 * 遍历xml
 * 
 */
public static Element listXML(Element e,String name){//Element 为dom4j中定义的
	Iterator<?> iter=e.elementIterator();//得到e的子孩子！！！！！！
	Element e3=null;
	while(iter.hasNext()){
		
		Element e2=(Element) iter.next();
		if(e2.element("name").getTextTrim().equals(name)){
			e3=e2;	
			break;
		}else{
			//递归得到其他的子孩子
			list(e2);
		}
		
	}
	if(e3!=null){
		return e3;
	}else{
		System.out.println("查询失败");
		return null;
	}
}

/*
 * 更新元素
 */
public static void update(Document doc){
//得到所有学生
	@SuppressWarnings("unchecked")
	List<Element> stus= doc.getRootElement().elements("stu");//util.list
	for(Element e1:stus ){//从e1中取出年龄并且修改
		Element age=e1.element("年龄");
		age.setText((Integer.parseInt(age.getText())+3)+"");
		//age.attributeValue("属性名字", "要改的属性值");
		//age.setAttributeValue("属性名字", "要改的属性值");
		age.addAttribute("要添加或者跟新的属性名", "属性值");
	}
	//reload(doc);
}


/*
 * 添加数据下载服务
 */
public static void adddownloadElement(Document doc, String fileOlderName){
	
	//创建节点对象
	Element SerIdent=DocumentHelper.createElement("SerIdent");
	Element resIdent = DocumentHelper.createElement("resIdent");
	
	Element resTitle=DocumentHelper.createElement("resTitle");
	resTitle.setText("主中心数据预览");
	Element resRefDate=DocumentHelper.createElement("resRefDate");
	Element refDate=DocumentHelper.createElement("refDate");
	refDate.setText("2012-12-10");
	Element refDateType=DocumentHelper.createElement("refDateType");
	refDateType.setText("其他");
	Element proId=DocumentHelper.createElement("proId");
	proId.setText("ndrcviewservice");
	
	Element idAbs=DocumentHelper.createElement("idAbs");
	idAbs.setText("服务描述");
	
	Element resMaint=DocumentHelper.createElement("resMaint");
	
	Element mainFreq=DocumentHelper.createElement("mainFreq");
	mainFreq.setText("不固定");
	
	Element resConst=DocumentHelper.createElement("resConst");
	
	
	Element Consts=DocumentHelper.createElement("Consts");
	
	
	Element useLimit=DocumentHelper.createElement("useLimit");
	useLimit.setText("用于自然资源和地理空间基础信息库项目");
	
	Element serCat=DocumentHelper.createElement("serCat");
	serCat.setText("数据下载服务");
	
	//把子节点挂载到SerIdent下面
	resRefDate.add(refDate);resRefDate.add(refDateType);
	resIdent.add(resTitle);resIdent.add(resRefDate);
	resMaint.add(mainFreq);
	Consts.add(useLimit);resConst.add(Consts);
	
	SerIdent.add(resIdent);SerIdent.add(proId);SerIdent.add(idAbs);
	SerIdent.add(resMaint);SerIdent.add(resConst);SerIdent.add(serCat);
	
	
	//把newStu节点加到根元素下面
	doc.getRootElement().element("dataIdInfo").add(SerIdent);
	//因为直接输出会出现中文乱码
	
	//跟新xml文件达到添加的最后目的
	reload (doc,fileOlderName);
	System.out.println("添加完成");
	
}
/*
 * 添加数据预览服务
 */
public static void addElement(Document doc, String fileOlderName){
	
	//创建节点对象
	Element SerIdent=DocumentHelper.createElement("SerIdent");
	Element resIdent = DocumentHelper.createElement("resIdent");
	
	Element resTitle=DocumentHelper.createElement("resTitle");
	resTitle.setText("主中心数据预览");
	Element resRefDate=DocumentHelper.createElement("resRefDate");
	Element refDate=DocumentHelper.createElement("refDate");
	refDate.setText("2012-12-10");
	Element refDateType=DocumentHelper.createElement("refDateType");
	refDateType.setText("其他");
	Element proId=DocumentHelper.createElement("proId");
	proId.setText("ndrcviewservice");
	
	Element idAbs=DocumentHelper.createElement("idAbs");
	idAbs.setText("服务描述");
	
	Element resMaint=DocumentHelper.createElement("resMaint");
	
	Element mainFreq=DocumentHelper.createElement("mainFreq");
	mainFreq.setText("不固定");
	
	Element resConst=DocumentHelper.createElement("resConst");
	
	
	Element Consts=DocumentHelper.createElement("Consts");
	
	
	Element useLimit=DocumentHelper.createElement("useLimit");
	useLimit.setText("用于自然资源和地理空间基础信息库项目");
	
	Element serCat=DocumentHelper.createElement("serCat");
	serCat.setText("数据预览服务");
	
	//把子节点挂载到SerIdent下面
	resRefDate.add(refDate);resRefDate.add(refDateType);
	resIdent.add(resTitle);resIdent.add(resRefDate);
	resMaint.add(mainFreq);
	Consts.add(useLimit);resConst.add(Consts);
	
	SerIdent.add(resIdent);SerIdent.add(proId);SerIdent.add(idAbs);
	SerIdent.add(resMaint);SerIdent.add(resConst);SerIdent.add(serCat);
	
	
	//把newStu节点加到根元素下面
	doc.getRootElement().element("dataIdInfo").add(SerIdent);
	//因为直接输出会出现中文乱码
	
	//跟新xml文件达到添加的最后目的
	reload (doc,fileOlderName);
	System.out.println("添加完成");
	
}
/*
 * 添加一个图片节点
 */
public static void addImage(Document doc,int i){
	
	//创建学生节点对象
	Element newImg=DocumentHelper.createElement("img"+i);
	String content="images/pageImage/"+i+".data";
	newImg.setText(content);
	
	
	//把newStu节点加到根元素下面
	doc.getRootElement().add(newImg);
	//因为直接输出会出现中文乱码
	//System.out.println(doc.getRootElement().element("img"+(i-1)).getText());
	//跟新xml文件达到添加的最后目的
	//reload (doc);
System.out.println("添加第"+i+"个节点完成");
	
}
/*
 * 删除元素
 */
public static void del(Document doc){
	Element stu=doc.getRootElement().element("学生");
	stu.getParent().remove(stu);
	//更新
	//reload(doc);
}

/*
 * 删除属性
 * 
 */
public static void delAttribue(Document doc,String elementName,String attr){
	Element stu=doc.getRootElement().element(elementName);
	stu.remove(stu.attribute(attr));
	//reload(doc);
}

//跟新xml文件
public static void reload(Document doc,String fileOldName){
	XMLWriter writer=null;
	OutputFormat format=null;
	
	//跟新xml文件达到添加的最后目的
		try {
			 format=OutputFormat.createPrettyPrint();//输出的形式带有换行
			//format=OutputFormat.createCompactFormat();//输出的不带有换行
			format.setEncoding("UTF-8");
			//writer=new XMLWriter(new FileWriter("src/com/jie/dom4j/MyXml3.xml"),format);
			writer=new XMLWriter(new FileOutputStream(fileOldName),format);
			writer.write(doc);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
}

//跟新xml文件
public static void reloadavecpath(Document doc,String path){
	XMLWriter writer=null;
	OutputFormat format=null;
	
	//跟新xml文件达到添加的最后目的
		try {
			 format=OutputFormat.createPrettyPrint();//输出的形式带有换行
			//format=OutputFormat.createCompactFormat();//输出的不带有换行
			format.setEncoding("utf-8");
			//writer=new XMLWriter(new FileWriter("src/com/jie/dom4j/MyXml3.xml"),format);
			writer=new XMLWriter(new FileOutputStream(path+System.currentTimeMillis()+".xml"),format);
			writer.write(doc);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
}

//跟新xml文件
public static void reloadbyId(Document doc,String name){
	XMLWriter writer=null;
	OutputFormat format=null;
	
	//跟新xml文件达到添加的最后目的
		try {
			 format=OutputFormat.createPrettyPrint();//输出的形式带有换行
			//format=OutputFormat.createCompactFormat();//输出的不带有换行
			format.setEncoding("utf-8");
			//writer=new XMLWriter(new FileWriter("src/com/jie/dom4j/MyXml3.xml"),format);
			writer=new XMLWriter(new FileOutputStream(""+name+System.currentTimeMillis()+".xml"),format);
			writer.write(doc);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
}
/*
 * 指定插叙某个元素（读取第一个学生的信息）
 */
public static void read(Document doc){
	Element root=doc.getRootElement();//得到根元素
	//root.elements();//取出root的直接下一集的所有子元素！！！！！！！！！！
	//root.elements("stu");//取出root下一级的所有stu元素，返回一个list集合！！！！！！！！！！！
	//root.element("stu");//取出root下一级的第一个stu元素，返回一个元素！！！！！！！！！！！
	Element stu=(Element) root.elements("stu").get(0);//得到root下一级的第一个stu元素！！！！！！！！！！！！！！
	System.out.println(stu.element("name"));//不能够跨层取值！！！！！！！！！！！
	System.out.println(((Element)stu.elements("name").get(0)).getText());
}

/*
 * 在指定的地方添加节点
 * Element aaa=DocumentHelper.createElement("");
 * aaa.setText("aaa");
 * 1.先得到节点list  List list=root.element("name").elements();
 * 2.在指定位置添加：list.add(1,aaa);
 * 3.更新：reload()
 */
public static void addByIndex(Document doc,String Searchname){
	Element newHero=DocumentHelper.createElement("stu");
	Element newHero_name=DocumentHelper.createElement("追风");
	Element newHero_sex=DocumentHelper.createElement("男");
	Element newHero_age=DocumentHelper.createElement("33");
	newHero.add(newHero_name);newHero.add(newHero_sex);newHero.add(newHero_age);
	@SuppressWarnings("unchecked")
	List<Element> list=doc.getRootElement().elements("stu");
	for(int i=0;i<list.size();i++){
		Element name=((Element) list.get(i)).element("name");
		if(name.getTextTrim().equals(Searchname)){
			list.add(i+1,newHero);
			break;//加入后必须break,不然list大小变化，情况将不可预知！！！！！！！！
		}
	}
	//reload(doc);
	
}

}

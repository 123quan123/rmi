package com.HTT.classTableMapping;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 解析器读入整个文档，然后构建�?个驻留内存的树结构，使用 DOM 接口来操作这个树结构�?
 * 优点：整个文档树在内存中，便于操作；支持删除、修改�?�重新排列等多种功能；访问效率高�?
 * 缺点：将整个文档调入内存（包括无用的节点），浪费时间和空间；
 * 使用场合：一旦解析了文档还需多次访问这些数据；硬件资源充足（内存、CPU�?
 * 优点�?
 * <ol>
 * 		1、形成了树结构，有助于更好的理解、掌握，且代码容易编写�??
 * 		2、解析过程中，树结构保存在内存中，方便修改�??
 * </ol>
 * 缺点�?
 * <ol>
 *		1、由于文件是�?次�?�读取，�?以对内存的�?�费比较大�??
 * 		2、如果XML文件比较大，容易影响解析性能且可能会造成内存溢出�?
 * </ol>
 * @author quan
 * @version 0.0.1
 */
public abstract class DOMXMLParser {
	private static DocumentBuilder db;
	
	static {
		try {
			//得到 DOM 解析器的工厂实例  
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			//然后�? DOM 工厂获得 DOM 解析�? 
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public DOMXMLParser() {
	}
	
	public static Document getDocument(String path) throws SAXException, IOException{
		//把要解析�? XML 文档转化为输入流，以�? DOM 解析器解析它 
		InputStream is = Class.class.getResourceAsStream(path);
		return getDocument(is);
	}

	public static Document getDocument(InputStream is) throws SAXException, IOException {
		//解析 XML 文档的输入流，得到一�? Document 
		Document doc = db.parse(is);
		
		return doc;
	}
	
	public abstract void parseElement(Element element);
	
	public DOMXMLParser  getXmlRoot(Document doc, String pro) {
		Element ele = doc.getDocumentElement();
		pro = ele.getAttribute(pro);
		System.out.println(ele + " : " + pro);
		
		return this;
	}
	
	public void parseByTagName(Document doc, String tagName) {
		NodeList nodeList = doc.getElementsByTagName(tagName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);
			
			parseElement(element);
		}
	}
	
	public void parseByTagName(Element element, String tagName) {
		NodeList nodeList = element.getElementsByTagName(tagName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element ele = (Element) nodeList.item(i);
			
			parseElement(ele);
		}
	}
}

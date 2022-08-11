package com.ssp.ddd.support.plugin.factory.xml;

import com.ssp.ddd.support.plugin.factory.VObjFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * The template that read xml files with DOM, and build the factory. 
 * When create the factory, input a path or list of paths, 
 * then it reads the resources and build the factory immediately.
 * <p>
 * The path can be a file or a direction, such as:
 * <li>vObj.xml that read from the class local path</li>
 * <li>src/test/java/com/demo2/support/xml that read from relative path</li>
 * <li>C:\\demo-service2-support\\src\\test\\java\\com\\demo2\\support\\xml</li>
 * <li>classpath*:vObj.xml that read from classpath or the classpaths in jar</li>
 * <li>file:C:\\demo-service2-support\\src\\test\\java\\com\\demo2\\support\\xml</li>
 * <li>/mapper/genericDaoMapper.xml that read from the context of the project</li>
 * <p>
 * Inherit the template and call the initFactory(), 
 * and then implement the loadBean(element), such as: 
 * <pre>
	protected void loadBean(Element element) {
		String clazz = element.getAttribute("class");
		String tableName = element.getAttribute("tableName");
		loadChildNodes(element, vObj);
	}
	
	private void loadChildNodes(Element element) {
		NodeList nodeList = element.getChildNodes();
		for(int i=0; i<=nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(!(node instanceof Element)) continue;
			if (node.getNodeName().equals("property")) {
				Element child = (Element) node;
				String name = child.getAttribute("name");
			}
		}
	}
 * </pre>
 * @author fangang
 */
public abstract class XmlBuildFactoryTemplate {

	private static final Log log = LogFactory.getLog(XmlBuildFactoryTemplate.class);
	private boolean validating = false;
	private boolean namespaceAware = false;
	private String[] paths;
	
	/**
	 * @return the namespaceAware
	 */
	public boolean isNamespaceAware() {
		return namespaceAware;
	}

	/**
	 * @param namespaceAware the namespaceAware to set
	 */
	public void setNamespaceAware(boolean namespaceAware) {
		this.namespaceAware = namespaceAware;
	}

	/**
	 * @return the validating
	 */
	public boolean isValidating() {
		return validating;
	}

	/**
	 * @param validating the validating to set
	 */
	public void setValidating(boolean validating) {
		this.validating = validating;
	}

	/**
	 * initialize a factory with path
	 * @param path the path to read xml file
	 */
	public void initFactory(String path){
		this.paths = new String[]{path};
		initFactory(this.paths);
	}
	
	/**
	 * initialize a factory with list of paths
	 * @param paths the list of paths to read xml files.
	 */
	public void initFactory(String... paths){
		try {
			FileResourceLoader loader = new FileResourceLoader();
			loader.loadResource(this::readXmlStream, paths);
		} catch (IOException e1) {
			throw new RuntimeException("no found the file", e1);
		}
	}

	/**
	 * reload the factory that read xml files again.
	 */
	public void reloadFactory(){
		initFactory(this.paths);
	}

	/**
	 * read the xml input stream, and then call <code>buildFactory(Element)</code>
	 * @param inputStream
	 */
	protected void readXmlStream(InputStream inputStream, String path) {
		try {
			if(inputStream==null) {
				throw new RuntimeException("no input stream");
			}
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        factory.setValidating(this.isValidating());
	        factory.setNamespaceAware(this.isNamespaceAware());
	        DocumentBuilder build = factory.newDocumentBuilder();
	        Document doc = build.parse(new InputSource(inputStream));
	        Element root = doc.getDocumentElement();
			if("".equals(root.getNodeName()) || !"vobjs".equals(root.getNodeName())){
				return;
			}
			String fileName = path.substring(path.lastIndexOf("\\") + 1);
	        buildFactory(root, fileName);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			throw new RuntimeException("Error when decode xml stream by sax", e);
		}
		
	}
	
	/**
	 * read from xml and build the factory.
	 * @param root the root of the xml
	 */
	protected void buildFactory(Element root, String fileName) {
		NodeList nodeList = root.getChildNodes();
		for(int i=0; i<=nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(!(node instanceof Element)) {
				continue;
			}
			Element element = (Element) node;
			loadBean(element, fileName);
		}
	}
	
	/**
	 * define what to do with each of node.
	 * @param element
	 */
	protected abstract void loadBean(Element element, String fileName);
}

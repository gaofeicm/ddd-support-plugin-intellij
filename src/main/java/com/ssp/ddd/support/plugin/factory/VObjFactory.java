package com.ssp.ddd.support.plugin.factory;

import com.ssp.ddd.support.plugin.factory.xml.XmlBuildFactoryTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The factory that get the configure of the value objects.
 * @author fangang
 */
public class VObjFactory extends XmlBuildFactoryTemplate {

	private static final Log log = LogFactory.getLog(VObjFactory.class);
	public static List<VObj> OBJ = new ArrayList<>();

	/**
	 * The default constructor.
	 */
	public VObjFactory(String path) {
		if(!OBJ.isEmpty()) {
			return;
		}
		File dir = new File(path);
		if (!dir.exists()) {
			throw new RuntimeException("file path does not exist!");
		}
		List<String> allFileList = new ArrayList<>();
		getAllFile(dir, allFileList);
		String[] files = allFileList.toArray(new String[0]);
		initFactory(files);
		OBJ = OBJ.stream().sorted(Comparator.comparing(VObj::getTable)).collect(Collectors.toList());
	}

	/**
	 * decode a value object configure and load into the factory.
	 * @param element element
	 */
	@Override
	protected void loadBean(Element element, String fileName) {
		String clazz = element.getAttribute("class");
		if("".equals(clazz)){
			return;
		}
		VObj vObj = new VObj();
		vObj.setClazz(clazz);
		vObj.setFrom(fileName);
		String tableName = element.getAttribute("tableName");
		vObj.setTable(tableName);
		loadChildNodes(element, vObj);
		OBJ.add(vObj);
	}

	/**
	 * load all of the child nodes into vObj.
	 * @param element element
	 * @param vObj vObj
	 */
	private void loadChildNodes(Element element, VObj vObj) {
		NodeList nodeList = element.getChildNodes();
		for(int i=0; i<=nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(!(node instanceof Element)) {
				continue;
			}
			if ("property".equals(node.getNodeName())) {
				Property property = getProperty((Element) node);
				vObj.getProperties().add(property);
			}
			if ("join".equals(node.getNodeName())) {
				Join join = getJoin((Element) node);
				vObj.getJoins().add(join);
			}
			if ("ref".equals(node.getNodeName())) {
				Ref ref = getRef((Element) node);
				vObj.getRefs().add(ref);
			}
			if ("extend".equals(node.getNodeName())) {
				Extend extend = getExtend((Element) node);
				vObj.setExtend(extend);
			}
		}
	}

	/**
	 * get property tag from xml.
	 * @param element element
	 * @return property property
	 */
	private Property getProperty(Element element) {
		Property property = new Property();
		String name = element.getAttribute("name");
		property.setName(name);
		String column = element.getAttribute("column");
		property.setColumn(column);
		String isPrimaryKey = element.getAttribute("isPrimaryKey");
		property.setPrimaryKey("true".equalsIgnoreCase(isPrimaryKey));
		String isSelect = element.getAttribute("select");
		property.setSelect(!"false".equals(isSelect));
		String isInsert = element.getAttribute("insert");
		property.setInsert(!"false".equals(isInsert));
		String isUpdate = element.getAttribute("update");
		property.setUpdate(!"false".equals(isUpdate));
		String isDelete = element.getAttribute("delete");
		property.setDelete(!"false".equals(isDelete));
		String isExtend = element.getAttribute("extend");
		property.setExtend(!"false".equals(isExtend));
		String defaultValue = element.getAttribute("defaultValue");
		property.setDefaultValue(defaultValue);
		String updateValue = element.getAttribute("updateValue");
		property.setUpdateValue(updateValue);
		return property;
	}

	/**
	 * get join tag from xml.
	 * @param element element
	 * @return join join
	 */
	private Join getJoin(Element element) {
		Join join = new Join();
		String name = element.getAttribute("name");
		join.setName(name);
		String joinKey = element.getAttribute("joinKey");
		join.setJoinKey(joinKey);
		String joinType = element.getAttribute("joinType");
		join.setJoinType(joinType);
		String clazz = element.getAttribute("class");
		join.setClazz(clazz);
		boolean isAggregation = "true".equals(element.getAttribute("isAggregation"));
		join.setAggregation(isAggregation);
		//分页
		boolean page = "true".equals(element.getAttribute("page"));
		join.setPage(page);
		int pageSize = "".equals(element.getAttribute("pageSize")) ? 50 : Integer.parseInt(element.getAttribute("pageSize"));
		join.setPageSize(pageSize);
		int pageNum = "".equals(element.getAttribute("pageNum")) ? 1 : Integer.parseInt(element.getAttribute("pageNum"));
		join.setPageNum(pageNum);
		boolean joinDelete = "true".equals(element.getAttribute("joinDelete"));
		join.setJoinDelete(joinDelete);
		return join;
	}

	/**
	 * get ref tag from xml.
	 * @param element element
	 * @return ref ref
	 */
	private Ref getRef(Element element) {
		Ref ref = new Ref();
		String name = element.getAttribute("name");
		ref.setName(name);
		String refKey = element.getAttribute("refKey");
		ref.setRefKey(refKey);
		String refType = element.getAttribute("refType");
		ref.setRefType(refType);
		String bean = element.getAttribute("bean");
		ref.setBean(bean);
		String method = element.getAttribute("method");
		ref.setMethod(method);
		String listMethod = element.getAttribute("listMethod");
		ref.setListMethod(listMethod);
		//分页
		boolean page = "true".equals(element.getAttribute("page"));
		ref.setPage(page);
		int pageSize = "".equals(element.getAttribute("pageSize")) ? 50 : Integer.parseInt(element.getAttribute("pageSize"));
		ref.setPageSize(pageSize);
		return ref;
	}

	/**
	 * get extend tag from xml.
	 * @param element element
	 * @return extend extend
	 */
	private Extend getExtend(Element element){
		Extend extend = new Extend();
		String name = element.getAttribute("name");
		extend.setName(name);
		String clazz = element.getAttribute("class");
		extend.setClazz(clazz);
		boolean isAggregation = "true".equals(element.getAttribute("isAggregation"));
		extend.setAggregation(isAggregation);
		return extend;
	}

	/**
	 * get all xml.
	 * @param fileInput fileInput
	 * @param allFileList allFileList
	 */
	private void getAllFile(File fileInput, List<String> allFileList) {
		// 获取文件列表
		File[] fileList = fileInput.listFiles();
		assert fileList != null;
		for (File file : fileList) {
			if (file.isDirectory()) {
				getAllFile(file, allFileList);
			} else {
				if(file.getName().toLowerCase().endsWith(".xml")) {
					allFileList.add(file.getPath());
				}
			}
		}
	}
}

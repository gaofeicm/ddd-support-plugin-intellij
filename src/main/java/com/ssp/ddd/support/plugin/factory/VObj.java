package com.ssp.ddd.support.plugin.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The configuration of the value object.
 * @author fangang
 */
public class VObj {
	private String clazz;
	private String from;
	private String table;
	private List<Property> properties = new ArrayList<>();
	private List<Join> joins = new ArrayList<>();
	private List<Ref> refs = new ArrayList<>();

	private Extend extend;
	/**
	 * @return the clazz
	 */
	public String getClazz() {
		return clazz;
	}
	/**
	 * @param clazz the clazz to set
	 */
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}
	/**
	 * @param table the table to set
	 */
	public void setTable(String table) {
		this.table = table;
	}
	/**
	 * @return the properties
	 */
	public List<Property> getProperties() {
		return properties;
	}
	/**
	 * @param properties the properties to set
	 */
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	/**
	 * @return the joins
	 */
	public List<Join> getJoins() {
		return joins;
	}
	/**
	 * @param joins the joins to set
	 */
	public void setJoins(List<Join> joins) {
		this.joins = joins;
	}
	/**
	 * @return the refs
	 */
	public List<Ref> getRefs() {
		return refs;
	}
	/**
	 * @param refs the refs to set
	 */
	public void setRefs(List<Ref> refs) {
		this.refs = refs;
	}

	public Extend getExtend() {
		return extend;
	}

	public void setExtend(Extend extend) {
		this.extend = extend;
	}

	public String getColumnByName(String name){
		Optional<Property> property = properties.stream().filter(p -> name.equals(p.getName())).findFirst();
		return property.isPresent() ? property.get().getColumn() : "";
	}

	public String getNameByColumn(String column){
		Optional<Property> property = properties.stream().filter(p -> column.equals(p.getColumn())).findFirst();
		return property.isPresent() ? property.get().getName() : "";
	}
}

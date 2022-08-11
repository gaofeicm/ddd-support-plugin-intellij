package com.ssp.ddd.support.plugin.factory;

/**
 * The property of the value object.
 * @author fangang
 */
public class Property {
	private String name;
	private String column;
	private boolean isPrimaryKey;
	private boolean select = true;
	private boolean insert = true;
	private boolean update = true;
	private boolean delete = true;
	private boolean extend = false;
	private String defaultValue;
	private String updateValue;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}
	/**
	 * @param column the column to set
	 */
	public void setColumn(String column) {
		this.column = column;
	}
	/**
	 * @return the isPrimaryKey
	 */
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	/**
	 * @param isPrimaryKey the isPrimaryKey to set
	 */
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public boolean isInsert() {
		return insert;
	}

	public void setInsert(boolean insert) {
		this.insert = insert;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public boolean isExtend() {
		return extend;
	}

	public void setExtend(boolean extend) {
		this.extend = extend;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getUpdateValue() {
		return updateValue;
	}

	public void setUpdateValue(String updateValue) {
		this.updateValue = updateValue;
	}

	public boolean equals(String recFlag){
		boolean result;
		switch (recFlag){
			case "N":
			case "n":
				result = this.isInsert();
				break;
			case "U":
			case "u":
				result = this.isUpdate();
				break;
			case "D":
			case "d":
				result = this.isDelete();
				break;
			default:
				result = this.isSelect();
		}
		return result;
	}
}

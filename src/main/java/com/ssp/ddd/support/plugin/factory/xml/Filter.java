/*
 * created on Dec 3, 2009
 */
package com.ssp.ddd.support.plugin.factory.xml;

/**
 * The file filter
 * @author fangang
 */
public abstract class Filter {
	
	/**
	 * @param fileName the filename pattern such as "*.xml"
	 * @return whether satisfied the filename pattern.
	 */
	public abstract boolean isSatisfied(String fileName);
}

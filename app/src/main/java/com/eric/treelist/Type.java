package com.eric.treelist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Type implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private Type parentType;
	private List<Type> children_l = new ArrayList<Type>();
	private String iconUrl; //图标路径
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Type getParentType() {
		return parentType;
	}
	public void setParentType(Type parentType) {
		this.parentType = parentType;
	}
	public List<Type> getChildren_l() {
		return children_l;
	}
	public void setChildren_l(List<Type> children_l) {
		this.children_l = children_l;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
}

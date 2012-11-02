package com.linqcan.mytodo;

import java.io.Serializable;

public class Task implements Serializable{
	
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 5868552206370971797L;
	private long id;
	private String title;
	private String description;
		
	
	public Task(){
		super();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}

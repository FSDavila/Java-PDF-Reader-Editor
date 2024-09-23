package com.davila.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageAddRequest {
	
	private float x;
	private float y;
	private float width;
	private float height;
	private int page;
	private boolean allPages;
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public boolean isAllPages() {
		return allPages;
	}
	public void setAllPages(boolean allPages) {
		this.allPages = allPages;
	}	
	
}

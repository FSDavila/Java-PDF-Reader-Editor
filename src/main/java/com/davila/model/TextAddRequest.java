package com.davila.model;

public class TextAddRequest {
	
	private float x;
	private float y;
	private String text;
	private int fontSize = 10; // optional, default is size 10
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}	
	
}

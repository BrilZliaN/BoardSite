package ru.school38dz52.board;

public class Category {
	
	private int id = -1;
	private String url = "tasks";
	private String title;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = "tasks/" + url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}

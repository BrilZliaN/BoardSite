package ru.school38dz52.board;

public class Menu {
	
	private String url = "1";
	private String title = "Страница";
	
	public Menu(String url, String title) {
		this.url = url;
		this.title = title;
	}
	
	public Menu() {
		
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	
}

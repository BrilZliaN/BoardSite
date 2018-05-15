package ru.school38dz52.board;

public class Page {
	
	private String url;
	private String title;
	private String content;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = "page/" + url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
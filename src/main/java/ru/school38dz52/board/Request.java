package ru.school38dz52.board;

public class Request {
	
	private int requesterId;
	private int itemId;
	private long time;
	private int performerId = -1;
	public int getRequesterId() {
		return requesterId;
	}
	public void setRequesterId(int requesterId) {
		this.requesterId = requesterId;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getPerformerId() {
		return performerId;
	}
	public void setPerformerId(int performerId) {
		this.performerId = performerId;
	}

}

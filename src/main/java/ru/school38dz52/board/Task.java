package ru.school38dz52.board;

public class Task {
	
	private int id = 0;
	private int categoryId = 0;
	private int userId = 0;
	private String title = "Поручение";
	private String budget = "500";
	private String description = "Описание поручения";
	private String photo = "";
	private boolean enabled = true;
	
	public Task() {
		
	}
	
	public Task(int id, int categoryId, int userId, String title, String budget, String description, String photo, boolean enabled) {
		this.id = id;
		this.categoryId = categoryId;
		this.userId = userId;
		this.title = title;
		this.budget = budget;
		this.description = description;
		this.photo = photo;
		this.setEnabled(enabled);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public Task copy() {
		Task copy = new Task();
		copy.id = this.id;
		copy.budget = this.budget;
		copy.categoryId = this.categoryId;
		copy.description = this.description;
		copy.enabled = this.enabled;
		copy.title = this.title;
		copy.userId = this.userId;
		copy.photo = this.photo;
		return copy;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}

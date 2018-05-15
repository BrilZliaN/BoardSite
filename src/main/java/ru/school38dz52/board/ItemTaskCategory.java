package ru.school38dz52.board;

public class ItemTaskCategory {
	
	private int task_id = 0;
	private int task_categoryId = 0;
	private String task_title = "Поручение";
	private String task_budget = "500";
	private String task_description = "Описание поручения";
	private String photo = "";
	private boolean hasPhoto = false;
	
	private String cat_url = "tasks";
	private String cat_title = "";
	private int cat_id = 0;
	
	private String user_name;
	private String user_phone;
	
	public int getCat_id() {
		return cat_id;
	}
	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}
	public String getCat_url() {
		return cat_url;
	}
	public void setCat_url(String cat_url) {
		this.cat_url = cat_url;
	}
	public String getCat_title() {
		return cat_title;
	}
	public void setCat_title(String cat_title) {
		this.cat_title = cat_title;
	}
	public int getTask_id() {
		return task_id;
	}
	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}
	public int getTask_categoryId() {
		return task_categoryId;
	}
	public void setTask_categoryId(int task_categoryId) {
		this.task_categoryId = task_categoryId;
	}
	public String getTask_title() {
		return task_title;
	}
	public void setTask_title(String task_title) {
		this.task_title = task_title;
	}
	public String getTask_budget() {
		return task_budget;
	}
	public void setTask_budget(String task_budget) {
		this.task_budget = task_budget;
	}
	public String getTask_description() {
		return task_description;
	}
	public void setTask_description(String task_description) {
		this.task_description = task_description;
	}
	
	public void setCategory(Category category) {
		this.cat_title = category.getTitle();
		this.cat_url = category.getUrl(); 
		this.cat_id = category.getId();
	}
	
	public void setTask(Task task) {
		this.task_id = task.getId();
		this.task_categoryId = task.getCategoryId();
		this.task_budget = task.getBudget();
		this.task_title = task.getTitle();
		this.task_description = task.getDescription();
		this.photo = task.getPhoto();
		this.hasPhoto = !photo.equals("");
	}
	
	public void setUser(User user) {
		this.setUser_name(user.getShortUserFullName());
		this.setUser_phone(user.getPhone());
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public boolean isHasPhoto() {
		return hasPhoto;
	}
	public void setHasPhoto(boolean hasPhoto) {
		this.hasPhoto = hasPhoto;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
	
}

package ru.school38dz52.board;

public class ItemRequestInfo {
	
	private int request_id;
	
	private String task_title;
	
	private String customer_name;
	private String customer_email;
	private String customer_phone;
	
	private String request_datetime;
	
	private boolean hasPerformer;
	private String performer_name;
	private String performer_phone;
	
	public static ItemRequestInfo create(int requestId, Request request, Task task, User customer, User performer) {
		ItemRequestInfo i = new ItemRequestInfo();
		i.request_id = requestId;
		i.request_datetime = Utils.getDate(request.getTime(), "dd.MM.yyyy HH:mm:ss");
		i.task_title = task.getTitle();
		i.customer_email = customer.getEmail();
		i.customer_name = customer.getLongUserFullName();
		i.customer_phone = customer.getPhone();
		i.hasPerformer = performer != null;
		if (performer != null) {
			i.performer_name = performer.getShortUserFullName();
			i.performer_phone = performer.getPhone();
		}
		return i;
	}

	public int getRequest_id() {
		return request_id;
	}

	public void setRequest_id(int request_id) {
		this.request_id = request_id;
	}

	public String getTask_title() {
		return task_title;
	}

	public void setTask_title(String task_title) {
		this.task_title = task_title;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getCustomer_email() {
		return customer_email;
	}

	public void setCustomer_email(String customer_email) {
		this.customer_email = customer_email;
	}

	public String getCustomer_phone() {
		return customer_phone;
	}

	public void setCustomer_phone(String customer_phone) {
		this.customer_phone = customer_phone;
	}

	public String getRequest_datetime() {
		return request_datetime;
	}

	public void setRequest_datetime(String request_datetime) {
		this.request_datetime = request_datetime;
	}

	public boolean isHasPerformer() {
		return hasPerformer;
	}

	public void setHasPerformer(boolean hasPerformer) {
		this.hasPerformer = hasPerformer;
	}

	public String getPerformer_name() {
		return performer_name;
	}

	public void setPerformer_name(String performer_name) {
		this.performer_name = performer_name;
	}

	public String getPerformer_phone() {
		return performer_phone;
	}

	public void setPerformer_phone(String performer_phone) {
		this.performer_phone = performer_phone;
	}

}

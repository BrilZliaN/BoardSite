package ru.school38dz52.board;

public class User {
	
	private int id;
	
	private String email;
	private String name;
	private String surname;
	private String patronymic;
	private String phone;
	private String birthday;
	private boolean isPerformer;
	private boolean admin;
	
	private String login;
	private String passwordHash;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPatronymic() {
		return patronymic;
	}
	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public String getShortUserFullName() {
		return surname + " " + name.substring(0, 1) + ". " + patronymic.substring(0, 1) + ".";
	}
	public String getLongUserFullName() {
		return surname + " " + name + " " + patronymic;
	}
	public boolean isPerformer() {
		return isPerformer;
	}
	public void setPerformer(boolean isPerformer) {
		this.isPerformer = isPerformer;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}

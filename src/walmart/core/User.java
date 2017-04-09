package walmart.core;

import java.sql.Timestamp;

@SuppressWarnings("unused")
public class User {
	  private int id;
	  private String email;
	  private String passwordhash;
	  private String oldpassword;
	  private String firstname;
	  private String lastname;
	  private String gender;
	  private String picturepath;
	   private Timestamp lastlogin;
	  private Timestamp lastpasswordchange;
	  private Timestamp lastaccess;
	  private boolean disabled;
	  private boolean resetpassword;
	  private String phone; 
	  private Timestamp creationtime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPasswordhash() {
		return passwordhash;
	}
	public void setPasswordhash(String passwordhash) {
		this.passwordhash = passwordhash;
	}
	public String getOldpassword() {
		return oldpassword;
	}
	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPicturepath() {
		return picturepath;
	}
	public void setPicturepath(String picturepath) {
		this.picturepath = picturepath;
	}
	public Timestamp getLastlogin() {
		return lastlogin;
	}
	public void setLastlogin(Timestamp lastlogin) {
		this.lastlogin = lastlogin;
	}
	public Timestamp getLastpasswordchange() {
		return lastpasswordchange;
	}
	public void setLastpasswordchange(Timestamp lastpasswordchange) {
		this.lastpasswordchange = lastpasswordchange;
	}
	public Timestamp getLastaccess() {
		return lastaccess;
	}
	public void setLastaccess(Timestamp lastaccess) {
		this.lastaccess = lastaccess;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public boolean isResetpassword() {
		return resetpassword;
	}
	public void setResetpassword(boolean resetpassword) {
		this.resetpassword = resetpassword;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Timestamp getCreationtime() {
		return creationtime;
	}
	public void setCreationtime(Timestamp creationtime) {
		this.creationtime = creationtime;
	}

    
}
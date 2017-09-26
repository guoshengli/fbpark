 package com.fbpark.rest.service.model;
 
 import java.io.Serializable;
 
 public class PasswordModel
   implements Serializable
 {
   private static final long serialVersionUID = 4589506673855732679L;
   private String current_password;
   private String password_new;
 
   public String getCurrent_password()
   {
     return this.current_password;
   }
 
   public void setCurrent_password(String current_password) {
     this.current_password = current_password;
   }

public String getPassword_new() {
	return password_new;
}

public void setPassword_new(String password_new) {
	this.password_new = password_new;
}
   
   
 
 }


package com.dubbo.user.api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mio_user")
public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 239843315674365320L;

	private Long id;

	private String name;
	
	private String age;
	
	private String password;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

}

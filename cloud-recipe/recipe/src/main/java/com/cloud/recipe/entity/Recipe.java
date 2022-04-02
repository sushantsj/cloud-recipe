
package com.cloud.recipe.entity;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Recipe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String title;

	@NotNull
	private String body;

	@NotNull
	private String estimatedTime;

	@NotNull
	private Instant date;

	@NotNull
	private String username;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(String estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

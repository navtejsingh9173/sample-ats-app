package com.example.ats.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Valid
@JsonInclude(Include.NON_NULL)
public class CampaignCreateDTO {
	
	public enum CATEGORY{TEMPORARY, FREELANCER, FULL_TIME, PART_TIME};
	
	private Long id;
	@NotNull
	private String title;
	@NotNull
	private String description;
	@NotNull
	private CATEGORY category;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the category
	 */
	public CATEGORY getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(CATEGORY category) {
		this.category = category;
	}

}

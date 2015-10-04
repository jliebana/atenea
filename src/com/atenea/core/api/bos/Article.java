package com.atenea.core.api.bos;

import java.util.Date;
import java.util.List;

/**
 * Business object representing an article
 * @author jliebana
 *
 */
public class Article  {

	private String author;
	private String content;
	private Date date;
	private String title;
	private String uri;
	private List<String> filteredContent;
	private float score;

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setFilteredContent(List<String> filteredContent) {
		this.filteredContent = filteredContent;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

	public Date getDate() {
		return date;
	}

	public String getTitle() {
		return title;
	}

	public String getUri() {
		return uri;
	}

	public List<String> getFilteredContent() {
		return filteredContent;
	}

	public float getScore() {
		return score;
	}

}

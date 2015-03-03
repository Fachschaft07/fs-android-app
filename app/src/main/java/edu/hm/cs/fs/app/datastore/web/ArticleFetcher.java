package edu.hm.cs.fs.app.datastore.web;

import java.util.Date;

/**
 * The Article stores the data for a newsline.
 * 
 * @author Fabio
 * @version 1
 */
public class ArticleFetcher {
	private final String title;
	private final String link;
	private final String description;
	private final String author;
	private final Date releaseDate;

	/**
	 * Creates a new article.
	 * 
	 * @param title
	 *            of the article.
	 * @param link
	 *            to the homepage where the article stands.
	 * @param description
	 *            of the article.
	 * @param author
	 *            of the article.
	 * @param releaseDate
	 *            of the article.
	 */
	public ArticleFetcher(final String title, final String link, final String description, final String author,
						  final Date releaseDate) {
		this.title = title;
		this.link = link;
		this.description = description;
		this.author = author;
		this.releaseDate = releaseDate;
	}

	/**
	 * Get the articles title.
	 * 
	 * @return the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Get the articles link to the homepage.
	 * 
	 * @return the link.
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Get the articles description which contains the article in html-format.
	 * 
	 * @return the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get the articles author.
	 * 
	 * @return the author.
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Get the articles release date.
	 * 
	 * @return the release date.
	 */
	public Date getReleaseDate() {
		return releaseDate;
	}
}

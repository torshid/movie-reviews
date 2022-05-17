package me.yaksoy.moviereviews.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

public class SearchInput {

  @Min(0)
  private int offset;

  @Length(max = 64)
  private String movieTitle;

  @Length(max = 64)
  private String author;

  @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}:[0-9]{4}-[0-9]{2}-[0-9]{2}$")
  private String publicationDate;

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public String getMovieTitle() {
    return movieTitle;
  }

  public void setMovieTitle(String movieTitle) {
    this.movieTitle = movieTitle;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getPublicationDate() {
    return publicationDate;
  }

  public void setPublicationDate(String publicationDate) {
    this.publicationDate = publicationDate;
  }

  @Override
  public String toString() {
    String query = "";
    query += "offset=" + offset;
    if (author != null && !author.trim().isEmpty()) {
      query += "&author=" + author;
    }
    if (movieTitle != null && !movieTitle.trim().isEmpty()) {
      query += "&movieTitle=" + movieTitle;
    }
    if (publicationDate != null && !publicationDate.trim().isEmpty()) {
      query += "&publicationDate=" + publicationDate;
    }
    return query;
  }

}

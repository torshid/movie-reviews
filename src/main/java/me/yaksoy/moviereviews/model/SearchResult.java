package me.yaksoy.moviereviews.model;

import java.util.LinkedList;
import java.util.List;

import org.openapitools.client.model.Review;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResult {

  @JsonProperty("next_page")
  private String nextPage; // eg. movieReviews?offset=40

  private List<Review> reviews;

  public String getNextPage() {
    return nextPage;
  }

  public void setNextPage(String nextPage) {
    this.nextPage = nextPage;
  }

  public List<Review> getReviews() {
    if (reviews == null) {
      reviews = new LinkedList<>();
    }
    return reviews;
  }

  public void setReviews(List<Review> reviews) {
    this.reviews = reviews;
  }

}

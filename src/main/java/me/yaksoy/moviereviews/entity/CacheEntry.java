package me.yaksoy.moviereviews.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import me.yaksoy.moviereviews.model.SearchResult;

@Document
public class CacheEntry {

  @Id
  private String id; // hash of the request query

  private String requestQuery; // query used to get the search result

  private SearchResult searchResult;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRequestQuery() {
    return requestQuery;
  }

  public void setRequestQuery(String requestQuery) {
    this.requestQuery = requestQuery;
  }

  public SearchResult getSearchResult() {
    return searchResult;
  }

  public void setSearchResult(SearchResult searchResult) {
    this.searchResult = searchResult;
  }

}

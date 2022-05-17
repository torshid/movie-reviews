package me.yaksoy.moviereviews.service;

import me.yaksoy.moviereviews.model.SearchInput;
import me.yaksoy.moviereviews.model.SearchResult;

public interface ISearchService {

  SearchResult search(SearchInput input);

}

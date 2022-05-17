package me.yaksoy.moviereviews.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import me.yaksoy.moviereviews.model.SearchInput;
import me.yaksoy.moviereviews.model.SearchResult;

@Service
public class ReliableSearchService implements ISearchService {

  private static final Logger logger = LoggerFactory.getLogger(ReliableSearchService.class);

  private final ISearchService searchService;

  private final RetryTemplate retryTemplate;

  /***
   * @param searchService Service used to retrieve results.
   * @param retryTemplate Retry mechanism used when the search service fails.
   */
  public ReliableSearchService(@Autowired ISearchService searchService,
      RetryTemplate retryTemplate) {
    this.searchService = searchService;
    this.retryTemplate = retryTemplate;
  }

  @Override
  public SearchResult search(SearchInput input) {

    logger.info("Reliably searching with input `" + input + "`...");

    SearchResult searchResult = retryTemplate.execute((i) -> searchService.search(input));

    logger.info("Successfully received search results.");

    return searchResult;

  }

}

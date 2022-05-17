package me.yaksoy.moviereviews.service;

import java.util.LinkedList;
import java.util.List;

import org.openapitools.client.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import me.yaksoy.moviereviews.model.SearchInput;
import me.yaksoy.moviereviews.model.SearchResult;

@Service
public class DummySearchService implements ISearchService {

  private static final Logger logger = LoggerFactory.getLogger(DummySearchService.class);

  @Override
  public SearchResult search(SearchInput input) {

    logger.info("Dummy searching with input `" + input + "`...");

    SearchResult searchResult = new SearchResult();
    searchResult.setNextPage("movieReviews?offset=" + (input.getOffset() + 50));

    List<Review> reviews = new LinkedList<>();
    reviews.add(new Review() {
      {
        setDisplayTitle("Dune");
      }
    });
    reviews.add(new Review() {
      {
        setDisplayTitle("Toy Story");
      }
    });

    searchResult.setReviews(reviews);

    logger.info("Successfully created dummy search results.");

    return searchResult;

  }

}

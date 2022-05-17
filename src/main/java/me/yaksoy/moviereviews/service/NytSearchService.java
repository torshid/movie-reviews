package me.yaksoy.moviereviews.service;

import org.openapitools.client.api.MoviesApi;
import org.openapitools.client.model.InlineResponse2002;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import me.yaksoy.moviereviews.model.SearchInput;
import me.yaksoy.moviereviews.model.SearchResult;

@Service
public class NytSearchService implements ISearchService {

  private static final Logger logger = LoggerFactory.getLogger(NytSearchService.class);

  private final MoviesApi moviesApi;

  private final int resultsPerPage;

  /***
   * @param moviesApi Client used to retrieve movie reviews.
   * @param resultsPerPage Number of entries the API serves per page.
   */
  public NytSearchService(@Autowired MoviesApi moviesApi,
      @Value("${nyt.api.page-size}") int resultsPerPage) {
    this.moviesApi = moviesApi;
    this.resultsPerPage = resultsPerPage;
  }

  @Override
  public SearchResult search(SearchInput input) {

    logger.info("Querying NYT API with input `" + input + "`...");

    InlineResponse2002 nytResponse = moviesApi.reviewsSearchJsonGet(null, input.getOffset(), null,
        null, input.getPublicationDate(), input.getAuthor(), input.getMovieTitle());

    if (!"OK".equals(nytResponse.getStatus())) {
      throw new IllegalStateException(
          "Received unexpected status `" + nytResponse.getStatus() + "` from NYT API!");
    }

    logger.info("Successfully queried NYT API.");
    return convertSearchInputAndNytResponseToSearchResult(input, nytResponse);

  }

  public SearchResult convertSearchInputAndNytResponseToSearchResult(SearchInput searchInput,
      InlineResponse2002 nytResponse) {

    SearchResult searchResult = new SearchResult();
    searchResult.setReviews(nytResponse.getResults());

    if (Boolean.TRUE.equals(nytResponse.getHasMore())) {
      // define 'next page' url according to searchInput's offset and if there are more results
      UriComponents uriComponents =
          UriComponentsBuilder.fromPath("movieReviews").query(searchInput.toString())
              .replaceQueryParam("offset", searchInput.getOffset() + resultsPerPage).build();
      searchResult.setNextPage(uriComponents.toUriString());
    } else {
      searchResult.setNextPage("");
    }

    return searchResult;

  }

}

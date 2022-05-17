package me.yaksoy.moviereviews;

import java.util.LinkedList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapitools.client.api.MoviesApi;
import org.openapitools.client.model.InlineResponse2002;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import me.yaksoy.moviereviews.model.SearchInput;
import me.yaksoy.moviereviews.model.SearchResult;
import me.yaksoy.moviereviews.service.NytSearchService;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class NytSearchServiceTest {

  @Autowired
  private NytSearchService nytSearchService;

  @Value("${nyt.api.page-size}")
  private int resultsPerPage;

  @Test
  void searchResultReviewsTest() {

    SearchInput searchInput = new SearchInput();

    InlineResponse2002 nytResponse = new InlineResponse2002();
    nytResponse.setResults(new LinkedList<>());

    SearchResult searchResult =
        nytSearchService.convertSearchInputAndNytResponseToSearchResult(searchInput, nytResponse);

    Assertions.assertEquals(nytResponse.getResults(), searchResult.getReviews());

  }

  @Test
  void searchResultInputWithOffsetWithMoreResultsTest() {

    SearchInput searchInput = new SearchInput();
    searchInput.setOffset(50);

    InlineResponse2002 nytResponse = new InlineResponse2002();
    nytResponse.hasMore(true);

    SearchResult searchResult =
        nytSearchService.convertSearchInputAndNytResponseToSearchResult(searchInput, nytResponse);

    Assertions.assertEquals("movieReviews?offset=" + (searchInput.getOffset() + resultsPerPage),
        searchResult.getNextPage());

    nytResponse.hasMore(false);

  }

  @Test
  void searchResultInputWithOffsetWithNoMoreResultsTest() {

    SearchInput searchInput = new SearchInput();
    searchInput.setOffset(50);

    InlineResponse2002 nytResponse = new InlineResponse2002();
    nytResponse.hasMore(false);

    SearchResult searchResult =
        nytSearchService.convertSearchInputAndNytResponseToSearchResult(searchInput, nytResponse);

    Assertions.assertEquals("", searchResult.getNextPage());

  }

  @Test
  void searchResultInputWithNoOffsetWithMoreResultsTest() {

    SearchInput searchInput = new SearchInput();

    InlineResponse2002 nytResponse = new InlineResponse2002();
    nytResponse.hasMore(true);

    SearchResult searchResult =
        nytSearchService.convertSearchInputAndNytResponseToSearchResult(searchInput, nytResponse);

    Assertions.assertEquals("movieReviews?offset=" + resultsPerPage, searchResult.getNextPage());

  }

  @Test
  void nytApiCallTest() {

    SearchInput searchInput = new SearchInput();

    MoviesApi moviesApi = Mockito.mock(MoviesApi.class);
    Mockito
        .when(moviesApi.reviewsSearchJsonGet(Mockito.any(), Mockito.any(), Mockito.any(),
            Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(new InlineResponse2002() {
          {
            setStatus("OK");
          }
        });

    NytSearchService nytSearchService = new NytSearchService(moviesApi, 50);
    nytSearchService.search(searchInput);

    Mockito.verify(moviesApi).reviewsSearchJsonGet(null, searchInput.getOffset(), null, null,
        searchInput.getPublicationDate(), searchInput.getAuthor(), searchInput.getMovieTitle());

  }

}

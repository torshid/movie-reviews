package me.yaksoy.moviereviews;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import me.yaksoy.moviereviews.controller.SearchController;
import me.yaksoy.moviereviews.model.SearchInput;
import me.yaksoy.moviereviews.model.SearchResult;
import me.yaksoy.moviereviews.service.ISearchService;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class SearchControllerTest {

  @Test
  void searchControllerTest() {

    // check that the controller correctly calls the service

    SearchInput searchInput = new SearchInput();
    SearchResult searchResult = new SearchResult();

    ISearchService mockSearchService = Mockito.mock(ISearchService.class);
    Mockito.when(mockSearchService.search(searchInput)).thenReturn(searchResult);

    SearchController searchController = new SearchController(mockSearchService);
    ResponseEntity<SearchResult> response = searchController.getMovieReviews(searchInput);

    Mockito.verify(mockSearchService).search(searchInput);

    Assertions.assertEquals(searchResult, response.getBody());

  }

}

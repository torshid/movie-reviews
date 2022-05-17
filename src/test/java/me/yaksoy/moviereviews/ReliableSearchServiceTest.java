package me.yaksoy.moviereviews;

import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.TestPropertySource;

import me.yaksoy.moviereviews.model.SearchInput;
import me.yaksoy.moviereviews.service.ISearchService;
import me.yaksoy.moviereviews.service.ReliableSearchService;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class ReliableSearchServiceTest {

  void reliableSearchTest() {

    SearchInput searchInput = new SearchInput();

    ISearchService mockSearchService = Mockito.mock(ISearchService.class);
    RetryTemplate mockRetryTemplate = Mockito.mock(RetryTemplate.class);

    ReliableSearchService reliableSearchService =
        new ReliableSearchService(mockSearchService, mockRetryTemplate);

    reliableSearchService.search(searchInput);

    Mockito.verify(mockRetryTemplate, Mockito.times(1)).execute(Mockito.any());
    Mockito.verify(mockSearchService, Mockito.times(1)).search(searchInput);

  }

}

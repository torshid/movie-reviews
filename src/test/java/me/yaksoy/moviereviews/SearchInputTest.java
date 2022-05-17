package me.yaksoy.moviereviews;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import me.yaksoy.moviereviews.model.SearchInput;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class SearchInputTest {

  @Test
  void searchInputToStringTest() {

    SearchInput searchInput = new SearchInput();

    searchInput.setOffset(50);
    Assertions.assertEquals("offset=50", searchInput.toString());

    searchInput.setAuthor("Yusuf");
    Assertions.assertEquals("offset=50&author=Yusuf", searchInput.toString());

    searchInput.setMovieTitle("Sun");
    Assertions.assertEquals("offset=50&author=Yusuf&movieTitle=Sun", searchInput.toString());

    searchInput.setPublicationDate("2020-01-01:2021-01-01");
    Assertions.assertEquals(
        "offset=50&author=Yusuf&movieTitle=Sun&publicationDate=2020-01-01:2021-01-01",
        searchInput.toString());

  }

}

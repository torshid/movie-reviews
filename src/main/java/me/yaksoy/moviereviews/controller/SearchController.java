package me.yaksoy.moviereviews.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.yaksoy.moviereviews.model.SearchInput;
import me.yaksoy.moviereviews.model.SearchResult;
import me.yaksoy.moviereviews.service.ISearchService;

@Tag(name = "Movie Reviews Search API")
@RestController
@RequestMapping("api/movieReviews")
public class SearchController {

  private final ISearchService searchService;

  public SearchController(@Autowired ISearchService searchService) {
    this.searchService = searchService;
  }

  @Parameters({
      @Parameter(in = ParameterIn.QUERY, name = "offset",
          description = "Results offset, used for pagination", schema = @Schema(type = "integer"),
          example = "0"),
      @Parameter(in = ParameterIn.QUERY, name = "movieTitle", description = "Movie title keywords",
          schema = @Schema(type = "string"), example = "World's Fair"),
      @Parameter(in = ParameterIn.QUERY, name = "author", description = "Review author",
          schema = @Schema(type = "string"), example = "A.O. Scott"),
      @Parameter(in = ParameterIn.QUERY, name = "publicationDate",
          description = "Review publication date range", schema = @Schema(type = "string"),
          example = "2020-01-01:2022-12-31")})
  @GetMapping
  public ResponseEntity<SearchResult> getMovieReviews(
      @Parameter(hidden = true) @Valid SearchInput searchInput) {
    return ResponseEntity.ok(searchService.search(searchInput));
  }

}

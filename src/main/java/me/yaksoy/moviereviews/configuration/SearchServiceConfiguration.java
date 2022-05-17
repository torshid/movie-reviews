package me.yaksoy.moviereviews.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.support.RetryTemplate;

import me.yaksoy.moviereviews.repository.ICacheEntryRepository;
import me.yaksoy.moviereviews.service.CacheSearchService;
import me.yaksoy.moviereviews.service.DummySearchService;
import me.yaksoy.moviereviews.service.IHashingService;
import me.yaksoy.moviereviews.service.ISearchService;
import me.yaksoy.moviereviews.service.NytSearchService;
import me.yaksoy.moviereviews.service.ReliableSearchService;

@Configuration
public class SearchServiceConfiguration {

  @Autowired
  private RetryTemplate retryTemplate;

  @Autowired
  private NytSearchService nytSearchService;

  @Autowired
  private DummySearchService dummySearchService;

  @Autowired
  private ICacheEntryRepository cacheEntryRepository;

  @Autowired
  private IHashingService hashingService;

  @Primary
  @Bean
  public ISearchService searchService() {
    // return dummySearchService;
    // return nytSearchService;
    // return new CacheSearchService(dummySearchService, cacheEntryRepository, hashingService);
    // return new CacheSearchService(nytSearchService, cacheEntryRepository, hashingService);
    // return new ReliableSearchService(nytSearchService, retryTemplate);
    return new ReliableSearchService(
        new CacheSearchService(nytSearchService, cacheEntryRepository, hashingService),
        retryTemplate);
  }

}

package me.yaksoy.moviereviews;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import me.yaksoy.moviereviews.entity.CacheEntry;
import me.yaksoy.moviereviews.model.SearchResult;
import me.yaksoy.moviereviews.repository.ICacheEntryRepository;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class CacheEntryRepositoryTest {

  @Autowired
  private ICacheEntryRepository cacheEntryRepository;

  @Test
  void cacheEntryRepositoryTest() {

    CacheEntry cacheEntry = new CacheEntry();
    cacheEntry.setId("some id");
    cacheEntry.setSearchResult(new SearchResult());

    CacheEntry savedCacheEntry = cacheEntryRepository.save(cacheEntry);

    Assertions.assertEquals(cacheEntry.getId(), savedCacheEntry.getId());

    cacheEntryRepository.delete(savedCacheEntry);

    Assertions.assertFalse(cacheEntryRepository.existsById(cacheEntry.getId()));

  }

}

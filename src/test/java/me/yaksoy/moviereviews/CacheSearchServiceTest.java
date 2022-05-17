package me.yaksoy.moviereviews;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import me.yaksoy.moviereviews.entity.CacheEntry;
import me.yaksoy.moviereviews.model.SearchInput;
import me.yaksoy.moviereviews.model.SearchResult;
import me.yaksoy.moviereviews.repository.ICacheEntryRepository;
import me.yaksoy.moviereviews.service.CacheSearchService;
import me.yaksoy.moviereviews.service.IHashingService;
import me.yaksoy.moviereviews.service.ISearchService;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class CacheSearchServiceTest {

  @Autowired
  private CacheSearchService cacheSearchService;

  @Autowired
  private IHashingService hashingService;

  @Autowired
  private ICacheEntryRepository cacheEntryRepository;

  @Test
  void isCacheableTest() {

    SearchInput searchInput = new SearchInput();
    Assertions.assertFalse(cacheSearchService.isCacheable(searchInput));

    searchInput.setPublicationDate("xyz");
    Assertions.assertFalse(cacheSearchService.isCacheable(searchInput));

    searchInput.setPublicationDate("2020");
    Assertions.assertFalse(cacheSearchService.isCacheable(searchInput));

    searchInput.setOffset(40);
    Assertions.assertFalse(cacheSearchService.isCacheable(searchInput));

    searchInput.setPublicationDate("2020-01-01");
    Assertions.assertTrue(cacheSearchService.isCacheable(searchInput));

    searchInput.setPublicationDate("2020-01-01:2022-01-01");
    Assertions.assertTrue(cacheSearchService.isCacheable(searchInput));

    searchInput.setAuthor("Yusuf");
    Assertions.assertTrue(cacheSearchService.isCacheable(searchInput));

  }

  @Test
  void cacheEntryPersistencyTest() {

    SearchInput searchInput = new SearchInput();
    SearchResult searchResult = new SearchResult();

    CacheEntry cacheEntry = cacheSearchService.cache(searchInput, searchResult);

    Assertions.assertEquals(hashingService.hash(searchInput.toString()), cacheEntry.getId());
    Assertions.assertTrue(cacheEntryRepository.existsById(cacheEntry.getId()));

    cacheEntryRepository.delete(cacheEntry);

  }

  @Test
  void notCacheableTest() {

    // cacheSearchService is expected to invoke and return result provided by the mockSearchService

    SearchInput searchInput = new SearchInput();
    SearchResult searchResult = new SearchResult();

    ISearchService mockSearchService = Mockito.mock(ISearchService.class);
    Mockito.when(mockSearchService.search(searchInput)).thenReturn(searchResult);

    ICacheEntryRepository cacheEntryRepository = Mockito.mock(ICacheEntryRepository.class);

    CacheSearchService cacheSearchService =
        new CacheSearchService(mockSearchService, cacheEntryRepository, hashingService) {
          @Override
          public boolean isCacheable(SearchInput request) {
            return false;
          }
        };

    Assertions.assertEquals(searchResult, cacheSearchService.search(searchInput));

    Mockito.verifyNoInteractions(cacheEntryRepository);
    Mockito.verify(mockSearchService).search(searchInput);

  }

  @Test
  void cacheableHitTest() {

    // cacheSearchService is expected to return result provided by the cacheEntryRepository

    SearchInput searchInput = new SearchInput();

    ISearchService mockSearchService = Mockito.mock(ISearchService.class);

    Optional<CacheEntry> cacheEntry = Optional.of(new CacheEntry() {
      {
        setSearchResult(new SearchResult());
      }
    });

    ICacheEntryRepository cacheEntryRepository = Mockito.mock(ICacheEntryRepository.class);
    Mockito.when(cacheEntryRepository.findById(hashingService.hash(searchInput.toString())))
        .thenReturn(cacheEntry);

    CacheSearchService cacheSearchService = Mockito
        .spy(new CacheSearchService(mockSearchService, cacheEntryRepository, hashingService) {
          @Override
          public boolean isCacheable(SearchInput request) {
            return true;
          }
        });

    Assertions.assertEquals(cacheEntry.get().getSearchResult(),
        cacheSearchService.search(searchInput));

    Mockito.verify(cacheEntryRepository).findById(hashingService.hash(searchInput.toString()));
    Mockito.verifyNoInteractions(mockSearchService);
    Mockito.verify(cacheSearchService, Mockito.times(0)).cache(Mockito.any(), Mockito.any());

  }

  @Test
  void cacheableMissTest() {

    // cacheSearchService is expected to invoke and return result provided by the mockSearchService,
    // and cache the result

    SearchInput searchInput = new SearchInput();
    SearchResult searchResult = new SearchResult();

    ISearchService mockSearchService = Mockito.mock(ISearchService.class);
    Mockito.when(mockSearchService.search(searchInput)).thenReturn(searchResult);

    CacheEntry cacheEntry = new CacheEntry();

    ICacheEntryRepository cacheEntryRepository = Mockito.mock(ICacheEntryRepository.class);
    Mockito.when(cacheEntryRepository.findById(hashingService.hash(searchInput.toString())))
        .thenReturn(Optional.empty());
    Mockito.when(cacheEntryRepository.save(Mockito.any())).thenReturn(cacheEntry);

    CacheSearchService cacheSearchService = Mockito
        .spy(new CacheSearchService(mockSearchService, cacheEntryRepository, hashingService) {
          @Override
          public boolean isCacheable(SearchInput request) {
            return true;
          }
        });

    Assertions.assertEquals(searchResult, cacheSearchService.search(searchInput));

    Mockito.verify(cacheEntryRepository).findById(hashingService.hash(searchInput.toString()));
    Mockito.verify(mockSearchService).search(searchInput);
    Mockito.verify(cacheSearchService).cache(searchInput, searchResult);

  }

}

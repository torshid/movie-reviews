package me.yaksoy.moviereviews.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.yaksoy.moviereviews.entity.CacheEntry;
import me.yaksoy.moviereviews.model.SearchInput;
import me.yaksoy.moviereviews.model.SearchResult;
import me.yaksoy.moviereviews.repository.ICacheEntryRepository;

@Service
public class CacheSearchService implements ISearchService {

  private static final Logger logger = LoggerFactory.getLogger(CacheSearchService.class);

  private final IHashingService hashingService;

  private final ICacheEntryRepository cacheEntryRepository;

  private final ISearchService searchService;

  /***
   * @param searchService Service used to retrieve results when cache is missed.
   * @param cacheEntryRepository Repository from which to get cache entries.
   * @param hashingService Used to calculate hashes.
   */
  public CacheSearchService(@Autowired ISearchService searchService,
      @Autowired ICacheEntryRepository cacheEntryRepository,
      @Autowired IHashingService hashingService) {
    this.searchService = searchService;
    this.cacheEntryRepository = cacheEntryRepository;
    this.hashingService = hashingService;
  }

  @Override
  public SearchResult search(SearchInput input) {

    boolean cacheable = isCacheable(input);

    if (cacheable) {

      logger.info("Hitting cache for input `" + input + "`...");

      CacheEntry cacheEntry =
          cacheEntryRepository.findById(hashingService.hash(input.toString())).orElse(null);

      if (cacheEntry != null) {
        logger.info("Cache hit!");
        return cacheEntry.getSearchResult();
      }

      else {

        logger.info("Cache missed, retrieving search results...");
        SearchResult searchResult = searchService.search(input);

        logger.info("Caching search results...");
        cache(input, searchResult);

        return searchResult;

      }

    }

    logger.info("Not cacheable input `" + input + "`, simply retrieving results...");
    return searchService.search(input);

  }

  public boolean isCacheable(SearchInput request) {
    return request.getPublicationDate() != null
        && request.getPublicationDate().trim().startsWith("2020-");
  }

  public CacheEntry cache(SearchInput request, SearchResult searchResult) {

    CacheEntry cacheEntry = new CacheEntry();
    cacheEntry.setId(hashingService.hash(request.toString()));
    cacheEntry.setRequestQuery(request.toString());
    cacheEntry.setSearchResult(searchResult);

    return cacheEntryRepository.save(cacheEntry);

  }

}

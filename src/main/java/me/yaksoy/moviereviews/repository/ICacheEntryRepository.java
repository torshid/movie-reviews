package me.yaksoy.moviereviews.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import me.yaksoy.moviereviews.entity.CacheEntry;

@Repository
public interface ICacheEntryRepository extends MongoRepository<CacheEntry, String> {
}

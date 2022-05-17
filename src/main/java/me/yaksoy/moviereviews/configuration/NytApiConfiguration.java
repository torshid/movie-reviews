package me.yaksoy.moviereviews.configuration;

import org.openapitools.client.ApiClient;
import org.openapitools.client.api.MoviesApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NytApiConfiguration {

  @Value("${nyt.api.key}")
  private String nytApiKey;

  @Bean
  public MoviesApi moviesApi() {
    return new MoviesApi(apiClient());
  }

  @Bean
  public ApiClient apiClient() {
    ApiClient apiClient = new ApiClient();
    apiClient.setApiKey(nytApiKey);
    return apiClient;
  }

}

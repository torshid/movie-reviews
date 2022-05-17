package me.yaksoy.moviereviews.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfiguration {

  @Value("${retry.attempts}")
  private int retryAttempts;

  @Bean
  public RetryTemplate retryTemplate() {

    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
    retryPolicy.setMaxAttempts(retryAttempts);

    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setInitialInterval(1000);
    backOffPolicy.setMaxInterval(10000);
    backOffPolicy.setMultiplier(2);

    RetryTemplate template = new RetryTemplate();
    template.setRetryPolicy(retryPolicy);
    template.setBackOffPolicy(backOffPolicy);

    return template;

  }

}

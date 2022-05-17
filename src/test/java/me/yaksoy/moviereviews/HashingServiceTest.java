package me.yaksoy.moviereviews;

import me.yaksoy.moviereviews.service.IHashingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class HashingServiceTest {

  @Autowired
  private IHashingService hashingService;

  @Test
  void deterministicTest() {
    Assertions.assertEquals(hashingService.hash("abc"), hashingService.hash("abc"));
  }

  @Test
  void outputDifferentFromInputTest() {
    Assertions.assertNotEquals(hashingService.hash("abc"), "abc");
  }

  @Test
  void differentOutputsForDifferentInputs() {
    Assertions.assertNotEquals(hashingService.hash("abc"), hashingService.hash("xyz"));
  }

}

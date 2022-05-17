package me.yaksoy.moviereviews.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class HashingService implements IHashingService {

  public String hash(String input) {
    Objects.requireNonNull(input);
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.update(input.getBytes());
      byte[] digest = messageDigest.digest();
      StringBuilder sb = new StringBuilder();
      for (byte b : digest) {
        sb.append(Integer.toHexString(b & 0xff));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      // should never occur
      throw new RuntimeException(e);
    }
  }

}

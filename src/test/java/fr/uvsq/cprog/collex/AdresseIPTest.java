package fr.uvsq.cprog.collex;

import static org.junit.Assert.*;
import org.junit.Test;

public class AdresseIPTest {

  @Test
  public void acceptsValidIp() {
    AdresseIP ip = new AdresseIP("193.51.31.90");
    assertEquals("193.51.31.90", ip.value());
    assertEquals("193.51.31.90", ip.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectsNull() {
    new AdresseIP(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectsOutOfRange() {
    new AdresseIP("256.0.0.1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectsMalformed() {
    new AdresseIP("a.b.c.d");
  }
}

package fr.uvsq.cprog.collex;

import static org.junit.Assert.*;
import org.junit.Test;

public class NomMachineTest {

  @Test
  public void acceptsValidFqdn() {
    NomMachine n = new NomMachine("www.uvsq.fr");
    assertEquals("www.uvsq.fr", n.value());
    assertEquals("www.uvsq.fr", n.toString());
  }

  @Test
  public void equalsIsCaseInsensitive() {
    NomMachine a = new NomMachine("WWW.UVSQ.FR");
    NomMachine b = new NomMachine("www.uvsq.fr");
    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectsInvalidFqdn() {
    new NomMachine("bad_name.uvsq.fr"); // '_' interdit
  }
}

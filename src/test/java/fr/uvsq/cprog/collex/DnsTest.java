package fr.uvsq.cprog.collex;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class DnsTest {
  private Dns dns;

  @Before
    public void setup() throws Exception {
      Path db = java.nio.file.Path.of("dns.txt");
      java.nio.file.Files.deleteIfExists(db);
      java.nio.file.Files.writeString(db,
          "193.51.31.90 www.uvsq.fr\n" +
          "193.51.25.12 ecampus.uvsq.fr\n");
      dns = new Dns();
    }

  @Test
  public void getByIp() {
    DnsItem it = dns.getItem(new AdresseIP("193.51.31.90"));
    assertNotNull(it);
    assertEquals("www.uvsq.fr", it.getNom().toString());
  }

  @Test
  public void getByName() {
    DnsItem it = dns.getItem(new NomMachine("ecampus.uvsq.fr"));
    assertNotNull(it);
    assertEquals("193.51.25.12", it.getAdresse().toString());
  }

  @Test
  public void listByDomainSorted() {
    List<DnsItem> list = dns.getItems("uvsq.fr");
    assertEquals(2, list.size());
    assertEquals("ecampus.uvsq.fr", list.get(0).getNom().toString());
    assertEquals("www.uvsq.fr", list.get(1).getNom().toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void addDuplicateNameFails() {
    dns.addItem(new AdresseIP("193.51.31.154"), new NomMachine("www.uvsq.fr"));
  }

  @Test
  public void addPersistsToFile() throws Exception {
    dns.addItem(new AdresseIP("193.51.31.154"), new NomMachine("poste.uvsq.fr"));
    // recharge pour vérifier la persistance
    Dns again = new Dns();
    assertNotNull(again.getItem(new AdresseIP("193.51.31.154")));
    assertEquals("poste.uvsq.fr",
        again.getItem(new AdresseIP("193.51.31.154")).getNom().toString());
  }
}


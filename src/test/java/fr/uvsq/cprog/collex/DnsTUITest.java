package fr.uvsq.cprog.collex;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Before;
import org.junit.Test;

public class DnsTUITest {
  private Dns dns;
  private DnsTUI tui;

  @Before
  public void setup() throws Exception {
    Path db = Path.of("dns.txt");
    Files.deleteIfExists(db);
    Files.writeString(db,
        "193.51.31.90 www.uvsq.fr\n" +
        "193.51.25.12 ecampus.uvsq.fr\n");
    dns = new Dns();
    tui = new DnsTUI();
  }

  @Test
  public void parseAndRunGetByName() {
    Commande c = tui.nextCommande(dns, "get www.uvsq.fr");
    String out = c.execute();
    assertEquals("193.51.31.90", out);
  }

  @Test
  public void parseAndRunGetByIp() {
    Commande c = tui.nextCommande(dns, "get 193.51.25.12");
    String out = c.execute();
    assertEquals("ecampus.uvsq.fr", out);
  }

  @Test
  public void parseAndRunLs() {
    Commande c = tui.nextCommande(dns, "ls uvsq.fr");
    String out = c.execute();
    // L'ordre est lexicographique sur les noms
    String[] lines = out.split("\\R");
    assertEquals(2, lines.length);
    assertEquals("ecampus.uvsq.fr", lines[0]);
    assertEquals("www.uvsq.fr", lines[1]);
  }

  @Test
  public void parseAndRunLsWithA() {
    Commande c = tui.nextCommande(dns, "ls -a uvsq.fr");
    String out = c.execute();
    assertTrue(out.contains("193.51.25.12 ecampus.uvsq.fr"));
    assertTrue(out.contains("193.51.31.90 www.uvsq.fr"));
  }

  @Test
  public void parseAndRunAdd() {
    Commande c = tui.nextCommande(dns, "add 193.51.31.154 poste.uvsq.fr");
    String out = c.execute();
    assertEquals("", out); // pas de sortie sur add
    assertNotNull(dns.getItem(new AdresseIP("193.51.31.154")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidCommandThrows() {
    tui.nextCommande(dns, "get"); // argument manquant
  }

  @Test
  public void quitReturnsQuit() {
    Commande c = tui.nextCommande(dns, "quit");
    assertEquals("quit", c.execute());
  }
}


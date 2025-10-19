package fr.uvsq.cprog.collex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DnsApp {
  private final Dns dns;
  private final DnsTUI tui;

  public DnsApp(Dns dns, DnsTUI tui) {
    this.dns = dns;
    this.tui = tui;
  }

  public void run() throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
      while (true) {
        String line = br.readLine();
        if (line == null) break;
        Commande cmd = tui.nextCommande(dns, line);
        String res = cmd.execute();
        if ("quit".equals(res)) break;
        if (!res.isEmpty()) {
          System.out.println(tui.affiche(res));
        }
      }
    }
  }

  public static void main(String[] args) throws IOException {
    Dns dns = new Dns();
    DnsTUI tui = new DnsTUI();
    new DnsApp(dns, tui).run();
  }
}


package fr.uvsq.cprog.collex;

import java.util.Optional;

public class CmdGet implements Commande {
  private final Dns dns;
  private final String token;

  public CmdGet(Dns dns, String token) {
    this.dns = dns;
    this.token = token;
  }

  @Override
  public String execute() {
    // try IP first
    try {
      DnsItem item = dns.getItem(new AdresseIP(token));
      if (item != null) {
        return item.getNom().toString();
      }
    } catch (IllegalArgumentException ignored) {}
    // else try FQDN
    try {
      DnsItem item = dns.getItem(new NomMachine(token));
      if (item != null) {
        return item.getAdresse().toString();
      }
    } catch (IllegalArgumentException ignored) {}
    return "not found";
  }
}


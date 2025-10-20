package fr.uvsq.cprog.collex;

import java.util.List;
import java.util.stream.Collectors;

public final class CmdLs implements Commande {
  private final Dns dns;
  private final String domaine;
  private final boolean withAddr;

  public CmdLs(Dns dns, String domaine, boolean withAddr) {
    this.dns = dns;
    this.domaine = domaine;
    this.withAddr = withAddr;
  }

  @Override
  public String execute() {
    List<DnsItem> items = dns.getItems(domaine);
    if (items.isEmpty()) return "";

    if (withAddr) {
      return items.stream()
          .map(it -> it.getAdresse().toString() + " " + it.getNom().toString())
          .collect(Collectors.joining(System.lineSeparator()));
    } else {
      return items.stream()
          .map(it -> it.getNom().toString())
          .collect(Collectors.joining(System.lineSeparator()));
    }
  }
}

package fr.uvsq.cprog.collex;

import java.util.List;
import java.util.stream.Collectors;

public class CmdLs implements Commande {
  private final Dns dns;
  private final String domaine;
  private final boolean withAddress;

  public CmdLs(Dns dns, String domaine, boolean withAddress) {
    this.dns = dns;
    this.domaine = domaine;
    this.withAddress = withAddress;
  }

  @Override
  public String execute() {
    List<DnsItem> items = dns.getItems(domaine);
    if (items.isEmpty()) return "";
    if (withAddress) {
      return items.stream()
          .map(it -> it.getAdresse() + " " + it.getNom())
          .collect(Collectors.joining(System.lineSeparator()));
    } else {
      return items.stream()
          .map(it -> it.getNom().toString())
          .collect(Collectors.joining(System.lineSeparator()));
    }
  }
}


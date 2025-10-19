package fr.uvsq.cprog.collex;

public class CmdAdd implements Commande {
  private final Dns dns;
  private final String ip;
  private final String name;

  public CmdAdd(Dns dns, String ip, String name) {
    this.dns = dns;
    this.ip = ip;
    this.name = name;
  }

  @Override
  public String execute() {
    dns.addItem(new AdresseIP(ip), new NomMachine(name));
    return "";
  }
}

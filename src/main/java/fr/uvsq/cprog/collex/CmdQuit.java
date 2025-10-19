package fr.uvsq.cprog.collex;

public class CmdQuit implements Commande {
  @Override
  public String execute() {
    return "quit";
  }
}

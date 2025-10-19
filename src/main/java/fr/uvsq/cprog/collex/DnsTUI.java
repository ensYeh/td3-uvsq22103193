package fr.uvsq.cprog.collex;

import java.util.Locale;

public class DnsTUI {

  /** Convertit une ligne utilisateur en Commande. */
  public Commande nextCommande(Dns dns, String input) {
    if (input == null) return null;
    String s = input.trim();
    if (s.isEmpty()) return null;

    // tolère un prompt tapé devant ("> ..." ou "$ ...")
    s = s.replaceFirst("^[>\\$]\\s*", "");

    String[] parts = s.split("\\s+");
    String cmd = parts[0].toLowerCase(Locale.ROOT);

    switch (cmd) {
      case "quit":
      case "exit":
        return new CmdQuit();

      case "get":
        // DOIT avoir exactement 2 tokens, sinon -> IllegalArgumentException
        if (parts.length == 2) {
          return new CmdGet(dns, parts[1]);
        }
        throw new IllegalArgumentException("Usage: get <ip|fqdn>");

      case "ls": {
        boolean withAddr = false;
        int idx = 1;
        if (parts.length > 1 && "-a".equals(parts[1])) {
          withAddr = true;
          idx = 2;
        }
        if (parts.length > idx) {
          return new CmdLs(dns, parts[idx], withAddr);
        }
        throw new IllegalArgumentException("Usage: ls [-a] <domaine>");
      }

      case "add":
        if (parts.length == 3) {
          return new CmdAdd(dns, parts[1], parts[2]);
        }
        throw new IllegalArgumentException("Usage: add <ip> <fqdn>");

      default:
        // pas une commande reconnue -> on peut accepter un GET IMPLICITE
        // mais SEULEMENT si le token unique est une IP OU un FQDN valide.
        if (parts.length == 1) {
          String token = parts[0];
          if (isValidIpOrFqdn(token)) {
            return new CmdGet(dns, token);
          }
        }
        throw new IllegalArgumentException("Commande invalide: " + input);
    }
  }

  /** Retourne le texte à afficher (utile pour tests). */
  public String affiche(String output) {
    return output == null ? "" : output;
  }

  // --- helpers ---

  /** On n'autorise le GET implicite que si le token est réellement une IP ou un FQDN. */
  private boolean isValidIpOrFqdn(String token) {
    try {
      new AdresseIP(token);
      return true;
    } catch (IllegalArgumentException ignore) {
      // pas une IP, on essaie FQDN
    }
    try {
      new NomMachine(token);
      return true;
    } catch (IllegalArgumentException ignore) {
      return false;
    }
  }
}

package fr.uvsq.cprog.collex;

import java.util.Objects;

/** Entrée associant une IP et un nom de machine. */
public final class DnsItem {
  private final AdresseIP adresse;
  private final NomMachine nom;

  public DnsItem(AdresseIP adresse, NomMachine nom) {
    this.adresse = Objects.requireNonNull(adresse, "adresse");
    this.nom = Objects.requireNonNull(nom, "nom");
  }

  public AdresseIP getAdresse() {
    return adresse;
  }

  public NomMachine getNom() {
    return nom;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DnsItem)) {
      return false;
    }
    DnsItem other = (DnsItem) o;
    return adresse.equals(other.adresse) && nom.equals(other.nom);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adresse, nom);
  }

  @Override
  public String toString() {
    return adresse + " " + nom;
  }
}

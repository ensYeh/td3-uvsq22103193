package fr.uvsq.cprog.collex;

import java.util.Objects;
import java.util.regex.Pattern;

/** Value object representing a fully qualified domain name. */
public final class NomMachine implements Comparable<NomMachine> {
  private static final Pattern FQDN =
      Pattern.compile("^(?=.{1,253}$)(?!-)([a-zA-Z0-9-]{1,63}\\.)+[a-zA-Z]{2,63}$");
  private final String value;

  public NomMachine(String value) {
    if (value == null || !FQDN.matcher(value.trim()).matches()) {
      throw new IllegalArgumentException("Nom de machine invalide: " + value);
    }
    this.value = value.trim();
  }

  public String value() {
    return value;
  }

  public String domaine() {
    int firstDot = value.indexOf('.');
    return firstDot < 0 ? value : value.substring(firstDot + 1);
  }

  @Override
  public int compareTo(NomMachine o) {
    return this.value.compareTo(o.value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof NomMachine)) return false;
    NomMachine that = (NomMachine) o;
    return value.equalsIgnoreCase(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value.toLowerCase());
  }

  @Override
  public String toString() {
    return value;
  }
}


package fr.uvsq.cprog.collex;

import java.util.Objects;
import java.util.regex.Pattern;

/** Value object representing an IPv4 address. */
public final class AdresseIP {
  private static final Pattern IPV4 =
      Pattern.compile(
          "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\."
              + "(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\."
              + "(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\."
              + "(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)$");
  private final String value;

  public AdresseIP(String value) {
    if (value == null || !IPV4.matcher(value.trim()).matches()) {
      throw new IllegalArgumentException("Adresse IP invalide: " + value);
    }
    this.value = value.trim();
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AdresseIP)) return false;
    AdresseIP that = (AdresseIP) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return value;
  }
}

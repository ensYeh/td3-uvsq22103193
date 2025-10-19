package fr.uvsq.cprog.collex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;


public final class Dns {

  private final Path filePath;
  private final Map<String, DnsItem> byIp   = new LinkedHashMap<>();
  private final Map<String, DnsItem> byName = new LinkedHashMap<>();


  public Dns() {
    String fileFromSysProp = System.getProperty("dns.file", null);


    String fileFromProps = null;
    try (InputStream in =
             Dns.class.getClassLoader().getResourceAsStream("dns.properties")) {
      if (in != null) {
        Properties p = new Properties();
        p.load(in);
        fileFromProps = p.getProperty("dns.file");
      }
    } catch (IOException e) {

    }


    String file = fileFromSysProp != null
        ? fileFromSysProp
        : (fileFromProps != null ? fileFromProps : "dns.txt");

    this.filePath = Paths.get(file);


    loadFromFile();
  }


  private void loadFromFile() {
    if (!Files.exists(filePath)) {

      return;
    }
    try (BufferedReader br =
             Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
      String line;
      while ((line = br.readLine()) != null) {
        String s = line.trim();
        if (s.isEmpty() || s.startsWith("#")) {
          continue;
        }
        String[] parts = s.split("\\s+");
        if (parts.length != 2) {
 
          continue;
        }
        try {
          AdresseIP ip = new AdresseIP(parts[0]);
          NomMachine nm = new NomMachine(parts[1]);
          addInternal(ip, nm, false);
        } catch (IllegalArgumentException ex) {
          // Entrée invalide -> on ignore la ligne
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException("Erreur de lecture de " + filePath, e);
    }
  }


  private void persist() {
    try (BufferedWriter bw =
             Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
      for (DnsItem it : byIp.values()) {
        bw.write(it.getAdresse().toString());
        bw.write(' ');
        bw.write(it.getNom().toString());
        bw.newLine();
      }
    } catch (IOException e) {
      throw new IllegalStateException("Erreur d'écriture de " + filePath, e);
    }
  }


  private void addInternal(AdresseIP ip, NomMachine nm, boolean checkDuplicate) {
    if (checkDuplicate) {
      if (byIp.containsKey(ip.toString())) {
        throw new IllegalArgumentException("Adresse IP déjà existante");
      }
      if (byName.containsKey(nm.toString())) {
        throw new IllegalArgumentException("Nom de machine déjà existant");
      }
    }
    DnsItem item = new DnsItem(ip, nm);
    byIp.put(ip.toString(), item);
    byName.put(nm.toString(), item);
  }

  /** Récupère une entrée par adresse IP (exacte), ou null si absente. */
  public DnsItem getItem(AdresseIP ip) {
    return byIp.get(ip.toString());
  }

  /** Récupère une entrée par FQDN (exact), ou null si absente. */
  public DnsItem getItem(NomMachine nom) {
    return byName.get(nom.toString());
  }


  public List<DnsItem> getItems(String domaine) {
    if (domaine == null || domaine.isEmpty()) {
      return List.of();
    }
    String suffix = "." + domaine;
    return byName.values().stream()
        .filter(it -> {
          String n = it.getNom().toString();
          return n.endsWith(suffix) || n.equals(domaine);
        })
        .sorted(Comparator.comparing(it -> it.getNom().toString()))
        .collect(Collectors.toList());
  }

  /**
   * Ajoute une nouvelle entrée (vérifie l'unicité IP et FQDN) puis persiste.
   * @throws IllegalArgumentException si IP ou nom existent déjà
   */
  public void addItem(AdresseIP ip, NomMachine nom) {
    addInternal(ip, nom, true);
    // Si le fichier n'existe pas encore, il sera créé ici
    persist();
  }
}

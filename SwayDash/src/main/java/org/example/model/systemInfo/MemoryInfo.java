package org.example.model.systemInfo;

public record MemoryInfo(
        float free,
        float total
) {
  public String freeGB() {
    return String.format("%.1f GB", free);
  }

  public String totalGB() {
    return String.format("%.1f GB", total);
  }

  public String percentageFree() {
    return String.format("%.1f", (free / total) * 100) + "%";
  }
}
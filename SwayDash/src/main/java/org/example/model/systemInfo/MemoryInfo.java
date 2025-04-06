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
    float freeSpace = (free / total) * 100;
    return freeSpace >= 100 ? "100%" : String.format("%.1f", freeSpace) + "%";
  }
}
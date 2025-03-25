package org.example.model;

import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PowerSource;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

public class SystemInfo {
  private static final oshi.SystemInfo systemInfo = new oshi.SystemInfo();
  private static final HardwareAbstractionLayer hal = systemInfo.getHardware();

  public static String getBatteryRemainingTime() {
    PowerSource battery = hal.getPowerSources().getFirst();
    long seconds = (long) battery.getTimeRemainingInstant();
    long minutes = seconds / 60;
    long hours = minutes / 60;
    minutes = minutes % 60;
    return String.format("%02d:%02d", hours, minutes);
  }

  public String getBatteryPercentage(boolean noRemaining) {
    PowerSource battery = hal.getPowerSources().getFirst();

    float current = battery.getCurrentCapacity();
    float max = battery.getMaxCapacity();
    float percentage = (current / max) * 100;
    boolean isCharging = !battery.isDischarging();

    String batCharging = (isCharging ? "\uD83D\uDD0C" : "🔋");
    String remaining = percentage >= 100 || noRemaining ? "" : "(" + getBatteryRemainingTime() + ")";
    String format = percentage >= 99.9 ? " %.0f" : " %.1f";
    return batCharging + String.format(format, percentage) + "% " + remaining;
  }

  public HardDriveInfo getHDSpace(String partition) {
    String totalSpace = "";
    String freeSpace = "";

    oshi.SystemInfo si = new oshi.SystemInfo();
    OperatingSystem os = si.getOperatingSystem();
    FileSystem fs = os.getFileSystem();

    for (OSFileStore store : fs.getFileStores()) {
      if (store.getMount().equals(partition + ":\\")) {
        totalSpace = String.format("%.1f GB", store.getTotalSpace() / (1024.0 * 1024 * 1024));
        freeSpace = String.format("%.1f GB", store.getUsableSpace() / (1024.0 * 1024 * 1024));
      }
    }
    return new HardDriveInfo(freeSpace, totalSpace);
  }

  public String getFreeRAM() {
    long freeMemory = hal.getMemory().getAvailable();
    return "\uD83E\uDEAE " + String.format("%.1f GB", freeMemory / (1024.0 * 1024 * 1024));
  }
}


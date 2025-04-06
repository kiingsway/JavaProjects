package org.example.model.systemInfo;

import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PowerSource;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

public class SystemInfo {

  private static final float BYTE_TO_GB = 1024.0f * 1024 * 1024;

  private final oshi.SystemInfo systemInfo = new oshi.SystemInfo();
  private final HardwareAbstractionLayer hal = systemInfo.getHardware();

  public String getBatteryRemainingTime() {
    PowerSource battery = hal.getPowerSources().getFirst();
    long seconds = (long) battery.getTimeRemainingInstant();
    long minutes = seconds / 60;
    long hours = minutes / 60;
    minutes = minutes % 60;
    return String.format("%02d:%02d", hours, minutes);
  }

  public boolean isBatteryCharging() {
    PowerSource battery = hal.getPowerSources().getFirst();
    return !battery.isDischarging();
  }

  public String getBatteryPercentage() {
    PowerSource battery = hal.getPowerSources().getFirst();

    float current = battery.getCurrentCapacity();
    float max = battery.getMaxCapacity();
    float percentage = (current / max) * 100;
    if (percentage >= 99.98) return "100%";
    else return String.format("%.1f%%", percentage);
  }

  public MemoryInfo getHDInfo(String partition) {
    float total = 0, free = 0;

    FileSystem fs = systemInfo.getOperatingSystem().getFileSystem();

    for (OSFileStore store : fs.getFileStores()) {
      if (store.getMount().equals(partition + ":\\")) {
        total = store.getTotalSpace() / BYTE_TO_GB;
        free = store.getUsableSpace() / BYTE_TO_GB;
      }
    }
    return new MemoryInfo(free, total);
  }

  public MemoryInfo getRAMInfo() {
    float free = hal.getMemory().getAvailable() / BYTE_TO_GB;
    float total = hal.getMemory().getTotal() / BYTE_TO_GB;

    return new MemoryInfo(free, total);
  }
}


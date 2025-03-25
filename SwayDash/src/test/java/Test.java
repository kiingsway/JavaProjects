import javax.swing.*;
import java.awt.*;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PowerSource;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

public class Test extends JFrame {

  private final SystemInfo systemInfo = new SystemInfo();
  private final HardwareAbstractionLayer hal = systemInfo.getHardware();
  private PowerSource battery = hal.getPowerSources().getFirst();

  String[] textList = {};

  private int index = -1;
  private final JLabel lblFunction = new JLabel(" ");
  private final JTextArea txtResult = new JTextArea();
  private final JButton btnNext = new JButton("Next");

  public Test() {
    setTitle("Test");
    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    lblFunction.setFont(new Font("Tahoma", Font.PLAIN, 25));

    txtResult.setWrapStyleWord(true);
    txtResult.setLineWrap(true);

    setTexts();
    btnNext.addActionListener(_ -> setTexts());

    add(lblFunction, BorderLayout.NORTH);
    add(new JScrollPane(txtResult), BorderLayout.CENTER);
    add(btnNext, BorderLayout.SOUTH);

    setVisible(true);
  }

  private void setTexts() {
    index++;
    if (index == textList.length) index = 0;

    battery = hal.getPowerSources().getFirst();

    String batteryRemainingTime = getBatteryRemainingTime();
    String batteryPercentage = getBatteryPercentage();
    boolean isCharging = battery.isCharging();

    String[] HDSpace = getHDSpace("C");
    String totalSpace = HDSpace[0];
    String freeSpace = HDSpace[1];

    String all = "Tempo restante: " + batteryRemainingTime + "\nBateria: " + batteryPercentage + "\nCarregando? " + (isCharging ? "Sim" : "Não") + "\nEspaço livre: " + freeSpace + "\nEspaço Total: " + totalSpace + "\n";

    textList = new String[]{all, hal.getDiskStores().toString()};

    String[] titleList = {"all", "getDiskStores"};

    txtResult.setText(textList[index]);
    lblFunction.setText(titleList[index]);
  }

  private String getBatteryRemainingTime() {
    long seconds = (long) battery.getTimeRemainingInstant();
    long minutes = seconds / 60;
    long hours = minutes / 60;
    minutes = minutes % 60;
    return String.format("%02d:%02d", hours, minutes);
  }

  private String getBatteryPercentage() {
    float current = battery.getCurrentCapacity();
    float max = battery.getMaxCapacity();
    return String.format("%.1f", ((current / max) * 100)) + "%";
  }

  private String[] getHDSpace(String partition) {
    String totalSpace = "";
    String freeSpace = "";

    SystemInfo si = new SystemInfo();
    OperatingSystem os = si.getOperatingSystem();
    FileSystem fs = os.getFileSystem();

    for (OSFileStore store : fs.getFileStores()) {
      if (store.getMount().equals(partition + ":\\")) {
        totalSpace = String.format("%.1f GB", store.getTotalSpace() / (1024.0 * 1024 * 1024));
        freeSpace = String.format("%.1f GB", store.getUsableSpace() / (1024.0 * 1024 * 1024));
      }
    }

    return new String[]{totalSpace, freeSpace};
  }

  public static void main(String[] args) {
    new Test();
  }
}

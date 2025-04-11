package org.example.view.components.system;

import org.example.model.components.ThemedPanel;
import org.example.model.log.LogItem;
import org.example.model.log.LogItemLevel;
import org.example.model.systemInfo.SystemInfo;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class SystemInfoPanel extends JPanel implements ThemedPanel {

  private final SystemInfo sys = new SystemInfo();

  private static final IconLabelPanel lblBattery = new IconLabelPanel();
  private static final IconLabelPanel lblHD = new IconLabelPanel();
  private static final IconLabelPanel lblRAM = new IconLabelPanel();
  private final Consumer<LogItem> addLog;

  public SystemInfoPanel(boolean isDarkMode, Consumer<LogItem> addLog) {
    this.addLog = addLog;
    try {
      setTheme(isDarkMode);

      setLayout(null);

      int height = 35;
      int row = 0;
      lblBattery.setBounds(0, 0, 250, height);
      row++;
      lblHD.setBounds(0, row * height, 250, height);
      row++;
      lblRAM.setBounds(0, row * height, 250, height);

      add(lblBattery);
      add(lblHD);
      add(lblRAM);

      updateValues();
      Timer timer = new Timer(1000, e -> updateValues());
      timer.start();
    } catch (Exception e) {
      addLog.accept(new LogItem(LogItemLevel.ERROR, this.getClass().getSimpleName(), e));
    }
  }

  private void updateValues() {
    try {
      String iconBattery = sys.isBatteryCharging() ? "\uD83D\uDD0C" : "🔋";
      String textBattery = sys.getBatteryPercentage();
      String text2Battery = sys.getBatteryRemainingTime();
      lblBattery.setTexts(iconBattery, textBattery, text2Battery);

      String percentageFreeHD = sys.getHDInfo("C").percentageFree();
      String freeGBHD = sys.getHDInfo("C").freeGB();
      lblHD.setTexts("🖥️", percentageFreeHD, freeGBHD);

      String percentageFreeRAM = sys.getRAMInfo().percentageFree();
      String freeGBRAM = sys.getRAMInfo().freeGB();
      lblRAM.setTexts("🪮", percentageFreeRAM, freeGBRAM);
    } catch (Exception e) {
      addLog.accept(new LogItem(LogItemLevel.ERROR, this.getClass().getSimpleName(), e));
    }
  }

  public void setTheme(boolean isDarkMode) {
    Color foreground = isDarkMode ? Color.LIGHT_GRAY : Color.DARK_GRAY;
    Color background = isDarkMode ? Color.BLACK : Color.WHITE;

    Component[] backgroundComponents = {this};
    Component[] foregroundComponents = {lblBattery, lblHD, lblRAM};

    for (Component comp : backgroundComponents) comp.setBackground(background);
    for (Component comp : foregroundComponents) comp.setForeground(foreground);

    revalidate();
    repaint();
  }
}

package org.example.controller;

import com.sun.jna.platform.win32.Kernel32;

public class PowerManager {

  private static final int ES_CONTINUOUS = 0x80000000;
  private static final int ES_SYSTEM_REQUIRED = 0x00000001;
  private static final int ES_DISPLAY_REQUIRED = 0x00000002;

  public static void preventSleep() {
    Kernel32.INSTANCE.SetThreadExecutionState(ES_CONTINUOUS | ES_SYSTEM_REQUIRED | ES_DISPLAY_REQUIRED);
  }

  public static void allowSleep() {
    Kernel32.INSTANCE.SetThreadExecutionState(ES_CONTINUOUS);
  }
}

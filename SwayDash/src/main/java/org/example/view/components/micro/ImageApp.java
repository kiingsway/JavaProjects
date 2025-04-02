package org.example.view.components.micro;

import javax.swing.*;
import java.net.URI;
import java.net.URL;

public class ImageApp extends JLabel {

  public ImageApp() {
  }

  public ImageApp(String url) {
    setImage(url);
  }

  public ImageApp(ImageIcon icon) {
    setIcon(icon);
  }

  @SuppressWarnings("CallToPrintStackTrace")
  private void setImage(String url) {
    if (url == null) return;
    try {
      URI uri = new URI(url);
      URL imageUrl = uri.toURL();
      ImageIcon imageIcon = new ImageIcon(imageUrl);
      setIcon(imageIcon);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

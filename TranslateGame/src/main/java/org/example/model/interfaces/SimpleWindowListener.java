package org.example.model.interfaces;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public interface SimpleWindowListener extends WindowListener {
    void onWindowClosing(WindowEvent e);

    @Override
    default void windowOpened(WindowEvent e) {}

    @Override
    default void windowClosing(WindowEvent e) {onWindowClosing(e);}

    @Override
    default void windowClosed(WindowEvent e) {}

    @Override
    default void windowIconified(WindowEvent e) {}

    @Override
    default void windowDeiconified(WindowEvent e) {}

    @Override
    default void windowActivated(WindowEvent e) {}

    @Override
    default void windowDeactivated(WindowEvent e) {}
}

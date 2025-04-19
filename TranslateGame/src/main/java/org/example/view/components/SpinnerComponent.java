package org.example.view.components;

import org.example.Constants;

import javax.swing.*;

public class SpinnerComponent extends JSpinner {
    public SpinnerComponent () {
        int value = Constants.INITIAL_N_QUESTIONS;
        int maxSize = Constants.MAX_QUESTIONS;
        SpinnerNumberModel snm = new SpinnerNumberModel(value, 1, maxSize, 1);
        setModel(snm);
    }
}
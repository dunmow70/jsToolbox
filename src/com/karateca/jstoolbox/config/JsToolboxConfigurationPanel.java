package com.karateca.jstoolbox.config;

import javax.swing.*;

/**
 * @author Andres Dominguez.
 */
public class JsToolboxConfigurationPanel {
  private JTextField unitTestSuffix;

  public JPanel getMyPanel() {
    return myPanel;
  }

  private JPanel myPanel;
  private JTextField fileSuffix;
  private JTextField viewSuffix;
  private JButton resetButton;

  public void setTestSuffix(String testSuffix) {
    unitTestSuffix.setText(testSuffix);
  }

  public String getTestSuffix() {
    return unitTestSuffix.getText();
  }

  public String getFileSuffix() {
    return fileSuffix.getText();
  }

  public void setFileSuffix(String suffix) {
    fileSuffix.setText(suffix);
  }

  public String getViewSuffix() {
    return viewSuffix.getText();
  }

  public void setViewSuffix(String suffix) {
    viewSuffix.setText(suffix);
  }

  public JButton getResetButton() {
    return resetButton;
  }
}

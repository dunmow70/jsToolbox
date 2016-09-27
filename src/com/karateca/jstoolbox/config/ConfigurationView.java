package com.karateca.jstoolbox.config;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Andres Dominguez.
 */
public class ConfigurationView {
  private JPanel goToRelatedPanel;
  private JPanel openInBrowserPanel;
  private JButton resetButton;
  private JTextField unitTestSuffix;
  private JTextField testFolder;
  private JTextField sourceFolder;
  private JTextField fileSuffix;
  private JTextField searchUrl;
  private JCheckBox useFilePath;
  private JTextField fromPath;
  private JPanel myPanel;
  private JTextField viewSuffix;
  private JButton saveButton;
  private JComboBox<String> templateNames;

  public ConfigurationView() {
    useFilePath.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        fromPath.setEnabled(useFilePath.isSelected());
      }
    });
  }

  public JPanel getMyPanel() {
    return myPanel;
  }

  public void setTestFolder(String value) { testFolder.setText(value); }
  public String getTestFolder() { return testFolder.getText(); }

  public void setSourceFolder(String value) { sourceFolder.setText(value); }
  public String getSourceFolder() { return sourceFolder.getText(); }

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

  public String getSearchUrl() {
    return searchUrl.getText();
  }

  public void setSearchUrl(String url) {
    searchUrl.setText(url);
  }

  public String getFromPath() {
    return fromPath.getText();
  }

  public void setFromPath(String dir) {
    fromPath.setText(dir);
  }

  public boolean getUseFilePath() {
    return useFilePath.isSelected();
  }

  public void setUseFilePath(boolean selected) {
    useFilePath.setSelected(selected);
    fromPath.setEnabled(selected);
  }

  // We will attach listeners to the reset and save buttons in the JsToolboxConfigurable class.
  public JButton getResetButton() {
    return resetButton;
  }

  public JButton getSaveButton() { return saveButton; }

  public String getSelectedTemplateName() {
    return templateNames.getModel().getSelectedItem().toString();
  }

  public void setSelectedTemplateName(String templateName) {
    this.templateNames.getModel().setSelectedItem(templateName);
  }

  public void setTemplateNames(String[] templateNames) {
    for (String templateName : templateNames) {
      if (this.templateNames != null) {
        this.templateNames.addItem(templateName);
      }
    }
  }

}

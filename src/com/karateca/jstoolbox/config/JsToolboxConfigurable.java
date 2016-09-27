package com.karateca.jstoolbox.config;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.impl.FileTemplateManagerImpl;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.intellij.openapi.project.Project;
import org.apache.log4j.Logger;

import static com.karateca.jstoolbox.config.JsToolboxSettings.Property;

/**
 * @author Andres Dominguez.
 */
public class JsToolboxConfigurable implements Configurable {

    private ConfigurationView view;
    private final JsToolboxSettings settings;
    private Project myProject = null;

    public JsToolboxConfigurable() {
        settings = new JsToolboxSettings();
    }

    public JsToolboxConfigurable(@NotNull Project project) {
        settings = new JsToolboxSettings();
        myProject = project;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "JS Toolbox";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "Configure the default settings for the JS Toolbox";
    }

    /**
     * Get an array of the file template names.
     * @return String[] A string array of the available template names.
     */
    private String[] getFileTemplates() {
        Logger logger = Logger.getLogger("FILE");
        logger.info("Getting file templates");
        FileTemplateManagerImpl ftManager = FileTemplateManagerImpl.getInstanceImpl(myProject);
        FileTemplate[] fileTemplates = ftManager.getAllTemplates();
        String[] templateNames = new String[fileTemplates.length];
        for (int i = 0; i < fileTemplates.length; i++) {
            templateNames[i] = fileTemplates[i].getName();
        }
        logger.info(templateNames);
        return templateNames;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (view == null) {
            view = new ConfigurationView();
        }

        // Reset on click.
        view.getResetButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                view.setTestSuffix(
                        Property.TestSuffix.getDefaultValue());
                view.setViewSuffix(
                        Property.ViewSuffix.getDefaultValue());
                view.setFileSuffix(
                        Property.FileSuffix.getDefaultValue());
                view.setTestFolder(
                        Property.TestFolder.getDefaultValue());
                view.setSourceFolder(
                        Property.SourceFolder.getDefaultValue());
                view.setSearchUrl(
                        Property.SearchUrl.getDefaultValue());
                view.setUseFilePath(
                        Boolean.parseBoolean(
                                Property.UseFilePath.getDefaultValue()));
                view.setFromPath(
                        Property.FromPath.getDefaultValue());
                view.setTemplateNames(getFileTemplates());
            }
        });

        view.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    apply();
                } catch (ConfigurationException e) {
                    e.printStackTrace();
                }
            }
        });

        reset();

        return view.getMyPanel();
    }

    @Override
    public boolean isModified() {
        return !StringUtils.equals(settings.getTestSuffix(), view.getTestSuffix()) ||
                !StringUtils.equals(settings.getViewSuffix(), view.getViewSuffix()) ||
                !StringUtils.equals(settings.getTestFolder(), view.getTestFolder()) ||
                !StringUtils.equals(settings.getSourceFolder(), view.getSourceFolder()) ||
                !StringUtils.equals(settings.getFileSuffix(), view.getFileSuffix()) ||
                !StringUtils.equals(settings.getSearchUrl(), view.getSearchUrl()) ||
                !StringUtils.equals(settings.getFromPath(), view.getFromPath()) ||
                settings.getUseFilePath() != view.getUseFilePath();
    }

    @Override
    public void apply() throws ConfigurationException {
        settings.setTestSuffix(view.getTestSuffix());
        settings.setFileSuffix(view.getFileSuffix());
        settings.setTestFolder(view.getTestFolder());
        settings.setSourceFolder(view.getSourceFolder());
        settings.setViewSuffix(view.getViewSuffix());
        settings.setSearchUrl(view.getSearchUrl());
        settings.setUseFilePath(view.getUseFilePath());
        settings.setFromPath(view.getFromPath());
        settings.setFileTemplate(view.getSelectedTemplateName());
        settings.save();
    }

    @Override
    public void reset() {
        settings.load();
        view.setTestSuffix(settings.getTestSuffix());
        view.setViewSuffix(settings.getViewSuffix());
        view.setTestFolder(settings.getTestFolder());
        view.setSourceFolder(settings.getSourceFolder());
        view.setFileSuffix(settings.getFileSuffix());
        view.setSearchUrl(settings.getSearchUrl());
        view.setUseFilePath(settings.getUseFilePath());
        view.setFromPath(settings.getFromPath());

        // Get the template names for the dropdown before setting the selected value.
        view.setTemplateNames(getFileTemplates());
        view.setSelectedTemplateName(settings.getFileTemplate());
    }

    @Override
    public void disposeUIResources() {
        view = null;
    }
}

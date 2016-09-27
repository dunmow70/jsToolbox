package com.karateca.jstoolbox.config;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.karateca.jstoolbox.MyAction;
import javax.swing.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.actionSystem.DataKeys;

/**
 * @author Tim Hancock
 */
public class ConfigurationViewAction extends MyAction {

    private JFrame frame;

    public void actionPerformed(AnActionEvent actionEvent) {

        // Get the project. If one can't be found, return early. We need the project to determine the file templates.
        Project project = actionEvent.getProject();
        if (project == null) {
            return;
        }

        if (frame == null) {
            JsToolboxConfigurable config = new JsToolboxConfigurable(project);

            // Create a frame to place the config form in.
            frame = new JFrame("JS Toolbox Configuration Options");

            //  Add the config form to the frame.
            frame.add(config.createComponent());

            // The frame should hide when the close button is clicked.
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

            // Set the width and height
            frame.setSize(800, 550);
        }

        // Make the frame visible.
        frame.setVisible(true);

    }

}

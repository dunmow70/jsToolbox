package com.karateca.jstoolbox.file;

import com.intellij.openapi.vfs.VirtualFileManager;
import com.karateca.jstoolbox.config.JsToolboxSettings;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.intellij.ide.fileTemplates.FileTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Tim Hancock
 */
public class FileCreator {

    /**
     * Creates a file in the location passed if one does not already exist. If a FileTemplate is passed, the
     * contents of the template are parsed for placeholders and written to the file.
     * @param fileTemplate The file template to write to the file.
     * @param fileLocation The file location object telling us where to write the file and what name to save it as.
     */
    public void createFileIfNotExists(@Nullable FileTemplate fileTemplate, @NotNull FileLocation fileLocation) {

        // Create the file.
        File file = createFileIfNotExists(fileLocation);

        // If there is no file template, don't attempt to write one to the file.
        if (fileTemplate == null) {
            return;
        }

        // Now write the template text to the file, replacing any known placeholders.
        try {
            FileWriter writer = new FileWriter(file);
            JsToolboxSettings settings = new JsToolboxSettings();

            // Get the test name as the folder path and file name, minus the extension.
            String testName = fileLocation.getFolderPath().replace(settings.getTestFolder(), "") + fileLocation.getFileName().replace(settings.getTestSuffix(), "");

            // Replace the ${NAME} placeholder with the test name.
            String fileText = fileTemplate.getText().replace("${NAME}", testName);

            // Write the text and close the file.
            writer.write(fileText);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates a file in the location passed if one does not already exist.
     * @param fileLocation The file location information object.
     * @return The File that has been created.
     */
    public File createFileIfNotExists(@NotNull FileLocation fileLocation) {

        // If this is a test file, we do not want to create the original source version.
        if (fileLocation.isTestFile()) {
            return null;
        }

        // Replace the source folder path with the target folder path.
        JsToolboxSettings settings = new JsToolboxSettings();
        fileLocation.setFolderPath(fileLocation.getFolderPath().replace(settings.getSourceFolder(), settings.getTestFolder()));
        fileLocation.setFileName(fileLocation.getFileName().replace(settings.getFileSuffix(), settings.getTestSuffix()));

        // Get the file instance.
        File file = new File(fileLocation.toString());

        // Make the parent directory.
        file.getParentFile().mkdirs();

        // If the file does not exist, create it.
        if (!file.exists()) {
            try {
                // Create the file.
                file.createNewFile();

                // Refresh the file system view.
                VirtualFileManager.getInstance().syncRefresh();

            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

}

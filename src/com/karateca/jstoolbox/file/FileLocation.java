package com.karateca.jstoolbox.file;

import com.karateca.jstoolbox.config.JsToolboxSettings;

/**
 * @author Tim Hancock
 */
public class FileLocation {

    private String fileName;
    private String folderPath;
    private String baseDir;
    private boolean isTestFile = false;

    public FileLocation(String fileName, String folderPath, String baseDir) {
        this.setFileName(fileName);
        this.setFolderPath(folderPath);
        this.setBaseDir(baseDir);
        this.setIsTestFile();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
        this.setIsTestFile();
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public boolean isTestFile() {
        return isTestFile;
    }

    /**
     * Check the folder path the file is contained in. If it matches the settings test folder, mark this as a test file.
     */
    protected void setIsTestFile() {
        JsToolboxSettings settings = new JsToolboxSettings();
        if (folderPath.startsWith(settings.getTestFolder())) {
            this.isTestFile = true;
        }
    }

    public String toString() {
        return this.getBaseDir() + this.getFolderPath() + this.getFileName();
    }
}

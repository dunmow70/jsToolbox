package com.karateca.jstoolbox.torelated;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.impl.FileTemplateManagerImpl;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.karateca.jstoolbox.MyAction;
import com.karateca.jstoolbox.config.JsToolboxSettings;
import com.karateca.jstoolbox.file.FileLocation;
import com.karateca.jstoolbox.file.FileCreator;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.karateca.jstoolbox.torelated.CandidateFinder.suggestDestinationFiles;

/**
 * foo-controller.js to vie
 * <p>
 * foo-controller * view suffixes
 * foo * view suffixes
 */

abstract class GoToRelatedAction extends MyAction {

    List<String> fileSuffixList;
    List<String> viewSuffixList;
    List<String> testSuffixList;

    @Override
    public void actionPerformed(AnActionEvent event) {
        // Disable if dependencies are not met.
        if (!canEnableAction(event)) {
            return;
        }

        readConfig();
        performSwitch(event, getCurrentFileName(event));
    }

    void performSwitch(AnActionEvent event, String fileName) {
        List<String> fromSuffixes = suffixesForType(getFileType(fileName));
        List<String> toSuffixes = suffixesForType(getNavigateTo(fileName));

        // If the test file does not exist, create one in the /test folder.
        FileCreator fileCreator = new FileCreator();
        fileCreator.createFileIfNotExists(getFileTemplate(event), this.getFileLocation(event));

        // Navigate to the test file.
        goToFiles(event, suggestDestinationFiles(fileName, fromSuffixes, toSuffixes));
    }

    /**
     * Returns the file template for the currently selected default file template.
     * @param event The ActionEvent that started this process. Used to get the Project instance.
     * @return The File Template to use when creating the test file.
     */
    FileTemplate getFileTemplate(@NotNull AnActionEvent event) {

        // Get the current project. This is required to allow us to get the file templates.
        Project project = event.getProject();

        // Return early if there is not project.
        if (project == null) {
            return null;
        }

        // Get the file templates for this project
        FileTemplateManagerImpl ftManager = FileTemplateManagerImpl.getInstanceImpl(project);
        FileTemplate[] fileTemplates = ftManager.getAllTemplates();

        // Get the template name from the setting
        JsToolboxSettings settings = new JsToolboxSettings();
        String templateName = settings.getFileTemplate();

        // Find the matching template
        for (FileTemplate fileTemplate : fileTemplates) {
            if (fileTemplate != null && fileTemplate.getName().equals(templateName)) {
                return fileTemplate;
            }
        }

        // No matching template name found, return null.
        return null;
    }

    /**
     * Returns the folder path
     * @param event The action event containing the file that was clicked on.
     * @return FileLocation
     */
    FileLocation getFileLocation(AnActionEvent event) {
        // Get the file from the event.
        Project project = event.getProject();
        PsiFile file = event.getData(LangDataKeys.PSI_FILE);
        String filePath = getCurrentFilePath(event); // Gives me the full file path starting from the machine's root. For example C:/wamp/..../some_file.js
        String basePath = project.getBasePath(); // Gives us the project's root path. For example, C:/wamp
        String fileName = file.getName(); // Gives me the file name. For example, some_file.js

        // If we have no base path or no file name, return early.
        if (basePath == null || fileName == null) {
            return null;
        }

        // Get the folder path.
        String folderPath = filePath.replace(basePath, "").replace(fileName, "");

        // Add the information to a file location object.
        return new FileLocation(fileName, folderPath, basePath);
    }

    List<String> suffixesForType(FileType fileType) {
        if (fileType == FileType.FILE) {
            return fileSuffixList;
        }

        if (fileType == FileType.TEST) {
            return testSuffixList;
        }

        return viewSuffixList;
    }

    abstract FileType getNavigateTo(String fileName);

    void readConfig() {
        JsToolboxSettings settings = new JsToolboxSettings();

        fileSuffixList = Arrays.asList(settings.getFileSuffix().split(","));
        viewSuffixList = Arrays.asList(settings.getViewSuffix().split(","));
        testSuffixList = Arrays.asList(settings.getTestSuffix().split(","));
    }

    boolean isViewFile(String fileName) {
        return endsWithAnyOf(fileName, viewSuffixList);
    }

    boolean isTestFile(String fileName) {
        return endsWithAnyOf(fileName, testSuffixList);
    }

    FileType getFileType(String fileName) {
        if (isViewFile(fileName)) {
            return FileType.VIEW;
        }

        if (isTestFile(fileName)) {
            return FileType.TEST;
        }

        return FileType.FILE;
    }

    private boolean endsWithAnyOf(String fileName, List<String> destinationSuffix) {
        for (String suffix : destinationSuffix) {
            if (fileName.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    void goToFiles(AnActionEvent e, List<String> destinationFiles) {
        String fileName = getCurrentFileName(e);
        String filePath = getCurrentFilePath(e);

        for (String goToFileName : destinationFiles) {
            List<PsiFile> files = findFilesInProjectWithName(goToFileName, e.getProject());

            if (files.size() == 1) {
                files.get(0).navigate(true);
            } else if (files.size() > 1) {
                // There is more that one match. Look for the closest match.
                Map<String, PsiFile> filesByPath = groupFilesByPath(files);
                String targetPath = filePath.replace(fileName, goToFileName);
                String longest = LongestSuffix.find(filesByPath.keySet(), targetPath);
                if (longest != null) {
                    filesByPath.get(longest).navigate(true);
                }
            }
        }
    }

    @NotNull
    private Map<String, PsiFile> groupFilesByPath(List<PsiFile> files) {
        Map<String, PsiFile> filesByPath = new HashMap<>();
        for (PsiFile file : files) {
            String path = file.getVirtualFile().getCanonicalPath();
            filesByPath.put(path, file);
        }
        return filesByPath;
    }

    List<PsiFile> findFilesInProjectWithName(String fileName, Project project) {
        FindRelatedFileIterator iterator =
                new FindRelatedFileIterator(fileName, PsiManager.getInstance(project));

        ProjectRootManager.getInstance(project).getFileIndex().iterateContent(iterator);

        return iterator.getFiles();
    }
}

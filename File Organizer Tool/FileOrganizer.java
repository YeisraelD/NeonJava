

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileOrganizer {
    private List<String> fileNames;
    private Map<String, List<File>> groupedFiles;

    public FileOrganizer() {
        this.fileNames = new ArrayList<>();
        this.groupedFiles = new HashMap<>();
    }

    /**
     * Scans the specified directory for files.
     * @param directoryPath The path to the directory to scan.
     */
    public void scanDirectory(String directoryPath) {
        File folder = new File(directoryPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Invalid directory path: " + directoryPath);
            return;
        }

        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isFile()) {
                fileNames.add(file.getName());
                groupFileByExtension(file);
            }
        }
    }

    /**
     * Groups a file by its extension.
     * @param file The file to group.
     */
    private void groupFileByExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        String extension = (dotIndex == -1) ? "no_extension" : fileName.substring(dotIndex + 1).toLowerCase();

        groupedFiles.computeIfAbsent(extension, k -> new ArrayList<>()).add(file);
    }

    /**
     * Moves grouped files into directories named after their extensions.
     * @param targetDirectory The directory where subfolders will be created.
     */
    public void organizeFiles(String targetDirectory) {
        for (Map.Entry<String, List<File>> entry : groupedFiles.entrySet()) {
            String extension = entry.getKey();
            List<File> files = entry.getValue();

            Path extensionDirPath = Paths.get(targetDirectory, extension);
            try {
                if (!Files.exists(extensionDirPath)) {
                    Files.createDirectories(extensionDirPath);
                }

                for (File file : files) {
                    Path sourcePath = file.toPath();
                    Path targetPath = extensionDirPath.resolve(file.getName());
                    Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Moved: " + file.getName() + " -> " + extension);
                }
            } catch (IOException e) {
                System.err.println("Error processing extension " + extension + ": " + e.getMessage());
            }
        }
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java FileOrganizer <directory_path>");
            return;
        }

        String path = args[0];
        FileOrganizer organizer = new FileOrganizer();
        System.out.println("Scanning directory: " + path);
        organizer.scanDirectory(path);

        System.out.println("Files found: " + organizer.getFileNames().size());
        for (String name : organizer.getFileNames()) {
            System.out.println(" - " + name);
        }

        System.out.println("\nOrganizing files...");
        organizer.organizeFiles(path);
        System.out.println("Done!");
    }
}

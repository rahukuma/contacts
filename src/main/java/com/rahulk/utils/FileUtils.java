package com.rahulk.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Rahul Kumar on 13-05-16.
 */

public class FileUtils {
    // Using servlet parent directory as contact data storage
    public static final String PARENT_DATA_DIR = System.getProperty("user.dir") + File.separator  +"contacts";

    public void save(String message, String filePath, String fileName) {
        Path path = null;
        if (filePath != null) {
            path = Paths.get(filePath);
        }
        try {
            if (filePath != null) {
                if (Files.notExists(path)) {
                    Files.createDirectory(path);
                }
                path = Paths.get(filePath + File.separator + fileName);
            } else {
                path = Paths.get(fileName);
            }
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            Files.write(path, message.getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    /**
     * @param match search criteria by which file name is searched
     * @return
     */
    public boolean exists(final String match) {
        if (getFiles(match).length == 0) return false;
        return true;

    }

    public File[] getFiles(final String match) {
        File file = new File(PARENT_DATA_DIR);
        File[] files = new File[0];
        if (file.exists()) {
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File file, String filename) {
                    if (match == null) {
                        return filename.contains(filename);
                    } else {
                        return filename.contains(match);
                    }
                }
            };
            files = file.listFiles(filter);
        }
        return files;
    }

    /**
     * @param filePath
     * @return content of file as string
     */
    public String read(String filePath) {
        try {
            return org.apache.commons.io.FileUtils.readFileToString(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes file as per supplied arg
     *
     * @param file
     */
    public void delete(String file) {
        Path path = Paths.get(PARENT_DATA_DIR + File.separator + file);
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

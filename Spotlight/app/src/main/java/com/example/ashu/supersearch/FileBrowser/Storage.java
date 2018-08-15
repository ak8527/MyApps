package com.example.ashu.supersearch.FileBrowser;

public class Storage {
    private final String fileName;
    private final String filePath;

    public Storage(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }
}

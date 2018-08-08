package com.example.ashu.supersearch.FileBrowser;

public class Storage {
    String fileName , filePath;

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

package CompositePattern.v1;

import java.util.ArrayList;
import java.util.List;

import static CompositePattern.utils.ValidationUtils.shouldMatch;

public class Directory {
    private Directory parent;
    private final String name;
    private final List<File> fileChildren = new ArrayList<>();
    private final List<Directory> dirChildren = new ArrayList<>();

    //應該只是明確地給出root要建立的提示方法?
    public static Directory root()
    {
        return new Directory("root");
    };

    public Directory(String name){
        this.name = shouldMatch("[A-Za-z0-9.\\-_]+", name);
    }

    public void addFile(File file) {
        fileChildren.add(file);
        file.setParent(this);
    }

    public void addDirectory(Directory directory) {
        dirChildren.add(directory);
        directory.setParent(this);
    }

    public File getFile(String name) {
        for(File file : fileChildren) {
            if(file.getName().equals(name)) {
                return file;
            }
        }
        return null;
    }

    public Directory getDirectory(String name) {
        for(Directory dir : dirChildren) {
            if(dir.name.equals(name)) {
                return dir;
            }
        }
        return null;
    }

    public long totalBytes() {
        long total = 0;
        for (Directory dir: dirChildren) {
            total += dir.totalBytes();
        }
        for(File file: fileChildren) {
            total += file.bytes();
        }

        return total;
    }

    public List<File> searchFiles(String keyword){
        List<File> files = new ArrayList<>();
        for(File file:fileChildren) {
            if(file.getName().contains(keyword)) {
                files.add(file);
            }
        }
        return files;
    }

    public List<Directory> searchDirectories(String keyword){
        List<Directory> directories = new ArrayList<>();
        for(Directory dirChild : dirChildren) {
            if(dirChild.getName().contains(keyword)) {
                directories.add(dirChild);
            }
        }
        return directories;
    }

    private void setParent(Directory parent) {
        this.parent = parent;
    }

    public Directory getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }
}

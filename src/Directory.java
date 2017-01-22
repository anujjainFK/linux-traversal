import java.util.ArrayList;
import java.util.List;

public class Directory {
    private String name;

    public List<Directory> getChildDirectories() {
        return childDirectories;
    }

    public String getName() {
        return name;
    }

    public Directory getParent() {
        return parent;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<Directory> childDirectories;
    private Directory parent;

    public Directory(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

    public void addChildDirectory(Directory child) {
        if (childDirectories == null)
            childDirectories = new ArrayList<>();
        this.childDirectories.add(child);
    }
}

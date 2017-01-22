import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Terminal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Application started...");

        String inputCommand = "";
        Directory rootDirectory = getNewRootDirectory();
        Directory currentDirectory = rootDirectory;
        while (!inputCommand.equals("exit")) {
            System.out.print("$");
            String inputString = scanner.nextLine();
            String[] inputStringArray = inputString.split(" ");
            String[] parameters = null;
            if (inputStringArray.length > 1)
                parameters = Arrays.copyOfRange(inputStringArray, 1, inputStringArray.length);
            inputCommand = inputStringArray[0];
            switch (inputCommand) {
                case Commands.MKDIR:
                    if (parameters == null || parameters.length == 0)
                        System.out.println("empty parameter list");
                    else {
                        makeDirectories(currentDirectory, parameters);
                    }
                    break;
                case Commands.LS:
                    listDirectory(currentDirectory);
                    break;
                case Commands.RM:
                    removeDirectory(currentDirectory, parameters[0], rootDirectory);
                    break;
                case Commands.CD:
                    currentDirectory = changeDirectory(currentDirectory, parameters[0], rootDirectory);
                    break;
                default:
                    System.out.println("ERR: CANNOT RECOGNIZE INPUT");
            }
            System.out.println();
        }

    }

    private static Directory changeDirectory(Directory currentDirectory, String parameter, Directory rootDirectory) {
        if (parameter.startsWith("/")) {
            Directory directory = getDirectoryWithPath(parameter, rootDirectory);
            return directory;
        } else {
            return getChildWithName(parameter, currentDirectory);
        }
    }

    private static void removeDirectory(Directory currentDirectory, String parameter, Directory rootDirectory) {
        if (parameter.startsWith("/")) {
            Directory directory = getDirectoryWithPath(parameter, rootDirectory);
            String [] childrenDirectories = parameter.split("/");
            parameter = childrenDirectories[childrenDirectories.length-1];
            if (directory != null || directory.getParent() != null)
                currentDirectory = directory.getParent();
        }

        if (currentDirectory.getChildDirectories() == null || currentDirectory.getChildDirectories().size() == 0) {
            System.out.print("Failed: directory not found");
            return;
        }
        List<Directory> childDirectories = currentDirectory.getChildDirectories();
        for (int i = 0; i < childDirectories.size(); i++) {
            Directory child = childDirectories.get(i);
            if (child.getName().equals(parameter)) {
                currentDirectory.getChildDirectories().remove(i);
                System.out.print("SUCC: DELETED");
                return;
            }
        }
        System.out.print("Failed: directory not found");
    }

    private static Directory getDirectoryWithPath(String parameter, Directory rootDirectory) {
        if (parameter.equals("/"))
            return rootDirectory;
        String[] childsplit = parameter.split("/");
        String[] childDirectoriesName = Arrays.copyOfRange(childsplit, 1, childsplit.length);
        for (String childName : childDirectoriesName) {
            Directory childDirectory = getChildWithName(childName, rootDirectory);
            if (childDirectory == null)
                return null;
            else
                rootDirectory = childDirectory;
        }
        return rootDirectory;
    }

    private static Directory getChildWithName(String childName, Directory currentDirectory) {
        List<Directory> children = currentDirectory.getChildDirectories();
        if (children == null || children.size() == 0) {
            return null;
        }
        for (Directory child : children)
            if (child.getName().equals(childName)) {
                return child;
            }
        return null;
    }

    private static void listDirectory(Directory currentDirectory) {
        List<Directory> children = currentDirectory.getChildDirectories();
        if (children != null && children.size() != 0) {
            System.out.print("DIRS: ");
        } else {
            return;
        }
        for (Directory child : children)
            System.out.print(child.getName() + "\t");
    }

    private static void makeDirectories(Directory currentDirectory, String[] parameters) {
        for (String name : parameters) {
            if (checkDirectoryExist(currentDirectory, name)) {
                System.out.print("ERR: DIRECTORY ALREADY EXISTS");
                return;
            }
            Directory directory = new Directory(name, currentDirectory);
            currentDirectory.addChildDirectory(directory);
        }
        System.out.println("SUCC: CREATED");
    }

    private static boolean checkDirectoryExist(Directory currentDirectory, String name) {
        if (currentDirectory.getChildDirectories() == null)
            return false;
        for (Directory child : currentDirectory.getChildDirectories())
            if (child.getName().equals(name)) {
                return true;
            }
        return false;
    }

    public static Directory getNewRootDirectory() {
        return new Directory("", null);
    }
}

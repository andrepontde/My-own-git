package dev.andrepontde;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class Initbuilder {
    ProcessBuilder pb;


    public Initbuilder (String folderName) {
        try {
            Path repoPath = Paths.get(folderName);
            Files.createDirectory(repoPath);

            Path pivcoPath = repoPath.resolve(".pivco");
            //Add attributes to the path later
            Files.createDirectory(pivcoPath);

            Set<String> folders = Set.of( "hooks", "objects", "refs");

            for (String fName : folders) {
                Files.createDirectory(pivcoPath.resolve(fName));
            }

            Files.createDirectory(pivcoPath.resolve("refs").resolve("heads"));
            Files.createDirectory(pivcoPath.resolve("refs").resolve("tags"));

            Files.createDirectory(pivcoPath.resolve("objects").resolve("info"));
            Files.createDirectory(pivcoPath.resolve("objects").resolve("pack"));


            Set<String> files = Set.of("HEAD", "config");
            for (String fName : files) {
                Files.createFile(pivcoPath.resolve(fName));
            }

            Path headFile = pivcoPath.resolve("HEAD");
            // Write the default branch reference to HEAD file
            Files.writeString(headFile, "ref: refs/heads/main\n");

            System.out.println("Initialized pivco repository");
            //Añadir los files que se necesitan tambien
        }catch (IOException e){
            System.out.println("Init could not run: " + e.getMessage());
        }
    }
    
    public Initbuilder() { 
        try {
            Path pivcoPath = Paths.get("").resolve(".pivco");
            //Add attributes to the path later
            Files.createDirectory(pivcoPath);

            Set<String> folders = Set.of( "hooks", "objects", "refs");

            for (String fName : folders) {
                Files.createDirectory(pivcoPath.resolve(fName));
            }

            Files.createDirectory(pivcoPath.resolve("refs").resolve("heads"));
            Files.createDirectory(pivcoPath.resolve("refs").resolve("tags"));

            Files.createDirectory(pivcoPath.resolve("objects").resolve("info"));
            Files.createDirectory(pivcoPath.resolve("objects").resolve("pack"));

            Set<String> files = Set.of("HEAD", "config");
            for (String fName : files) {
                Files.createFile(pivcoPath.resolve(fName));
            }

            Path headFile = pivcoPath.resolve("HEAD");
            // Write the default branch reference to HEAD file
            Files.writeString(headFile, "ref: refs/heads/main\n");

            System.out.println("Initialized pivco repository");
            //Añadir los files que se necesitan tambien
        }catch (IOException e){
            System.out.println("Init could not run: " + e.getMessage());
        }
    }


    
}

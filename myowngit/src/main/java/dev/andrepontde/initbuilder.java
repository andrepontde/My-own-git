package dev.andrepontde;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class initbuilder {
    ProcessBuilder pb;


    public initbuilder (String folderName) {
        try {
            Path repoPath = Paths.get(folderName);
            Files.createDirectory(repoPath);

            Path pivcoPath = repoPath.resolve(".pivco");
            //Add attributes to the path later
            Files.createDirectory(pivcoPath);

            Set<String> folders = Set.of( "hooks", "objects", "refs", "info");

            for (String fName : folders) {
                Files.createDirectory(pivcoPath.resolve(fName));
            }

            Set<String> files = Set.of("HEAD", "config", "description");
            for (String fName : files) {
                Files.createFile(pivcoPath.resolve(fName));
            }

            System.out.println("Initialized pivco repository");
            //Añadir los files que se necesitan tambien
        }catch (IOException e){
            System.out.println("Init could not run: " + e.getMessage());
        }
    }
    
    public initbuilder() { 
        try {
            Path pivcoPath = Paths.get("").resolve(".pivco");
            //Add attributes to the path later
            Files.createDirectory(pivcoPath);

            Set<String> folders = Set.of( "hooks", "objects", "refs", "info");

            for (String fName : folders) {
                Files.createDirectory(pivcoPath.resolve(fName));
            }

            Set<String> files = Set.of("HEAD", "config", "description");
            for (String fName : files) {
                Files.createFile(pivcoPath.resolve(fName));
            }

            System.out.println("Initialized pivco repository");
            //Añadir los files que se necesitan tambien
        }catch (IOException e){
            System.out.println("Init could not run: " + e.getMessage());
        }
    }


    
}

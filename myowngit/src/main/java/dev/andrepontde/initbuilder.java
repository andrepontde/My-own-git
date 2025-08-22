package dev.andrepontde;

import java.io.IOException;

public class initbuilder {
    ProcessBuilder pb;


    public initbuilder (String folderName) {
        pb = new ProcessBuilder("mkdir", folderName);
        try {
            Process process = pb.start();
            pb = new ProcessBuilder("mkdir", folderName+"/pivco");
            process = pb.start();
            pb = new ProcessBuilder("mkdir", folderName+"/pivco/hooks", 
            folderName+"/pivco/objects", folderName+"/pivco/refs", folderName+"/pivco/info");
            // pb = new ProcessBuilder("echo", folderName+"config", );
        }catch (IOException e){
            System.out.println("Init could not run: " + e.getMessage());
        }
    }
    
    public initbuilder() { 
        pb = new ProcessBuilder("mkdir", ".pivco");
        
        try {
            Process process = pb.start();
            System.out.println(process);
        }catch (IOException e){
            System.out.println("Init could not run: " + e.getMessage());
        }
    }


    
}

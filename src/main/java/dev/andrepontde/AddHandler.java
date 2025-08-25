package dev.andrepontde;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.HexFormat;


public class AddHandler {
    MessageDigest digest;

    public AddHandler (String fInput){
        if (".".equals(fInput)){
            //Main code logic

        }

        else {
            //Code for single file
            Path filePath = Paths.get(fInput);
            hashFile(filePath);
            
            

        }

        
    }

    
    private String hashFile(Path fPath){
        String blobPath = "";
        
        try {
            digest = MessageDigest.getInstance("SHA-1");
            
            byte [] fContent = Files.readAllBytes(fPath);
            byte [] hashBytes =  digest.digest(fContent);
            String hashString = HexFormat.of().formatHex(hashBytes);

            blobPath = blobBuilder(fContent, hashString);

            // System.out.println(hashString);
            // System.out.println(fContent);

        } catch (java.security.NoSuchAlgorithmException | java.io.IOException e) {
            System.out.println("Could not hash file: " + e.getMessage());
        }

        return blobPath;
    }

    private String blobBuilder(byte[] content, String hashString){
        String folderName = hashString.substring(0, 2);
        String fileName = hashString.substring(2);
        Path blobPath =  Paths.get(".pivco/objects").resolve(folderName);
        String bPath = "";

        try {
            Files.createDirectories(blobPath);
            Path blob = blobPath.resolve(fileName);       
            Files.write(blob, content);
            bPath = blob.toString();

            //TODO - add idex reference!!!

        } catch (java.io.IOException e) {
            System.out.println("Could not create object: " + e.getMessage());
        }

        return bPath;

    }

    
    

}

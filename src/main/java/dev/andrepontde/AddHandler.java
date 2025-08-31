package dev.andrepontde;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.HexFormat;

//TODO -- Add .pivignore file implementation!!

public class AddHandler {
    MessageDigest digest;

    public AddHandler (String fInput){
        if (".".equals(fInput)){
            //Main code logic
            Path startPath = Paths.get("");
            try {
                Files.walk(startPath)
                    .filter(Files::isRegularFile)  // Filters only files, not directories
                    .filter(path -> !path.startsWith(".pivco"))  //Skips .pivco folder
                    .filter(path -> !path.startsWith(".git"))  //Skips .pivco folder
                    .forEach(this::hashFile);

            } catch (Exception e) {
                System.out.println("Could not hash multiple files in the current dir \n" + e.getMessage());
            }
            

        }

        else {
            //Code for single file
            Path filePath = Paths.get(fInput);
            hashFile(filePath);
        }

        
    }

    
    private String hashFile(Path fPath){
        String blobPath = "";
        Path pivcoPath = Paths.get(".pivco");
        
        try {
            digest = MessageDigest.getInstance("SHA-1");
            
            //Getting File content and creating blob header with standard git format
            byte [] fContent = Files.readAllBytes(fPath);
            String headerString = "blob " + fContent.length + "\0";
            byte[] headerBytes = headerString.getBytes();

            //Actual hashing using SHA-1, first git iteration of hashing systems
            digest.update(headerBytes);
            digest.update(fContent);
            byte [] hashBytes = digest.digest();

            //Get the hexFormat of the hashBytes to get the folder name and title for your blob
            String hashString = HexFormat.of().formatHex(hashBytes);

            //Copy the byte arrays into a single one to build the content inside the blob in 
            //a separate function
            byte[] fullBlob = new byte[headerBytes.length + fContent.length];
            System.arraycopy(headerBytes, 0, fullBlob, 0, headerBytes.length);
            System.arraycopy(fContent, 0, fullBlob, headerBytes.length, fContent.length);    

            blobPath = blobBuilder(fullBlob, hashString);

            String indexEntry = fPath + " " + hashString +"\n";
            Path indexPath = pivcoPath.resolve("index");
            String currentIndex = "";
            if (Files.exists(indexPath)) {
                currentIndex = Files.readString(indexPath);
            }

            // Check if entry already exists and update it
            String[] lines = currentIndex.split("\n");
            StringBuilder updatedIndex = new StringBuilder();
            boolean entryFound = false;

            for (String line : lines) {
                if (line.startsWith(fPath.toString() + " ")) {
                    updatedIndex.append(indexEntry);
                    entryFound = true;
                } else if (!line.isEmpty()) {
                    updatedIndex.append(line).append("\n");
                }
            }

            // If entry wasn't found, append it
            if (!entryFound) {
                updatedIndex.append(indexEntry);
            }

            Files.writeString(indexPath, updatedIndex.toString(), 
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);


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


        } catch (java.io.IOException e) {
            System.out.println("Could not create object: " + e.getMessage());
        }

        return bPath;

    }

    
    

}

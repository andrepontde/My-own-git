package dev.andrepontde;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;

public class StatusWard {
    Path startPath;
    Path indexPath;
    List <String> indexEntries;
    
    public StatusWard(){
        // Initialize paths first
        startPath = Paths.get("");
        indexPath = Paths.get(".pivco/");
        
        try {
            indexEntries = Files.readAllLines(indexPath.resolve("index"));
        } catch (Exception e) {
            System.out.println("Could not fetch index entries: " + e.getMessage());
            indexEntries = new ArrayList<>(); // Initialize empty list if file doesn't exist
        }
        
        System.out.println("On branch " + getBranch());
        HashMap <String, ArrayList <String> > statusInfo = getIndexStatus();

        ArrayList <String> changedFiles = statusInfo.get("changedFiles");
        ArrayList <String> stagedFiles = statusInfo.get("stagedFiles");
        ArrayList <String> deletedFiles = statusInfo.get("deletedFiles");
        ArrayList <String> untrackedFiles = statusInfo.get("untrackedFiles");

        // Changes to be committed (staged files)
        if (!stagedFiles.isEmpty()) {
            System.out.println("\nChanges to be committed:");
            for (String file : stagedFiles) {
                System.out.println("  new file:   " + file.split(" ")[0]);
            }
        }

        // Changes not staged for commit (modified files)
        if (!changedFiles.isEmpty()) {
            System.out.println("\nChanges not staged for commit:");
            for (String file : changedFiles) {
                System.out.println("  modified:   " + file.split(" ")[0]);
            }
        }

        // Deleted files
        if (!deletedFiles.isEmpty()) {
            System.out.println("\nDeleted files:");
            for (String file : deletedFiles) {
                System.out.println("  deleted:    " + file.split(" ")[0]);
            }
        }

        // Untracked files
        if (!untrackedFiles.isEmpty()) {
            System.out.println("\nUntracked files:");
            for (String file : untrackedFiles) {
                System.out.println("  " + file);
            }
        }

        // If everything is clean
        if (stagedFiles.isEmpty() && changedFiles.isEmpty() && deletedFiles.isEmpty() && untrackedFiles.isEmpty()) {
            System.out.println("\nworking tree clean");
        }
    }

    private HashMap <String, ArrayList <String> > getIndexStatus(){
        HashMap <String, ArrayList <String>> statusInfo = new HashMap<>();
        //For files that have unstaged changes
        ArrayList<String> changedFiles = new ArrayList<>();
        //For files that have no changes
        ArrayList <String> stagedFiles = new ArrayList<>();
        //Files that are in index but not in the directory anymore 
        ArrayList <String> deletedFiles = new ArrayList<>();
        //New files that are not in the index
        ArrayList <String> untrackedFiles = new ArrayList<>();

        try {
            
            
            //Look for each entry in the index file 
            for (String entry : indexEntries) {
                Path entryPath = Paths.get(entry.split(" ")[0]);
                String entryHashString = entry.split(" ")[1];
                
                if (entry.isEmpty()){continue;}
                
                //Add to deleted files, untracked, or staged arraylists 
                if (!Files.exists(entryPath)) {
                    deletedFiles.add(entry);
                    continue;
                }

                if (!getHashString(entryPath).equals(entryHashString)) {
                    changedFiles.add(entry);
                    continue;
                }else{
                    stagedFiles.add(entry);
                    continue;
                }

                

            }

            Files.walk(startPath)
                .filter(Files::isRegularFile)  // Filters only files, not directories
                .filter(path -> !path.startsWith(".pivco"))  //Skips .pivco folder
                .filter(path -> !path.startsWith(".git"))  //Skips .git folder
                .forEach(path -> {
                    String fileName = path.toString();
                    // Check if this file is NOT in the index
                    boolean isInIndex = indexEntries.stream()
                        .anyMatch(entry -> !entry.isEmpty() && entry.split(" ")[0].equals(fileName));
                    
                    if (!isInIndex) {
                        untrackedFiles.add(fileName);
                    }
                });

            } catch (Exception e) {
                System.out.println("Could not hash multiple files in the current dir \n" + e.getMessage());
            }

        statusInfo.put("changedFiles", changedFiles);
        statusInfo.put("stagedFiles", stagedFiles);
        statusInfo.put("deletedFiles", deletedFiles);
        statusInfo.put("untrackedFiles", untrackedFiles);

        return statusInfo;

    }

    public String getHashString(Path fPath){
        String hashString = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            byte [] fContent = Files.readAllBytes(fPath);
            String headerString = "blob " + fContent.length + "\0";
            byte[] headerBytes = headerString.getBytes();

            //Actual hashing using SHA-1, first git iteration of hashing systems
            digest.update(headerBytes);
            digest.update(fContent);
            byte [] hashBytes = digest.digest();

            //Get the hexFormat of the hashBytes to get the folder name and title for your blob
            hashString = HexFormat.of().formatHex(hashBytes);    
        } catch (Exception e) {
            System.out.println("Unable to get hash string of file "+ fPath + "\n" + e.getMessage());
        }

        return hashString;
        
    }

    private String getBranch(){
        final Path headPath = Paths.get(".pivco/HEAD");
        //If something goes wrong will use this value
        String currBranch = "default"; 
        
        try {
            List<String> headRead = Files.readAllLines(headPath);
            if (!headRead.isEmpty()) {
                String headContent = headRead.get(0).trim();
                if (headContent.startsWith("ref: refs/heads/")) {
                    currBranch = headContent.substring("ref: refs/heads/".length());
                } else {
                    currBranch = headContent;
                }
            }

        } catch (Exception e) {
            System.out.println("Could not read the file HEAD "+ e.getMessage());
        }

        return currBranch;
    }


}
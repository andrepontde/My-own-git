package dev.andrepontde;

// Private version control comand runner
public class pivco {
    public static void main(String[] args) {
        if (args.length == 0){
            System.out.println("Usage pivco <command>");
            return; 
        }
        String command = args[0].toLowerCase();
        switch (command) {
            case "init":
                if (!(args.length == 1)){
                    initbuilder ib = new initbuilder(args[1]);
                }else {
                    initbuilder ib = new initbuilder();
                }
                
                break;
            default:
                throw new AssertionError();
        } 
    }
}
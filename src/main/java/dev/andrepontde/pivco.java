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
                    Initbuilder ib = new Initbuilder(args[1]);
                }else {
                    Initbuilder ib = new Initbuilder();
                }
                
                break;
            case "add": 
                if (args.length == 1){
                    System.out.println("Usage: add <file path or  ''.'' >");
                    break;
                }else {
                    AddHandler ah = new AddHandler(args[1]);
                    break;
                }
            case "status": 
                if (args.length > 1){
                    System.out.println("The status command does not take any parameters");
                    break;
                }else {
                    StatusWard sw = new StatusWard();
                    break;
                }
            default:
                System.out.println("'" + args[0] + "' is not a pivco command");;
        } 
    }
}
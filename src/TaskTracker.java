public class TaskTracker {

    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("Please provide a command.");
            return;
        }

        String command = args[0];
        switch (command) {
            case "add":
                // add task
                break;
            case "list":
                // list tasks
                break;
            case "update":
                // update task
                break;
            case "delete" :
                // delete task
                break;
            case "mark-in-progress":
                // mark in progress
                break;
            case "mark-done":
                // mark done
                break;
            default:
                System.out.println("Unknown command");
        }
    }
}

import java.util.Arrays;
import java.util.List;

public class TaskTracker {

    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("Usage: java TaskTracker <command> [options]");
            System.out.println("Commands: add | list | update | delete | mark-in-progress | mark-done");
            return;
        }

        String command = args[0];
        List<Task> tasks = TaskStorage.loadTasks();

        switch (command) {
            case "add":
                handleAdd(args, tasks);
                break;
            case "list":
                handleList(args, tasks);
                break;
            case "update":
                handleUpdate(args, tasks);
                break;
            case "delete" :
                handleDelete(args, tasks);
                break;
            case "mark-in-progress":
                handleMark(args, tasks, "in-progress");
                break;
            case "mark-done":
                handleMark(args, tasks, "done");
                break;
            default:
                System.out.println("Unknown command: " + command);
        }

        TaskStorage.saveTasks(tasks);
    }

    private static void handleAdd(String[] args, List<Task> tasks) {
        if(args.length < 2){
            System.out.println("Usage: task-cli add \"description\"");
            return;
        }

        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        int id = tasks.isEmpty() ? 1 : tasks.get(tasks.size() -1).getId() + 1;
        Task t = new Task(id, description);

        tasks.add(t);

        System.out.println("Task added successfully (ID: " + id + ")");
    }

    private static void handleList(String[] args, List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No task found");
            return;
        }

        String filter = args.length > 1 ? args[1] : "all";

        for (Task t : tasks) {
            if(filter.equals("all") || t.getStatus().equalsIgnoreCase(filter)) {
                System.out.println(t);
            }
        }
    }

    private static void handleUpdate(String[] args, List<Task> tasks) {
        if(args.length < 3) {
            System.out.println("Usage: task-cli update <id> \"new description\"");
            return;
        }

        try {
            int id = Integer.parseInt(args[1]);
            String description = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

            for (Task t : tasks) {
                if(t.getId() == id) {
                    t.setDescription(description);
                    t.touchUpdateAt();
                    System.out.println("Task updated successfully");
                    return;
                }
            }

            System.out.println("Task not found (ID: " + id + ")");
        } catch (NumberFormatException e) {
            System.out.println("Invalid task ID.");
        }
    }

    private static void handleDelete(String[] args, List<Task> tasks) {
        if (args.length < 2) {
            System.out.println("Usage: task-cli delete <id>");
            return;
        }

        try {
            int id = Integer.parseInt(args[1]);
            boolean removed = tasks.removeIf(t -> t.getId() == id);

            if (removed) {
                System.out.println("Task deleted successfully.");
            } else {
                System.out.println("Task not found (ID: " + id + ")");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid task ID.");
        }
    }

    private static void handleMark(String[] args, List<Task> tasks, String status) {
        if(args.length < 2) {
            System.out.println("Usage: task-cli mark-" + status + " <id>");
            return;
        }

        try {
            int id = Integer.parseInt(args[1]);

            for (Task t : tasks) {
                if(t.getId() == id) {
                    t.setStatus(status);
                    t.touchUpdateAt();
                    System.out.println("Task marked as " + status);
                    return;
                }
            }

            System.out.println("Task not found (ID: "+ id + ")");

        } catch (NumberFormatException e) {
            System.out.println("Invalid task ID.");
        }
    }
}

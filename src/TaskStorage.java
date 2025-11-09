import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskStorage {

    private static final String FILE_NAME = "task.json";

    public static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(FILE_NAME);

        if(!file.exists()) return tasks;

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonBuilder = new StringBuilder();

            String line;
            while((line = br.readLine()) != null){
                jsonBuilder.append(line.trim());
            }

            String json = jsonBuilder.toString();
            if(json.isEmpty() || json.equals("[]")) return tasks;

            json = json.substring(1, json.length() - 1);
            String[] objects = json.split("(?<=\\}),\\s*(?=\\{)");

            for (String o : objects) {
                Task t = parseTask(o);
                if(t != null) tasks.add(t);
            }

        } catch (IOException e) {
            System.out.println("Error reading tasks file: " + e.getMessage());
        }

        return tasks;
    }

    public static void saveTasks(List<Task> tasks) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            bw.write("[\n");
            for (int i = 0; i < tasks.size(); i++){
                bw.write(toJson(tasks.get(i)));

                if(i < tasks.size() - 1) bw.write(",");

                bw.write("\n");
            }
            bw.write("]");
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    private static String toJson(Task t) {
        return String.format("{\"id\": %d,\"description\": \"%s\",\"status\": \"%s\",\"createdAt\": \"%s\",\"updatedAt\": \"%s\"}",
                t.getId(), escape(t.getDescription()), t.getStatus(),
                t.getCreatedAt(), t.getUpdatedAt());
    }

    private static Task parseTask(String json) {
        try {
            Map<String, String> map = new HashMap<>();

            json = json.trim().replace("[\\{\\}\"]", "");
            String[] fields = json.split(",");

            for (String f : fields) {
                String[] kv = f.split(":", 2);
                if(kv.length == 2) {
                    map.put(kv[0], kv[1]);
                }
            }

            int id = Integer.parseInt(map.get("id"));
            String description = map.get("description");
            String status = map.get("status");
            LocalDateTime createdAt = LocalDateTime.parse(map.get("createdAt"));
            LocalDateTime updatedAt = LocalDateTime.parse(map.get("updatedAt"));

            return new Task(id, description, status, createdAt, updatedAt);
        } catch (Exception e) {
            System.out.println("Failed to parse task: " + e.getMessage());
            return null;
        }
    }
    private static String escape(String text) {
        return text.replace("\"", "\\\"");
    }
}

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskStorage {

    private static final String FILE_NAME = "task.json";

    public static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(FILE_NAME);

        if(!file.exists()) return tasks;

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null){
                // Parse line into Task (we can start simple)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public static void saveTasks(List<Task> tasks) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Task t : tasks){
                bw.write(t.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

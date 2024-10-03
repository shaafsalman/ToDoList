package com.example.todolist.utility;

import android.content.Context;
import android.util.Log;

import com.example.todolist.model.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class TaskManager {
    private static final String FILE_NAME = "tasks.json";

    public static void saveTasks(Context context, ArrayList<Task> tasks) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Task task : tasks) {
                JSONObject taskObject = new JSONObject();
                taskObject.put("title", task.getTitle());
                taskObject.put("description", task.getDescription());
                taskObject.put("isCompleted", task.isCompleted());
                jsonArray.put(taskObject);
            }

            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(jsonArray.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e("TaskManager", "Error saving tasks", e);
        }
    }

    public static ArrayList<Task> loadTasks(Context context) {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            String json = new String(buffer);
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject taskObject = jsonArray.getJSONObject(i);
                String title = taskObject.getString("title");
                String description = taskObject.getString("description");
                boolean isCompleted = taskObject.getBoolean("isCompleted");
                tasks.add(new Task(title, description, isCompleted));
            }
        } catch (Exception e) {
            Log.e("TaskManager", "Error loading tasks", e);
        }
        return tasks;
    }

    public static void addTask(Context context, Task task) {
        ArrayList<Task> tasks = loadTasks(context);
        tasks.add(task);
        saveTasks(context, tasks);
    }
}

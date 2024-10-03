package com.example.todolist.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.adapter.TaskAdapter;
import com.example.todolist.model.Task;
import com.example.todolist.utility.TaskManager;

import java.util.ArrayList;

public class ActiveTasksFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;

    private static final int ADD_TASK_REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_tasks, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewActiveTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadActiveTasks();

        view.findViewById(R.id.fabAddTask).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTaskActivity.class);
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE);
        });

        return view;
    }

    private void loadActiveTasks() {
        ArrayList<Task> allTasks = TaskManager.loadTasks(getContext());
        ArrayList<Task> activeTasks = new ArrayList<>();

        for (Task task : allTasks) {
            if (!task.isCompleted()) {
                activeTasks.add(task);
            }
        }

        taskAdapter = new TaskAdapter(getContext(), activeTasks, true);
        recyclerView.setAdapter(taskAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            Task newTask = (Task) data.getSerializableExtra("new_task");
            if (newTask != null) {
                TaskManager.addTask(getContext(), newTask);
                loadActiveTasks(); // Reload active tasks
                Toast.makeText(getContext(), "Task added!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

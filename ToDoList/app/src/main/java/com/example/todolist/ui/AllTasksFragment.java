package com.example.todolist.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;

import com.example.todolist.R;
import com.example.todolist.adapter.TaskAdapter;
import com.example.todolist.model.Task;
import com.example.todolist.utility.TaskManager;

import java.util.ArrayList;

public class AllTasksFragment extends Fragment implements TaskAdapter.OnDeleteTaskListener {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_tasks, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadCompletedTasks();

        return view;
    }

    private void loadCompletedTasks() {
        ArrayList<Task> allTasks = TaskManager.loadTasks(getContext());
        ArrayList<Task> completedTasks = new ArrayList<>();

        for (Task task : allTasks) {
            if (task.isCompleted()) {
                completedTasks.add(task);
            }
        }

        taskAdapter = new TaskAdapter(getContext(), completedTasks, false);
        taskAdapter.setOnDeleteTaskListener(this);
        recyclerView.setAdapter(taskAdapter);
    }

    public void updateTaskList() {
        loadCompletedTasks();  // Reload tasks when updated
    }

    @Override
    public void onDeleteTask(int position) {
        showDeleteConfirmationDialog(position);
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    taskAdapter.deleteTask(position);
                    updateTaskList();
                })
                .setNegativeButton("No", null)
                .show();
    }
}

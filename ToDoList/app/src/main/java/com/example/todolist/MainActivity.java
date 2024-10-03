package com.example.todolist;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Button;

import com.example.todolist.model.Task;
import com.example.todolist.ui.ActiveTasksFragment;
import com.example.todolist.ui.AllTasksFragment;
import com.example.todolist.ui.TaskDetailActivity;
import com.example.todolist.ui.TaskDetailFragment;

public class MainActivity extends AppCompatActivity {

    private Button btnActiveTasks, btnAllTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnActiveTasks = findViewById(R.id.btnActiveTasks);
        btnAllTasks = findViewById(R.id.btnAllTasks);

        loadFragment(new ActiveTasksFragment());

        btnActiveTasks.setOnClickListener(v -> loadFragment(new ActiveTasksFragment()));
        btnAllTasks.setOnClickListener(v -> loadFragment(new AllTasksFragment()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    public void showTaskDetails(Task task) {
        TaskDetailFragment taskDetailFragment = TaskDetailFragment.newInstance(task);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        int containerId = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                ? R.id.taskDetailsContainer
                : R.id.fragmentContainer;

        transaction.replace(containerId, taskDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void updateAllTasksFragment() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (currentFragment instanceof AllTasksFragment) {
            ((AllTasksFragment) currentFragment).updateTaskList();
        }
    }

    public boolean isInLandscapeMode() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public void showTaskDetailsInFragment(Task task) {
        TaskDetailFragment fragment = TaskDetailFragment.newInstance(task);

        if (findViewById(R.id.taskDetailsContainer) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.taskDetailsContainer, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, TaskDetailActivity.class);
            intent.putExtra("task", task);
            startActivity(intent);
        }
    }
}

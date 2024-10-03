package com.example.todolist.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todolist.ui.MainActivity;
import com.example.todolist.R;
import com.example.todolist.model.Task;
import com.example.todolist.utility.TaskManager;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final Context context;
    private final ArrayList<Task> taskList;
    private final boolean isActiveTasks;
    private OnDeleteTaskListener deleteTaskListener;

    public TaskAdapter(Context context, ArrayList<Task> taskList, boolean isActiveTasks) {
        this.context = context;
        this.taskList = taskList;
        this.isActiveTasks = isActiveTasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isActiveTasks ? R.layout.item_active_task : R.layout.item_all_task;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskTitle.setText(task.getTitle());
        holder.taskDescription.setText(task.getDescription());

        if (isActiveTasks) {
            holder.btnComplete.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.GONE);

            holder.btnComplete.setOnClickListener(v -> completeTask(position));

            holder.itemView.setOnClickListener(v -> {
                if (context instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) context;
                    if (mainActivity.isInLandscapeMode()) {
                        mainActivity.showTaskDetailsInFragment(task);
                    } else {
                        mainActivity.showTaskDetails(task);
                    }
                }
            });
        } else {
            holder.btnComplete.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(v -> {
                if (context instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) context;
                    if (mainActivity.isInLandscapeMode()) {
                        mainActivity.showTaskDetailsInFragment(task);
                    } else {
                        mainActivity.showTaskDetails(task);
                    }
                }
            });
            holder.btnDelete.setOnClickListener(v -> {
                if (deleteTaskListener != null) {
                    deleteTaskListener.onDeleteTask(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateTaskList(ArrayList<Task> newTaskList) {
        this.taskList.clear();
        this.taskList.addAll(newTaskList);
        notifyDataSetChanged();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle;
        TextView taskDescription;
        Button btnComplete;
        Button btnDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.textViewTitle);
            taskDescription = itemView.findViewById(R.id.textViewDescription);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public void addTask(Task task) {
        taskList.add(task);
        notifyItemInserted(taskList.size() - 1);
    }

    public void completeTask(int position) {
        Log.d("TaskAdapter", "Completing task at position: " + position);

        if (position < 0 || position >= taskList.size()) {
            Log.e("TaskAdapter", "Invalid position: " + position);
            return;
        }


        Task task = taskList.get(position);
        task.setCompleted(true);

        ArrayList<Task> allTasks = TaskManager.loadTasks(context);
        for (Task t : allTasks) {
            if (t.getTitle().equals(task.getTitle())) {
                t.setCompleted(true);
                break;
            }
        }
        TaskManager.saveTasks(context, allTasks);

        taskList.remove(position);
        notifyItemRemoved(position);
        Toast.makeText(context, "Task '" + task.getTitle() + "' completed!", Toast.LENGTH_SHORT).show();

    }

    public void deleteTask(int position) {
        if (position >= 0 && position < taskList.size()) {
            Task taskToRemove = taskList.get(position);
            taskList.remove(position);
            notifyItemRemoved(position);

            // Remove from the persistent storage
            ArrayList<Task> allTasks = TaskManager.loadTasks(context);
            allTasks.removeIf(task -> task.getTitle().equals(taskToRemove.getTitle()));
            TaskManager.saveTasks(context, allTasks);
        }
    }

    public interface OnDeleteTaskListener {
        void onDeleteTask(int position);
    }

    public void setOnDeleteTaskListener(OnDeleteTaskListener listener) {
        this.deleteTaskListener = listener;
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }
}

package com.example.todolist.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todolist.R;

public class TaskDetailActivity extends AppCompatActivity {

    private TextView taskTitle, taskDescription;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        taskTitle = findViewById(R.id.taskTitleDetail);
        taskDescription = findViewById(R.id.taskDescriptionDetail);
        btnBack = findViewById(R.id.btnBack);

        String title = getIntent().getStringExtra("task_title");
        String description = getIntent().getStringExtra("task_description");

        taskTitle.setText(title);
        taskDescription.setText(description);

        btnBack.setOnClickListener(view -> finish());
    }
}

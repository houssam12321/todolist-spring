package com.example.ToDoApp.Repo;

import com.example.ToDoApp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task,Integer> {
}

package com.example.ToDoApp.controller;

import com.example.ToDoApp.Repo.TaskRepo;
import com.example.ToDoApp.model.Task;
import com.example.ToDoApp.model.TaskDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepo repo;

    @GetMapping({"", "/"})
    public String showTaskList(Model model) {
        List<Task> tasks = repo.findAll();
        model.addAttribute("tasks", tasks);
        return "tasks/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("taskDto", new TaskDto());
        return "tasks/CreateTask";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute @Valid TaskDto taskDto, BindingResult result) {

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDate(taskDto.getDate());
        task.setType(taskDto.getType());
        repo.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id) {
        try {
            Task task = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task ID"));
            TaskDto taskDto = new TaskDto();
            taskDto.setTitle(task.getTitle());
            taskDto.setDate(task.getDate());
            taskDto.setType(task.getType());
            model.addAttribute("task", task);
            model.addAttribute("taskDto", taskDto);
        } catch (Exception ex) {
            return "redirect:/tasks";
        }
        return "tasks/EditTask";
    }

    @PutMapping("/edit")
    public String updateTask(@RequestParam int id, @ModelAttribute @Valid TaskDto taskDto, BindingResult result) {
        if (result.hasErrors()) {
            return "tasks/EditTask";
        }
        try {
            Task task = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task ID"));
            task.setTitle(taskDto.getTitle());
            task.setDate(taskDto.getDate());
            task.setType(taskDto.getType());
            repo.save(task);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/tasks";
    }

    @GetMapping("/delete")
    public String deleteTask(@RequestParam int id) {
        try {
            repo.deleteById(id);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/tasks";
    }
}

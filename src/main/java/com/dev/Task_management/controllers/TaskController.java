package com.dev.Task_management.controllers;

import com.dev.Task_management.entities.Task;
import com.dev.Task_management.entities.User;
import com.dev.Task_management.repositories.UserRepository;
import com.dev.Task_management.services.Impl.TaskServiceImpl;
import com.dev.Task_management.services.TaskService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
@Controller
@RequestMapping("/")
public class TaskController {

    @Autowired
    private final TaskServiceImpl taskService;

    @Autowired
    private UserRepository userRepository;

    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

//    @GetMapping
//    public String getAllTasks(Model model) {
//        List<Task> tasks = taskService.findAllTasks();
//        model.addAttribute("tasks", tasks);
//        return "list";
//    }

    @GetMapping("/{id}")
    public String getTaskById(@PathVariable Long id, Model model) throws Throwable {
        Task task = (Task) taskService.findTaskById(id);
        model.addAttribute("task", task);
        return "details";
    }

    @GetMapping("/tasks/create")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        return "create";
    }

    //    @PostMapping
//    public String createTask(@ModelAttribute("task") Task task) {
//        taskService.saveTask(task);
//        return "redirect:/";
//    }
    @GetMapping()
    public String getALlTasksForCurrentUser(Model model){
        List<Task> tasks = taskService.getTasksForCurrentUser();
        model.addAttribute("tasks", tasks);

        return "list";
    }
    @PostMapping
    public String createTask(@ModelAttribute Task task) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.findByUsername(currentPrincipalName).orElse(null);
        assert user != null;
        Long userId = user.getId();


        taskService.saveTask(task);
        // Redirect to the task details page for the newly created task
        return "redirect:/" + task.getId(); // Assuming the task details page URL includes the task ID
    }


    private User getCurrentUser() {
        // Get the username of the currently authenticated user
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        // Create a user object with the username
        User user = new User();
        user.setUsername(username);
        return user;
    }


    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) throws Throwable {
        Task task = (Task) taskService.findTaskById(id);
        model.addAttribute("task", task);
        return "edit";
    }

    @PostMapping("/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute("task") Task taskDetails) throws Throwable {

        Task task = (Task) taskService.findTaskById(id);
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setCompleted(taskDetails.isCompleted());
        task.setDueDate(taskDetails.getDueDate());
        taskService.saveTask(taskDetails);
        return "redirect:/";

    }

    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }
}
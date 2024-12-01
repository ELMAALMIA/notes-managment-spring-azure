package com.dev.Task_management.controllers;

import com.dev.Task_management.entities.Task;
import com.dev.Task_management.services.TaskService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;


    // Lister toutes les tâches
    @GetMapping
    public String listTasks(Model model) {
        try {
            model.addAttribute("tasks", taskService.getAllTasks());
        } catch (Exception e) {

            model.addAttribute("error", "Impossible de récupérer les tâches.");
        }
        return "tasks"; // Correspond à tasks.html dans templates
    }


    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            Task task = taskService.getTaskById(id);
            model.addAttribute("task", task);
        } catch (Exception e) {
            model.addAttribute("error", "Impossible de récupérer la tâche à modifier.");
            return "redirect:/tasks"; // Redirection en cas d'erreur
        }
        return "task-form"; // Réutilise le même formulaire pour la création et la mise à jour
    }


    // Afficher le formulaire pour créer une tâche
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        try {
            model.addAttribute("task", new Task());
        } catch (Exception e) {
            //logger.error("Erreur lors de l'affichage du formulaire", e);
            model.addAttribute("error", "Impossible d'afficher le formulaire.");
        }
        return "task-form"; // Correspond à task-form.html
    }

    // Enregistrer une nouvelle tâche ou mettre à jour une tâche existante
    @PostMapping
    public String saveTask(@ModelAttribute("task") Task task, Model model) {
        try {
            taskService.saveTask(task);
        } catch (Exception e) {
            //logger.error("Erreur lors de l'enregistrement de la tâche", e);
            model.addAttribute("error", "Impossible d'enregistrer la tâche.");
            return "task-form"; // Réafficher le formulaire en cas d'erreur
        }
        return "redirect:/tasks";
    }

    // Supprimer une tâche
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id, Model model) {
        try {
            taskService.deleteTask(id);
        } catch (Exception e) {
            //logger.error("Erreur lors de la suppression de la tâche", e);
            model.addAttribute("error", "Impossible de supprimer la tâche.");
        }
        return "redirect:/tasks";
    }
}

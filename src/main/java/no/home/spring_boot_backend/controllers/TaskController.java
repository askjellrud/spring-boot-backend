package no.home.spring_boot_backend.controllers;

import no.home.spring_boot_backend.model.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private AtomicLong counter = new AtomicLong(0);
    private Map<Long, Task> tasks = new HashMap<>();

    @GetMapping()
    public ResponseEntity<Collection<Task>> hentAlle() {
        return ResponseEntity.status(200).body(tasks.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") Long id) {
        Task task = tasks.get(id);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable("id") Long id, @RequestBody UpdateTask body) {
        Task task = tasks.get(id);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        task.setTitle(body.getTitle());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeTask(@PathVariable("id") Long id) {
        Task task = tasks.get(id);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        tasks.remove(task.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping()
    public ResponseEntity<Void> addTask(@RequestBody AddTask body) {
        Long newId = counter.incrementAndGet();

        Task task = new Task();
        task.setId(newId);
        task.setTitle(body.getTitle());
        task.setCompleted(false);

        tasks.put(newId, task);

        URI taskURI = URI.create("/tasks/" + newId);
        return ResponseEntity.created(taskURI).build();
    }

}

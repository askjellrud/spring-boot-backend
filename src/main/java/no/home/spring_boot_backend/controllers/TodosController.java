package no.home.spring_boot_backend.controllers;

import no.home.spring_boot_backend.model.Todo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/todos")
public class TodosController {

    private AtomicLong teller = new AtomicLong(0);
    private Map<Long, Todo> todos = new HashMap<>();

    @GetMapping()
    public ResponseEntity<Collection<Todo>> hentAlle() {
        return ResponseEntity.status(200).body(todos.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> hentTodo(@PathVariable("id") Long id) {
        Todo todo = todos.get(id);

        if (todo == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> oppdaterTodo(@PathVariable("id") Long id, @RequestBody OppdaterTodoRequest request) {
        Todo todo = todos.get(id);

        if (todo == null) {
            return ResponseEntity.notFound().build();
        }

        todo.setTitle(request.getTitle());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> taVekkTodo(@PathVariable("id") Long id) {
        Todo todo = todos.get(id);

        if (todo == null) {
            return ResponseEntity.notFound().build();
        }

        todos.remove(todo.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping()
    public ResponseEntity<Void> leggTilTodo(@RequestBody LeggTilTodoRequest request) {
        Long nyId = teller.incrementAndGet();

        Todo todo = new Todo();
        todo.setId(nyId);
        todo.setTitle(request.getTitle());
        todo.setCompleted(false);

        todos.put(nyId, todo);

        URI todoUri = URI.create("/todos/" + nyId);
        return ResponseEntity.created(todoUri).build();
    }

}

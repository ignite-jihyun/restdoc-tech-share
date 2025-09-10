package org.example.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(new ArrayList<>(store.values()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = store.get(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody Map<String, String> payload) {
        String name = payload.getOrDefault("name", "");
        String email = payload.getOrDefault("email", "");
        long id = seq.incrementAndGet();
        User user = new User(id, name, email);
        store.put(id, user);
        return ResponseEntity.created(URI.create("/users/" + id)).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        User existing = store.get(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        String name = payload.getOrDefault("name", existing.name());
        String email = payload.getOrDefault("email", existing.email());
        User updated = new User(id, name, email);
        store.put(id, updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User removed = store.remove(id);
        if (removed == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}

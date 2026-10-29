package edu.midterm;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;
@RestController @RequestMapping("/api/students")
public class StudentController {
 private final StudentRepository repo;
 public StudentController(StudentRepository repo) { this.repo=repo; }
 @GetMapping public List<Student> all(@RequestParam(required=false) String name) {
  return name == null || name.isBlank() ? repo.findAll() : repo.findByNameContainingIgnoreCase(name.trim());
 }
 @GetMapping("/{id}") public Student one(@PathVariable Long id) {
  return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Student not found"));
 }
 @PostMapping public ResponseEntity<Student> create(@Valid @RequestBody StudentRequest r) {
  Student s=new Student(); apply(s,r); s=repo.saveAndFlush(s);
  return ResponseEntity.created(URI.create("/api/students/"+s.id)).body(s);
 }
 @PutMapping("/{id}") public Student update(@PathVariable Long id,@Valid @RequestBody StudentRequest r) {
  Student s=one(id); apply(s,r); return repo.saveAndFlush(s);
 }
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
 public void delete(@PathVariable Long id) { repo.delete(one(id)); }
 private void apply(Student s,StudentRequest r) { s.name=r.name(); s.email=r.email(); s.course=r.course(); }
}

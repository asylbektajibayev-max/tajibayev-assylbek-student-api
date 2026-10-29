package edu.midterm;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.Map;
@RestControllerAdvice
public class Errors {
 @ExceptionHandler(DataIntegrityViolationException.class)
 public ResponseEntity<Map<String,String>> duplicate() {
  return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","Email already exists or data violates constraints"));
 }
}

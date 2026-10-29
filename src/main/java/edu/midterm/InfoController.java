package edu.midterm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
public class InfoController {
 @Value("${student.full-name}") private String student;
 @GetMapping("/") public Map<String,String> info() { return Map.of("application","Student API","student",student,"version","1.1"); }
}

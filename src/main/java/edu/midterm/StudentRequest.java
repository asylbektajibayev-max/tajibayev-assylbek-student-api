package edu.midterm;
import jakarta.validation.constraints.*;
public record StudentRequest(@NotBlank @Size(max=120) String name,
 @NotBlank @Email @Size(max=200) String email,
 @NotBlank @Size(max=120) String course) {}

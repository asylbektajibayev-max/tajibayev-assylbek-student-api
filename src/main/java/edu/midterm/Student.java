package edu.midterm;
import jakarta.persistence.*;
@Entity
@Table(name="students", uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class Student {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false) public String name;
 @Column(nullable=false) public String email;
 @Column(nullable=false) public String course;
 public Student() {}
}

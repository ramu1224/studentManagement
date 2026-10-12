package com.test.studentmanagement.controller;

import com.test.studentmanagement.model.Student;
import com.test.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * StudentController
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:4200")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * This API is to return all the student info
     *
     * @return
     */
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    /**
     * This API is to return the student details based on search parameter values
     * passed as query params, example /api/students/search?firstName=ra&sex=Male
     *
     * @return
     */
    @GetMapping("/search")
    public List<Student> serachStudents(Student student) {
        return studentService.searchStudents(student);
    }

    /**
     * This API is to get a student information
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    /**
     * This API is to create a new student
     *
     * @param student
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(@Valid @RequestBody Student student) {
        return studentService.createStudent(student);
    }

    /**
     * This API is to update the student with passing info
     *
     * @param id
     * @param student
     * @return
     */
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @Valid @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    /**
     * This API is to delete the student
     * @param id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}

package com.test.studentmanagement.controller;

import com.test.studentmanagement.exception.DuplicateEmailException;
import com.test.studentmanagement.exception.ResourceNotFoundException;
import com.test.studentmanagement.model.Student;
import com.test.studentmanagement.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    private Student ada() {
        Student student = new Student("Ada", "Lovelace", "King", "Jr.", "Female", new Date(0), "ada@test.com");
        student.setId(1L);
        return student;
    }

    @Test
    void getAllStudents_returns200() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(ada()));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Ada"))
                .andExpect(jsonPath("$[0].email").value("ada@test.com"));
    }

    @Test
    void getStudent_whenMissing_returns404() throws Exception {
        when(studentService.getStudent(9L)).thenThrow(new ResourceNotFoundException("Student not found with id 9"));

        mockMvc.perform(get("/api/students/9"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student not found with id 9"));
    }

    @Test
    void createStudent_withValidBody_returns201() throws Exception {
        when(studentService.createStudent(any(Student.class))).thenReturn(ada());

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ada())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createStudent_withInvalidBody_returns400() throws Exception {
        Student bad = new Student("", "Lovelace", "King", "Jr.", "Female", new Date(0), "not-an-email");

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.firstName").value("First name is required"))
                .andExpect(jsonPath("$.errors.email").value("Email is not valid"));
    }

    @Test
    void createStudent_withDuplicateEmail_returns409() throws Exception {
        when(studentService.createStudent(any(Student.class))).thenThrow(new DuplicateEmailException("ada@test.com"));

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ada())))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteStudent_returns204() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(1L);
    }
}

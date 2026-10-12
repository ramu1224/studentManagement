package com.test.studentmanagement;

import com.test.studentmanagement.model.Course;
import com.test.studentmanagement.model.Student;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

// starts the whole app on a random port and calls it with a RestTemplate
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentApiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void courses_comeWithSeedData() {
        ResponseEntity<Course[]> response = restTemplate.getForEntity("/api/courses", Course[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length >= 3);
    }

    @Test
    void createReadAndDeleteStudent() {
        Student student = new Student("Test", "User", "M", "Jr.", "Male", new Date(0), "test.user@example.com");

        ResponseEntity<Student> created = restTemplate.postForEntity("/api/students", student, Student.class);
        assertEquals(HttpStatus.CREATED, created.getStatusCode());
        Long id = created.getBody().getId();
        assertNotNull(id);

        ResponseEntity<Student> found = restTemplate.getForEntity("/api/students/" + id, Student.class);
        assertEquals("Test", found.getBody().getFirstName());

        restTemplate.delete("/api/students/" + id);
        ResponseEntity<String> afterDelete = restTemplate.getForEntity("/api/students/" + id, String.class);
        assertEquals(HttpStatus.NOT_FOUND, afterDelete.getStatusCode());
    }

    @Test
    void createStudent_withSameEmailTwice_returnsConflict() {
        Student student = new Student("Dup", "User", "M", "Jr.", "Male", new Date(0), "dup.user@example.com");
        restTemplate.postForEntity("/api/students", student, Student.class);

        ResponseEntity<String> second = restTemplate.postForEntity("/api/students", student, String.class);

        assertEquals(HttpStatus.CONFLICT, second.getStatusCode());
    }
}

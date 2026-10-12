package com.test.studentmanagement.repository;

import com.test.studentmanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * StudentRepository
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<Student> findByCoursesId(Long courseId);

    // every parameter is optional, a null parameter is ignored
    @Query("select s from Student s where "
            + "(:firstName is null or lower(s.firstName) like lower(concat('%', cast(:firstName as string), '%'))) and "
            + "(:lastName is null or lower(s.lastName) like lower(concat('%', cast(:lastName as string), '%'))) and "
            + "(:email is null or lower(s.email) like lower(concat('%', cast(:email as string), '%'))) and "
            + "(:sex is null or s.sex = :sex) and "
            + "(cast(:dob as date) is null or s.dob = :dob)")
    List<Student> searchStudents(@Param("firstName") String firstName,
                                 @Param("lastName") String lastName,
                                 @Param("email") String email,
                                 @Param("sex") String sex,
                                 @Param("dob") Date dob);
}

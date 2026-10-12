INSERT INTO COURSE (name, description) VALUES ('AI', 'AI programming');
INSERT INTO COURSE (name, description) VALUES ('Data Science', 'Data Science');
INSERT INTO COURSE (name, description) VALUES ('DB', 'DB design');

INSERT INTO STUDENT (first_name, last_name, middle_name, suffix, sex, dob, email)
VALUES
    ('Rama', 'Cheruku', '', 'Sr.', 'Male', '1985-06-15', 'ram.c@gmail.com'),
    ('Rani', 'Veer', '', 'Jr.', 'Female', '2001-08-22', 'rani.k@gmail.com');

INSERT INTO STUDENT_COURSE (student_id, course_id) VALUES (1, 1);
INSERT INTO STUDENT_COURSE (student_id, course_id) VALUES (1, 2);
INSERT INTO STUDENT_COURSE (student_id, course_id) VALUES (2, 3);

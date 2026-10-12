import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Course } from '../../models/course';
import { Student } from '../../models/student';
import { CourseService } from '../../services/course.service';
import { MessageService } from '../../services/message.service';
import { StudentService } from '../../services/student.service';

@Component({
  selector: 'app-student-form',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './student-form.component.html'
})
export class StudentFormComponent implements OnInit {
  form: FormGroup;
  allCourses: Course[] = [];
  selectedCourseIds: number[] = [];
  studentId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private studentService: StudentService,
    private courseService: CourseService,
    private messageService: MessageService
  ) {
    this.form = this.fb.group({
      firstName: ['', Validators.required],
      middleName: ['', Validators.required],
      lastName: ['', Validators.required],
      suffix: ['', Validators.required],
      sex: ['', Validators.required],
      dob: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit() {
    this.courseService.getCourses().subscribe({ next: (courses) => (this.allCourses = courses), error: () => {} });

    // if the url has an id we are editing, otherwise it is a new student
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.studentId = Number(id);
      this.studentService.getStudent(this.studentId).subscribe({
        next: (student) => {
          // backend returns dob as a full timestamp, the date input needs yyyy-MM-dd
          this.form.patchValue({ ...student, dob: student.dob ? String(student.dob).substring(0, 10) : '' });
          this.selectedCourseIds = student.courses.map((course) => course.id!);
        },
        error: () => this.router.navigate(['/students'])
      });
    }
  }

  get firstName() {
    return this.form.get('firstName')!;
  }

  get middleName() {
    return this.form.get('middleName')!;
  }

  get lastName() {
    return this.form.get('lastName')!;
  }

  get suffix() {
    return this.form.get('suffix')!;
  }

  get sex() {
    return this.form.get('sex')!;
  }

  get dob() {
    return this.form.get('dob')!;
  }

  get email() {
    return this.form.get('email')!;
  }

  toggleCourse(courseId: number) {
    if (this.selectedCourseIds.includes(courseId)) {
      this.selectedCourseIds = this.selectedCourseIds.filter((id) => id !== courseId);
    } else {
      this.selectedCourseIds.push(courseId);
    }
  }

  save() {
    this.messageService.clear();
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const student: Student = {
      ...this.form.value,
      courses: this.allCourses.filter((course) => this.selectedCourseIds.includes(course.id!))
    };

    const request = this.studentId
      ? this.studentService.updateStudent(this.studentId, student)
      : this.studentService.addStudent(student);

    request.subscribe({
      next: () => this.router.navigate(['/students']),
      error: () => {} // message is shown by the interceptor, stay on the form
    });
  }
}

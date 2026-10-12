import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CourseService } from '../../services/course.service';
import { MessageService } from '../../services/message.service';

@Component({
  selector: 'app-course-form',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './course-form.component.html'
})
export class CourseFormComponent implements OnInit {
  form: FormGroup;
  courseId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private courseService: CourseService,
    private messageService: MessageService
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', Validators.maxLength(255)]
    });
  }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.courseId = Number(id);
      this.courseService.getCourse(this.courseId).subscribe({
        next: (course) => this.form.patchValue(course),
        error: () => this.router.navigate(['/courses'])
      });
    }
  }

  get name() {
    return this.form.get('name')!;
  }

  get description() {
    return this.form.get('description')!;
  }

  save() {
    this.messageService.clear();
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const request = this.courseId
      ? this.courseService.updateCourse(this.courseId, this.form.value)
      : this.courseService.addCourse(this.form.value);

    request.subscribe({
      next: () => this.router.navigate(['/courses']),
      error: () => {}
    });
  }
}

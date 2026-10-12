import { AsyncPipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { RouterLink } from '@angular/router';
import { Course } from '../../models/course';
import { CourseService } from '../../services/course.service';
import { MessageService } from '../../services/message.service';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';

@Component({
  selector: 'app-course-list',
  standalone: true,
  imports: [AsyncPipe, RouterLink, ConfirmModalComponent],
  templateUrl: './course-list.component.html'
})
export class CourseListComponent implements OnInit {
  courses$: Observable<Course[]>;
  courseToDelete: Course | null = null;

  constructor(private courseService: CourseService, private messageService: MessageService) {
    this.courses$ = this.courseService.courses$;
  }

  ngOnInit() {
    this.courseService.loadCourses();
  }

  askToDelete(course: Course) {
    this.courseToDelete = course;
  }

  confirmDelete() {
    this.messageService.clear();
    const id = this.courseToDelete?.id;
    this.courseToDelete = null;
    if (id) {
      this.courseService.deleteCourse(id).subscribe({ error: () => {} });
    }
  }

  cancelDelete() {
    this.courseToDelete = null;
  }
}

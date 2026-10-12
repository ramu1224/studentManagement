import { AsyncPipe, DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { RouterLink } from '@angular/router';
import { Student } from '../../models/student';
import { MessageService } from '../../services/message.service';
import { StudentService } from '../../services/student.service';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';

@Component({
  selector: 'app-student-list',
  standalone: true,
  imports: [AsyncPipe, DatePipe, RouterLink, ConfirmModalComponent],
  templateUrl: './student-list.component.html'
})
export class StudentListComponent implements OnInit {
  students$: Observable<Student[]>;
  studentToDelete: Student | null = null;

  constructor(private studentService: StudentService, private messageService: MessageService) {
    this.students$ = this.studentService.students$;
  }

  ngOnInit() {
    this.studentService.loadStudents();
  }

  fullName(student: Student): string {
    return [student.firstName, student.middleName, student.lastName, student.suffix]
      .filter((part) => !!part)
      .join(' ');
  }

  courseNames(student: Student): string {
    return student.courses.map((course) => course.name).join(', ');
  }

  askToDelete(student: Student) {
    this.studentToDelete = student;
  }

  confirmDelete() {
    this.messageService.clear();
    const id = this.studentToDelete?.id;
    this.studentToDelete = null;
    if (id) {
      this.studentService.deleteStudent(id).subscribe({ error: () => {} });
    }
  }

  cancelDelete() {
    this.studentToDelete = null;
  }
}

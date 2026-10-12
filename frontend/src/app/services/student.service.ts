import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Student } from '../models/student';

@Injectable({ providedIn: 'root' })
export class StudentService {
  private baseUrl = 'http://localhost:8080/api/students';

  // the list lives in a BehaviorSubject so every component sees the latest students
  private studentsSubject = new BehaviorSubject<Student[]>([]);
  students$ = this.studentsSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadStudents() {
    this.http.get<Student[]>(this.baseUrl).subscribe({
      next: (students) => this.studentsSubject.next(students),
      error: () => {} // the error interceptor already shows the message
    });
  }

  getStudent(id: number): Observable<Student> {
    return this.http.get<Student>(`${this.baseUrl}/${id}`);
  }

  addStudent(student: Student): Observable<Student> {
    return this.http.post<Student>(this.baseUrl, student).pipe(tap(() => this.loadStudents()));
  }

  updateStudent(id: number, student: Student): Observable<Student> {
    return this.http.put<Student>(`${this.baseUrl}/${id}`, student).pipe(tap(() => this.loadStudents()));
  }

  deleteStudent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(tap(() => this.loadStudents()));
  }
}

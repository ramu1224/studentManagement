import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Course } from '../models/course';

@Injectable({ providedIn: 'root' })
export class CourseService {
  private baseUrl = 'http://localhost:8080/api/courses';

  private coursesSubject = new BehaviorSubject<Course[]>([]);
  courses$ = this.coursesSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadCourses() {
    this.http.get<Course[]>(this.baseUrl).subscribe({
      next: (courses) => this.coursesSubject.next(courses),
      error: () => {}
    });
  }

  // used by the student form to show the course checkboxes
  getCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(this.baseUrl);
  }

  getCourse(id: number): Observable<Course> {
    return this.http.get<Course>(`${this.baseUrl}/${id}`);
  }

  addCourse(course: Course): Observable<Course> {
    return this.http.post<Course>(this.baseUrl, course).pipe(tap(() => this.loadCourses()));
  }

  updateCourse(id: number, course: Course): Observable<Course> {
    return this.http.put<Course>(`${this.baseUrl}/${id}`, course).pipe(tap(() => this.loadCourses()));
  }

  deleteCourse(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(tap(() => this.loadCourses()));
  }
}

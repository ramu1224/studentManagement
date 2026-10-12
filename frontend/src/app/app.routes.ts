import { Routes } from '@angular/router';
import { CourseFormComponent } from './components/course-form/course-form.component';
import { CourseListComponent } from './components/course-list/course-list.component';
import { StudentFormComponent } from './components/student-form/student-form.component';
import { StudentListComponent } from './components/student-list/student-list.component';

export const routes: Routes = [
  { path: '', redirectTo: 'students', pathMatch: 'full' },
  { path: 'students', component: StudentListComponent },
  { path: 'students/new', component: StudentFormComponent },
  { path: 'students/:id/edit', component: StudentFormComponent },
  { path: 'courses', component: CourseListComponent },
  { path: 'courses/new', component: CourseFormComponent },
  { path: 'courses/:id/edit', component: CourseFormComponent },
  { path: '**', redirectTo: 'students' }
];

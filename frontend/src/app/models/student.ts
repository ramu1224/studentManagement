import { Course } from './course';

export interface Student {
  id?: number;
  firstName: string;
  middleName: string;
  lastName: string;
  suffix: string;
  sex: string;
  dob: string; // yyyy-MM-dd
  email: string;
  courses: Course[];
}

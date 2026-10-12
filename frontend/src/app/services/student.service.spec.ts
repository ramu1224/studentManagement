import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Student } from '../models/student';
import { StudentService } from './student.service';

describe('StudentService', () => {
  let service: StudentService;
  let httpMock: HttpTestingController;

  const url = 'http://localhost:8080/api/students';
  const ada: Student = { id: 1, firstName: 'Ada', middleName: 'King', lastName: 'Lovelace', suffix: 'Jr.', sex: 'Female', dob: '1815-12-10', email: 'ada@test.com', courses: [] };

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(StudentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('loadStudents puts the students into the stream', () => {
    let result: Student[] = [];
    service.students$.subscribe((students) => (result = students));

    service.loadStudents();
    httpMock.expectOne(url).flush([ada]);

    expect(result).toEqual([ada]);
  });

  it('addStudent sends a POST and reloads the list', () => {
    service.addStudent(ada).subscribe();

    const post = httpMock.expectOne(url);
    expect(post.request.method).toBe('POST');
    post.flush(ada);

    // after saving, the list is loaded again
    const reload = httpMock.expectOne(url);
    expect(reload.request.method).toBe('GET');
    reload.flush([ada]);
  });

  it('deleteStudent sends a DELETE for the right id', () => {
    service.deleteStudent(1).subscribe();

    const del = httpMock.expectOne(`${url}/1`);
    expect(del.request.method).toBe('DELETE');
    del.flush(null);

    httpMock.expectOne(url).flush([]);
  });
});

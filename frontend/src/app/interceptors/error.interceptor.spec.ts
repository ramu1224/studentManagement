import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { MessageService } from '../services/message.service';
import { errorInterceptor } from './error.interceptor';

describe('errorInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;
  let messageService: MessageService;
  let lastMessage: string;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(withInterceptors([errorInterceptor])), provideHttpClientTesting()]
    });
    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    messageService = TestBed.inject(MessageService);
    messageService.message$.subscribe((m) => (lastMessage = m));
  });

  it('shows the message sent by the backend', () => {
    http.get('/test').subscribe({ error: () => {} });
    httpMock.expectOne('/test').flush({ message: 'Student not found with id 9' }, { status: 404, statusText: 'Not Found' });

    expect(lastMessage).toBe('Student not found with id 9');
  });

  it('shows the validation errors from the backend', () => {
    http.post('/test', {}).subscribe({ error: () => {} });
    httpMock.expectOne('/test').flush(
      { message: 'Validation failed', errors: { firstName: 'First name is required' } },
      { status: 400, statusText: 'Bad Request' }
    );

    expect(lastMessage).toBe('First name is required');
  });

  it('shows a friendly message when the server cannot be reached', () => {
    http.get('/test').subscribe({ error: () => {} });
    httpMock.expectOne('/test').error(new ProgressEvent('error'));

    expect(lastMessage).toContain('Cannot connect to the server');
  });
});

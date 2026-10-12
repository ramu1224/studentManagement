import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { MessageService } from '../services/message.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const messageService = inject(MessageService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let text = 'Something went wrong. Please try again.';

      if (error.status === 0) {
        text = 'Cannot connect to the server. Is the backend running?';
      } else if (error.error?.errors) {
        // validation errors from the backend, one message per field
        text = Object.values(error.error.errors).join(', ');
      } else if (error.error?.message) {
        text = error.error.message;
      }

      messageService.show(text);
      return throwError(() => error);
    })
  );
};

import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

// holds the error message that the app component shows at the top of the page
@Injectable({ providedIn: 'root' })
export class MessageService {
  private messageSubject = new BehaviorSubject<string>('');
  message$ = this.messageSubject.asObservable();

  show(message: string) {
    this.messageSubject.next(message);
  }

  clear() {
    this.messageSubject.next('');
  }
}

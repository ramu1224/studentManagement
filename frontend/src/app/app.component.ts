import { AsyncPipe } from '@angular/common';
import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MessageService } from './services/message.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, AsyncPipe],
  templateUrl: './app.component.html'
})
export class AppComponent {
  message$: Observable<string>;

  constructor(private messageService: MessageService) {
    this.message$ = this.messageService.message$;
  }

  closeMessage() {
    this.messageService.clear();
  }
}

import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-confirm-modal',
  standalone: true,
  templateUrl: './confirm-modal.component.html'
})
export class ConfirmModalComponent {
  @Input() title = 'Are you sure?';
  @Input() message = '';

  // the parent decides what to do when the user clicks a button
  @Output() confirmed = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();
}

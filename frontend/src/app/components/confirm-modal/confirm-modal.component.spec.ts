import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ConfirmModalComponent } from './confirm-modal.component';

describe('ConfirmModalComponent', () => {
  let fixture: ComponentFixture<ConfirmModalComponent>;
  let component: ConfirmModalComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({ imports: [ConfirmModalComponent] }).compileComponents();
    fixture = TestBed.createComponent(ConfirmModalComponent);
    component = fixture.componentInstance;
    component.title = 'Delete student';
    component.message = 'Are you sure?';
    fixture.detectChanges();
  });

  it('shows the title and message', () => {
    const text = fixture.nativeElement.textContent;
    expect(text).toContain('Delete student');
    expect(text).toContain('Are you sure?');
  });

  it('emits confirmed when Delete is clicked', () => {
    spyOn(component.confirmed, 'emit');
    fixture.nativeElement.querySelector('.btn-danger').click();
    expect(component.confirmed.emit).toHaveBeenCalled();
  });

  it('emits cancelled when Cancel is clicked', () => {
    spyOn(component.cancelled, 'emit');
    fixture.nativeElement.querySelector('.btn-secondary').click();
    expect(component.cancelled.emit).toHaveBeenCalled();
  });
});

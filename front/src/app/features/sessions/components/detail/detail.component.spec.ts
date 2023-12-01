import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let service: SessionService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const sessionMock : Session = {
    name: '',
    description: '',
    date: new Date(),
    teacher_id: 0,
    users: [mockSessionService.sessionInformation.id]
  };

  let sessionApiServiceMock : jest.Mocked<SessionApiService>;

  beforeEach(async () => {

    sessionApiServiceMock = {
      detail: jest.fn().mockReturnValue(of(sessionMock))
    } as unknown as jest.Mocked<SessionApiService>;

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: sessionApiServiceMock },
      ],
    })
      .compileComponents();
    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    // fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should window.history.back() when back()', () => {
    jest.spyOn(window.history, 'back');
    component.back()
    expect(window.history.back).toHaveBeenCalled();
  });

  // A FINALISER !!
  it('should set isParticipate to True when participate function is called', () => {
    component.ngOnInit();
    expect(sessionApiServiceMock.detail).toHaveBeenCalled();
    expect(component.session).toBe(sessionMock);
    expect(component.isParticipate).toBeTruthy()
    // expect(component.session).toBeDefined();
  })

});
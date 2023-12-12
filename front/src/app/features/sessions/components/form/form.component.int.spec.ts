import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { TeacherService } from 'src/app/services/teacher.service';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const sessionMock = {
    name: 'name',
    description: 'description',
    date: new Date(),
    teacher_id: 1,
    users: []
  } as jest.Mocked<Session>;

  const sessionInfoMock: SessionInformation = {
    admin: true,
    id: 1,
    token: '',
    type: '',
    username: '',
    firstName: '',
    lastName: ''
  }

  let sessionService: SessionService;
  let sessionApiService: SessionApiService;
  let matSnackBar: MatSnackBar;
  let teacherService: TeacherService

  let routerMock: jest.Mocked<Router>
  let activatedRouteMock: jest.Mocked<ActivatedRoute>;

  beforeEach(async () => {

    routerMock = {
      url: '/',
      navigate: jest.fn(),
    } as unknown as jest.Mocked<Router>;

    activatedRouteMock = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue(of('1'))
        }
      }
    } as unknown as jest.Mocked<ActivatedRoute>;

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        SessionService,
        SessionApiService,
        MatSnackBar,
        TeacherService,
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    sessionService = TestBed.inject(SessionService);
    sessionApiService = TestBed.inject(SessionApiService);
    matSnackBar = TestBed.inject(MatSnackBar);
    teacherService = TestBed.inject(TeacherService);

    sessionService.logIn(sessionInfoMock);

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('should create session properly', () => {
    expect(component.onUpdate).toBeFalsy()

    const session = component.sessionForm?.value as Session;
    const sessionApiServiceSpy = jest.spyOn(sessionApiService, 'create').mockReturnValue(of(session));
    const matSnackBarSpy = jest.spyOn(matSnackBar, 'open');

    component.submit()

    expect(sessionApiServiceSpy).toHaveBeenCalledWith(session);
    expect(matSnackBarSpy).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should update session properly', () => {
    // @ts-ignore : modify read-only property routerMock.url
    routerMock.url = '/update';
    component.ngOnInit();
    expect(component.onUpdate).toBeTruthy()

    const session = component.sessionForm?.value as Session;
    const sessionApiServiceSpy = jest.spyOn(sessionApiService, 'update').mockReturnValue(of(session));
    const matSnackBarSpy = jest.spyOn(matSnackBar, 'open');

    component.submit()

    // @ts-ignore : access private property component.id
    expect(sessionApiServiceSpy).toHaveBeenCalledWith(component.id, session);
    expect(matSnackBarSpy).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  });

});
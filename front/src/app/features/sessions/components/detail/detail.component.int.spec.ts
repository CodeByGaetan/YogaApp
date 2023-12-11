import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { TeacherService } from 'src/app/services/teacher.service';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { ActivatedRoute, Router } from '@angular/router';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { FileCoverage } from 'istanbul-lib-coverage';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  const sessionInfoMock: SessionInformation = {
    admin: true,
    id: 1,
    token: '',
    type: '',
    username: '',
    firstName: '',
    lastName: ''
  }

  const teacherMock: Teacher = {
    id: 0,
    lastName: '',
    firstName: '',
    createdAt: new Date(),
    updatedAt: new Date()
  }

  let sessionService: SessionService;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let matSnackBar: MatSnackBar;
  let router: Router;

  let activatedRouteMock: jest.Mocked<ActivatedRoute>;

  beforeEach(async () => {

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
        MatSnackBarModule,
        ReactiveFormsModule,
        BrowserAnimationsModule,
        NoopAnimationsModule
      ],
      declarations: [DetailComponent],
      providers: [
        SessionService,
        SessionApiService,
        TeacherService,
        MatSnackBar,
        { provide: ActivatedRoute, useValue: activatedRouteMock },
      ],
    })
      .compileComponents();

    sessionService = TestBed.inject(SessionService);
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);

    sessionService.logIn(sessionInfoMock);

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('should delete session properly', async () => {
    const sessionApiServiceSpy = jest.spyOn(sessionApiService, 'delete').mockReturnValue(of({} as any));
    const matSnackBarSpy = jest.spyOn(matSnackBar, 'open');
    const routerSpy = jest.spyOn(router, 'navigate');

    component.delete();

    expect(sessionApiServiceSpy).toHaveBeenCalledWith(component.sessionId);
    expect(matSnackBarSpy).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    await expect(routerSpy).toHaveBeenCalledWith(['sessions']);
  });

  // it('should call sessionApiService.delete(), matSnackBar.open() and router.navigate() when delete()', () => {
  //   component.delete();
  //   
  //   
  //   expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  // });

  // it('should call sessionApiService.participate() when participate()', () => {
  //   component.participate();
  //   expect(sessionApiServiceMock.participate).toHaveBeenCalledWith(component.sessionId, component.userId);
  // });

  // it('should call sessionApiService.unParticipate() when unParticipate()', () => {
  //   component.unParticipate();
  //   expect(sessionApiServiceMock.unParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
  // });

});
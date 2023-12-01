import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Router } from '@angular/router';


// class mockAuthService extends AuthService {

//   const sessionInformation : SessionInformation = {
//     token: '',
//     type: '',
//     id: 0,
//     username: '',
//     firstName: '',
//     lastName: '',
//     admin: false
//   }

//   public override login(loginRequest: LoginRequest): Observable<SessionInformation> {
//       return this.sessionInformation;
//   }
// }


describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const sessionInformation: SessionInformation = {
    token: '',
    type: '',
    id: 0,
    username: '',
    firstName: '',
    lastName: '',
    admin: false
  }

  let authServiceMock: jest.Mocked<AuthService>;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let routerMock: jest.Mocked<Router>;

  beforeEach(async () => {

    authServiceMock = {
      login: jest.fn().mockReturnValue(of(sessionInformation)),
    } as unknown as jest.Mocked<AuthService>;

    sessionServiceMock = {
      logIn: jest.fn()
    } as unknown as jest.Mocked<SessionService>;

    routerMock = {
      navigate: jest.fn()
    } as unknown as jest.Mocked<Router>;

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        SessionService,
        { provide: AuthService, useValue: authServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: routerMock },
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should validate form when inputs field are not empty', async () => {

    // TEST A RESOUDRE
    // const emailInput = fixture.nativeElement.querySelector('#email') as HTMLInputElement;
    // emailInput.value = 'test@gmail.com';
    // const passwordInput = fixture.nativeElement.querySelector("#password") as HTMLInputElement;   
    // passwordInput.value = 'gaga1232121';
    // fixture.detectChanges();
    // await fixture.whenStable();

    component.form.setValue({ email: "test@gmail.com", password: "azerty" })
    expect(component.form.invalid).toBeFalsy();
  });

  it('should authService.login() when submit()', () => {
    // jest.spyOn(authServiceMock, 'login');
    component.submit()
    expect(authServiceMock.login).toHaveBeenCalled();
  });

  it('should sessionService.logIn() when submit()', () => {
    // jest.spyOn(sessionServiceMock, 'logIn');
    component.submit();
    expect(sessionServiceMock.logIn).toHaveBeenCalled();
  });

  it('should navigate to /sessions when submit()', () => {
    component.submit();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions'])
  })

  it('should set onErrror to true when submit() authService.login() throw error', () => {
    authServiceMock.login.mockImplementation(() => {
      const error = new Error('my error message')
      return throwError(() => error);
    });
    component.submit();
    expect(component.onError).toBeTruthy();
  })

});
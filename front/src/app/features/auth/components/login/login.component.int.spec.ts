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


describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  let authService: AuthService;
  let sessionService:SessionService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        AuthService,
        SessionService
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

    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);

    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('should login correctly', () => {

    const sessionInfoMock : SessionInformation = {
        token: '',
        type: '',
        id: 0,
        username: '',
        firstName: '',
        lastName: '',
        admin: false
    }
    const authServiceSpy = jest.spyOn(authService, 'login').mockReturnValue(of(sessionInfoMock));
    const sessionServiceSpy = jest.spyOn(sessionService, 'logIn');
    const routerSpy = jest.spyOn(router, 'navigate');

    component.form.setValue({ email: "test@gmail.com", password: "azerty" });
    component.submit();

    expect(authServiceSpy).toHaveBeenCalledWith(component.form.value);
    expect(sessionServiceSpy).toHaveBeenCalled();
    expect(sessionService.isLogged).toBeTruthy();
    expect(sessionService.sessionInformation).toBe(sessionInfoMock);
    expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBeFalsy();
  })

});
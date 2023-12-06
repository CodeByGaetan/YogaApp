import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import { User } from 'src/app/interfaces/user.interface';
import { of } from 'rxjs';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  let userService : UserService;
  let sessionService : SessionService;

  const sessionInfoMock : SessionInformation = {
    token: '',
    type: '',
    id: 0,
    username: '',
    firstName: '',
    lastName: '',
    admin: false
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        UserService,
        SessionService
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;

    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);

    sessionService.logIn(sessionInfoMock);

    fixture.detectChanges()

  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('should delete user properly', () => {
    const userServiceSpy = jest.spyOn(userService, 'delete')

    component.delete();
    
    expect(userServiceSpy).toHaveBeenCalledWith(sessionInfoMock.id.toString());
    
    // expect(matSnackBarMock.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    // expect(sessionServiceMock.logOut).toHaveBeenCalled();
    // expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });

});
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

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  let sessionServiceMock: jest.Mocked<SessionService>;
  let userServiceMock: jest.Mocked<UserService>;
  let matSnackBarMock: jest.Mocked<MatSnackBar>;
  let routerMock: jest.Mocked<Router>;

  beforeEach(async () => {

    userServiceMock = {
      getById: jest.fn().mockReturnValue(of({} as User)),
      delete: jest.fn().mockReturnValue(of({} as any))
    } as unknown as jest.Mocked<UserService>;

    sessionServiceMock = {
      sessionInformation: {
        admin: true,
        id: 1
      },
      logOut: jest.fn()
    } as unknown as jest.Mocked<SessionService>;

    matSnackBarMock = {
      open: jest.fn()
    } as unknown as jest.Mocked<MatSnackBar>;

    routerMock = {
      navigate: jest.fn()
    } as unknown as jest.Mocked<Router>;

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
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: UserService, useValue: userServiceMock },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        { provide: Router, useValue: routerMock },
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('should call userService.getById() and set user when ngOnInit()', () => {
    expect(component.user).toBeUndefined();
    component.ngOnInit();
    expect(userServiceMock.getById).toHaveBeenCalledWith(sessionServiceMock.sessionInformation!.id.toString());
    expect(component.user).toBeDefined();
  });

  it('should call window.history.back() when back()', () => {
    jest.spyOn(window.history, 'back');
    component.back();
    expect(window.history.back).toHaveBeenCalled();
  });

  it('should call userService.delete(), matSnackBar.open(), sessionService.logOut() and router.navigate() when delete()', () => {
    component.delete();
    expect(userServiceMock.delete).toHaveBeenCalledWith(sessionServiceMock.sessionInformation!.id.toString());
    expect(matSnackBarMock.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });

});
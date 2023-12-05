import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { Observable } from 'rxjs';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return a Observable of boolean when $isLogged()', () => {
    expect(service.$isLogged()).toBeInstanceOf(Observable<boolean>);
  });

  it('should set sessionInformation and isLogged when logIn()', () => {
    const sessionInformationMock : SessionInformation = {
      token: '',
      type: '',
      id: 0,
      username: '',
      firstName: '',
      lastName: '',
      admin: false
    }
    service.logIn(sessionInformationMock);
    expect(service.sessionInformation).toBe(sessionInformationMock);
    expect(service.isLogged).toBeTruthy();
  });

  it('should unset sessionInformation and isLogged when logIn()', () => {
    service.logOut();
    expect(service.sessionInformation).toBeUndefined();
    expect(service.isLogged).toBeFalsy();
  });

});
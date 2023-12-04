import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {

  const sessionMock : Session = {
    name: '',
    description: '',
    date: new Date(),
    teacher_id: 0,
    users: []
  }

  let service: SessionApiService;
  let controller : HttpTestingController;
  let pathService = 'api/session';

  beforeEach(() : void => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule]
    });
    service = TestBed.inject(SessionApiService);
    controller = TestBed.inject(HttpTestingController);
  });

  afterEach((): void => {
    controller.verify()
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should handle get All request properly', (done) => {
    const sessionListMock : Session[] = [sessionMock];
    service.all()
      .subscribe((response) => {
        expect(response).toBe(sessionListMock);
        done()
      });
    const request: TestRequest = controller.expectOne(`${pathService}`);
    expect(request.request.method).toBe('GET');
    request.flush(sessionListMock);
  });

  // it('should handle get Detail request properly', (done) => {
  //   service.detail('1')
  //     .subscribe((response) => {
  //       expect(response).toBe(sessionListMock);
  //       done()
  //     });
  //   const request: TestRequest = controller.expectOne(`${pathService}`);
  //   expect(request.request.method).toBe('GET');
  //   request.flush(sessionListMock);
  // });


  
});
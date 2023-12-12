import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let service: UserService;
  let controller : HttpTestingController;
  const pathService = 'api/user';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule]
    });
    service = TestBed.inject(UserService);
    controller = TestBed.inject(HttpTestingController);
  });

  afterEach((): void => {
    controller.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should handle getById request properly', (done) => {
    const userMock : User = {
      id: 0,
      email: '',
      lastName: '',
      firstName: '',
      admin: false,
      password: '',
      createdAt: new Date()
    }
    const id = '1';
    service.getById(id)
      .subscribe((response) => {
        expect(response).toBe(userMock);
        done()
      });
    const request: TestRequest = controller.expectOne(`${pathService}/${id}`);
    expect(request.request.method).toBe('GET');
    request.flush(userMock);
  });

  it('should handle Delete request properly', (done) => {
    const id = '1';
    service.delete(id)
      .subscribe((response) => {
        expect(response).toBe(null);
        done()
      });
    const request: TestRequest = controller.expectOne(`${pathService}/${id}`);
    expect(request.request.method).toBe('DELETE');
    request.flush(null);
  });


});

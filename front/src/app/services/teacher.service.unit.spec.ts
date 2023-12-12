import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {

  const teacherMock : Teacher = {
    id: 0,
    lastName: '',
    firstName: '',
    createdAt: new Date(),
    updatedAt: new Date()
  }
  
  let service: TeacherService;
  let controller : HttpTestingController;
  const pathService = 'api/teacher';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule]
    });
    service = TestBed.inject(TeacherService);
    controller = TestBed.inject(HttpTestingController);
  });

  afterEach((): void => {
    controller.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should handle get All request properly', (done) => {
    const teacherListMock : Teacher[] = [teacherMock];
    service.all()
      .subscribe((response) => {
        expect(response).toBe(teacherListMock);
        done()
      });
    const request: TestRequest = controller.expectOne(`${pathService}`);
    expect(request.request.method).toBe('GET');
    request.flush(teacherListMock);
  });

  it('should handle get Detail request properly', (done) => {
    const id = '1';
    service.detail(id)
      .subscribe((response) => {
        expect(response).toBe(teacherMock);
        done()
      });
    const request: TestRequest = controller.expectOne(`${pathService}/${id}`);
    expect(request.request.method).toBe('GET');
    request.flush(teacherMock);
  });
});

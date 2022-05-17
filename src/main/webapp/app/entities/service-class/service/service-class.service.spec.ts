import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IServiceClass, ServiceClass } from '../service-class.model';

import { ServiceClassService } from './service-class.service';

describe('ServiceClass Service', () => {
  let service: ServiceClassService;
  let httpMock: HttpTestingController;
  let elemDefault: IServiceClass;
  let expectedResult: IServiceClass | IServiceClass[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ServiceClassService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomService: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ServiceClass', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ServiceClass()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServiceClass', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomService: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServiceClass', () => {
      const patchObject = Object.assign(
        {
          nomService: 'BBBBBB',
        },
        new ServiceClass()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServiceClass', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomService: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ServiceClass', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addServiceClassToCollectionIfMissing', () => {
      it('should add a ServiceClass to an empty array', () => {
        const serviceClass: IServiceClass = { id: 123 };
        expectedResult = service.addServiceClassToCollectionIfMissing([], serviceClass);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceClass);
      });

      it('should not add a ServiceClass to an array that contains it', () => {
        const serviceClass: IServiceClass = { id: 123 };
        const serviceClassCollection: IServiceClass[] = [
          {
            ...serviceClass,
          },
          { id: 456 },
        ];
        expectedResult = service.addServiceClassToCollectionIfMissing(serviceClassCollection, serviceClass);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServiceClass to an array that doesn't contain it", () => {
        const serviceClass: IServiceClass = { id: 123 };
        const serviceClassCollection: IServiceClass[] = [{ id: 456 }];
        expectedResult = service.addServiceClassToCollectionIfMissing(serviceClassCollection, serviceClass);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceClass);
      });

      it('should add only unique ServiceClass to an array', () => {
        const serviceClassArray: IServiceClass[] = [{ id: 123 }, { id: 456 }, { id: 52273 }];
        const serviceClassCollection: IServiceClass[] = [{ id: 123 }];
        expectedResult = service.addServiceClassToCollectionIfMissing(serviceClassCollection, ...serviceClassArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serviceClass: IServiceClass = { id: 123 };
        const serviceClass2: IServiceClass = { id: 456 };
        expectedResult = service.addServiceClassToCollectionIfMissing([], serviceClass, serviceClass2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceClass);
        expect(expectedResult).toContain(serviceClass2);
      });

      it('should accept null and undefined values', () => {
        const serviceClass: IServiceClass = { id: 123 };
        expectedResult = service.addServiceClassToCollectionIfMissing([], null, serviceClass, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceClass);
      });

      it('should return initial array if no ServiceClass is added', () => {
        const serviceClassCollection: IServiceClass[] = [{ id: 123 }];
        expectedResult = service.addServiceClassToCollectionIfMissing(serviceClassCollection, undefined, null);
        expect(expectedResult).toEqual(serviceClassCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

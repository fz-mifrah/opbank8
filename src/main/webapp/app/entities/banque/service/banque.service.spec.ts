import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBanque, Banque } from '../banque.model';

import { BanqueService } from './banque.service';

describe('Banque Service', () => {
  let service: BanqueService;
  let httpMock: HttpTestingController;
  let elemDefault: IBanque;
  let expectedResult: IBanque | IBanque[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BanqueService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
      adresse: 'AAAAAAA',
      email: 'AAAAAAA',
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

    it('should create a Banque', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Banque()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Banque', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          adresse: 'BBBBBB',
          email: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Banque', () => {
      const patchObject = Object.assign(
        {
          adresse: 'BBBBBB',
        },
        new Banque()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Banque', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          adresse: 'BBBBBB',
          email: 'BBBBBB',
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

    it('should delete a Banque', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBanqueToCollectionIfMissing', () => {
      it('should add a Banque to an empty array', () => {
        const banque: IBanque = { id: 123 };
        expectedResult = service.addBanqueToCollectionIfMissing([], banque);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(banque);
      });

      it('should not add a Banque to an array that contains it', () => {
        const banque: IBanque = { id: 123 };
        const banqueCollection: IBanque[] = [
          {
            ...banque,
          },
          { id: 456 },
        ];
        expectedResult = service.addBanqueToCollectionIfMissing(banqueCollection, banque);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Banque to an array that doesn't contain it", () => {
        const banque: IBanque = { id: 123 };
        const banqueCollection: IBanque[] = [{ id: 456 }];
        expectedResult = service.addBanqueToCollectionIfMissing(banqueCollection, banque);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(banque);
      });

      it('should add only unique Banque to an array', () => {
        const banqueArray: IBanque[] = [{ id: 123 }, { id: 456 }, { id: 9432 }];
        const banqueCollection: IBanque[] = [{ id: 123 }];
        expectedResult = service.addBanqueToCollectionIfMissing(banqueCollection, ...banqueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const banque: IBanque = { id: 123 };
        const banque2: IBanque = { id: 456 };
        expectedResult = service.addBanqueToCollectionIfMissing([], banque, banque2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(banque);
        expect(expectedResult).toContain(banque2);
      });

      it('should accept null and undefined values', () => {
        const banque: IBanque = { id: 123 };
        expectedResult = service.addBanqueToCollectionIfMissing([], null, banque, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(banque);
      });

      it('should return initial array if no Banque is added', () => {
        const banqueCollection: IBanque[] = [{ id: 123 }];
        expectedResult = service.addBanqueToCollectionIfMissing(banqueCollection, undefined, null);
        expect(expectedResult).toEqual(banqueCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

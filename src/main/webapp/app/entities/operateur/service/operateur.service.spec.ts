import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOperateur, Operateur } from '../operateur.model';

import { OperateurService } from './operateur.service';

describe('Operateur Service', () => {
  let service: OperateurService;
  let httpMock: HttpTestingController;
  let elemDefault: IOperateur;
  let expectedResult: IOperateur | IOperateur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OperateurService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomOp: 'AAAAAAA',
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

    it('should create a Operateur', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Operateur()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Operateur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomOp: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Operateur', () => {
      const patchObject = Object.assign(
        {
          nomOp: 'BBBBBB',
        },
        new Operateur()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Operateur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomOp: 'BBBBBB',
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

    it('should delete a Operateur', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOperateurToCollectionIfMissing', () => {
      it('should add a Operateur to an empty array', () => {
        const operateur: IOperateur = { id: 123 };
        expectedResult = service.addOperateurToCollectionIfMissing([], operateur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operateur);
      });

      it('should not add a Operateur to an array that contains it', () => {
        const operateur: IOperateur = { id: 123 };
        const operateurCollection: IOperateur[] = [
          {
            ...operateur,
          },
          { id: 456 },
        ];
        expectedResult = service.addOperateurToCollectionIfMissing(operateurCollection, operateur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Operateur to an array that doesn't contain it", () => {
        const operateur: IOperateur = { id: 123 };
        const operateurCollection: IOperateur[] = [{ id: 456 }];
        expectedResult = service.addOperateurToCollectionIfMissing(operateurCollection, operateur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operateur);
      });

      it('should add only unique Operateur to an array', () => {
        const operateurArray: IOperateur[] = [{ id: 123 }, { id: 456 }, { id: 87795 }];
        const operateurCollection: IOperateur[] = [{ id: 123 }];
        expectedResult = service.addOperateurToCollectionIfMissing(operateurCollection, ...operateurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const operateur: IOperateur = { id: 123 };
        const operateur2: IOperateur = { id: 456 };
        expectedResult = service.addOperateurToCollectionIfMissing([], operateur, operateur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operateur);
        expect(expectedResult).toContain(operateur2);
      });

      it('should accept null and undefined values', () => {
        const operateur: IOperateur = { id: 123 };
        expectedResult = service.addOperateurToCollectionIfMissing([], null, operateur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operateur);
      });

      it('should return initial array if no Operateur is added', () => {
        const operateurCollection: IOperateur[] = [{ id: 123 }];
        expectedResult = service.addOperateurToCollectionIfMissing(operateurCollection, undefined, null);
        expect(expectedResult).toEqual(operateurCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPaimentFacture, PaimentFacture } from '../paiment-facture.model';

import { PaimentFactureService } from './paiment-facture.service';

describe('PaimentFacture Service', () => {
  let service: PaimentFactureService;
  let httpMock: HttpTestingController;
  let elemDefault: IPaimentFacture;
  let expectedResult: IPaimentFacture | IPaimentFacture[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PaimentFactureService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      referance: 0,
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

    it('should create a PaimentFacture', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PaimentFacture()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaimentFacture', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          referance: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaimentFacture', () => {
      const patchObject = Object.assign(
        {
          referance: 1,
        },
        new PaimentFacture()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaimentFacture', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          referance: 1,
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

    it('should delete a PaimentFacture', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPaimentFactureToCollectionIfMissing', () => {
      it('should add a PaimentFacture to an empty array', () => {
        const paimentFacture: IPaimentFacture = { id: 123 };
        expectedResult = service.addPaimentFactureToCollectionIfMissing([], paimentFacture);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paimentFacture);
      });

      it('should not add a PaimentFacture to an array that contains it', () => {
        const paimentFacture: IPaimentFacture = { id: 123 };
        const paimentFactureCollection: IPaimentFacture[] = [
          {
            ...paimentFacture,
          },
          { id: 456 },
        ];
        expectedResult = service.addPaimentFactureToCollectionIfMissing(paimentFactureCollection, paimentFacture);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaimentFacture to an array that doesn't contain it", () => {
        const paimentFacture: IPaimentFacture = { id: 123 };
        const paimentFactureCollection: IPaimentFacture[] = [{ id: 456 }];
        expectedResult = service.addPaimentFactureToCollectionIfMissing(paimentFactureCollection, paimentFacture);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paimentFacture);
      });

      it('should add only unique PaimentFacture to an array', () => {
        const paimentFactureArray: IPaimentFacture[] = [{ id: 123 }, { id: 456 }, { id: 56348 }];
        const paimentFactureCollection: IPaimentFacture[] = [{ id: 123 }];
        expectedResult = service.addPaimentFactureToCollectionIfMissing(paimentFactureCollection, ...paimentFactureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paimentFacture: IPaimentFacture = { id: 123 };
        const paimentFacture2: IPaimentFacture = { id: 456 };
        expectedResult = service.addPaimentFactureToCollectionIfMissing([], paimentFacture, paimentFacture2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paimentFacture);
        expect(expectedResult).toContain(paimentFacture2);
      });

      it('should accept null and undefined values', () => {
        const paimentFacture: IPaimentFacture = { id: 123 };
        expectedResult = service.addPaimentFactureToCollectionIfMissing([], null, paimentFacture, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paimentFacture);
      });

      it('should return initial array if no PaimentFacture is added', () => {
        const paimentFactureCollection: IPaimentFacture[] = [{ id: 123 }];
        expectedResult = service.addPaimentFactureToCollectionIfMissing(paimentFactureCollection, undefined, null);
        expect(expectedResult).toEqual(paimentFactureCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

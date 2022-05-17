import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVirement, Virement } from '../virement.model';

import { VirementService } from './virement.service';

describe('Virement Service', () => {
  let service: VirementService;
  let httpMock: HttpTestingController;
  let elemDefault: IVirement;
  let expectedResult: IVirement | IVirement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VirementService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
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

    it('should create a Virement', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Virement()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Virement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Virement', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
        },
        new Virement()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Virement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
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

    it('should delete a Virement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVirementToCollectionIfMissing', () => {
      it('should add a Virement to an empty array', () => {
        const virement: IVirement = { id: 123 };
        expectedResult = service.addVirementToCollectionIfMissing([], virement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(virement);
      });

      it('should not add a Virement to an array that contains it', () => {
        const virement: IVirement = { id: 123 };
        const virementCollection: IVirement[] = [
          {
            ...virement,
          },
          { id: 456 },
        ];
        expectedResult = service.addVirementToCollectionIfMissing(virementCollection, virement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Virement to an array that doesn't contain it", () => {
        const virement: IVirement = { id: 123 };
        const virementCollection: IVirement[] = [{ id: 456 }];
        expectedResult = service.addVirementToCollectionIfMissing(virementCollection, virement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(virement);
      });

      it('should add only unique Virement to an array', () => {
        const virementArray: IVirement[] = [{ id: 123 }, { id: 456 }, { id: 65019 }];
        const virementCollection: IVirement[] = [{ id: 123 }];
        expectedResult = service.addVirementToCollectionIfMissing(virementCollection, ...virementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const virement: IVirement = { id: 123 };
        const virement2: IVirement = { id: 456 };
        expectedResult = service.addVirementToCollectionIfMissing([], virement, virement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(virement);
        expect(expectedResult).toContain(virement2);
      });

      it('should accept null and undefined values', () => {
        const virement: IVirement = { id: 123 };
        expectedResult = service.addVirementToCollectionIfMissing([], null, virement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(virement);
      });

      it('should return initial array if no Virement is added', () => {
        const virementCollection: IVirement[] = [{ id: 123 }];
        expectedResult = service.addVirementToCollectionIfMissing(virementCollection, undefined, null);
        expect(expectedResult).toEqual(virementCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

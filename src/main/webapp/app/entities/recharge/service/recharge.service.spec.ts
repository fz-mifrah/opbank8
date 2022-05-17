import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRecharge, Recharge } from '../recharge.model';

import { RechargeService } from './recharge.service';

describe('Recharge Service', () => {
  let service: RechargeService;
  let httpMock: HttpTestingController;
  let elemDefault: IRecharge;
  let expectedResult: IRecharge | IRecharge[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RechargeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      numTel: 0,
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

    it('should create a Recharge', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Recharge()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Recharge', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numTel: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Recharge', () => {
      const patchObject = Object.assign(
        {
          numTel: 1,
        },
        new Recharge()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Recharge', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numTel: 1,
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

    it('should delete a Recharge', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRechargeToCollectionIfMissing', () => {
      it('should add a Recharge to an empty array', () => {
        const recharge: IRecharge = { id: 123 };
        expectedResult = service.addRechargeToCollectionIfMissing([], recharge);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recharge);
      });

      it('should not add a Recharge to an array that contains it', () => {
        const recharge: IRecharge = { id: 123 };
        const rechargeCollection: IRecharge[] = [
          {
            ...recharge,
          },
          { id: 456 },
        ];
        expectedResult = service.addRechargeToCollectionIfMissing(rechargeCollection, recharge);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Recharge to an array that doesn't contain it", () => {
        const recharge: IRecharge = { id: 123 };
        const rechargeCollection: IRecharge[] = [{ id: 456 }];
        expectedResult = service.addRechargeToCollectionIfMissing(rechargeCollection, recharge);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recharge);
      });

      it('should add only unique Recharge to an array', () => {
        const rechargeArray: IRecharge[] = [{ id: 123 }, { id: 456 }, { id: 26746 }];
        const rechargeCollection: IRecharge[] = [{ id: 123 }];
        expectedResult = service.addRechargeToCollectionIfMissing(rechargeCollection, ...rechargeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const recharge: IRecharge = { id: 123 };
        const recharge2: IRecharge = { id: 456 };
        expectedResult = service.addRechargeToCollectionIfMissing([], recharge, recharge2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recharge);
        expect(expectedResult).toContain(recharge2);
      });

      it('should accept null and undefined values', () => {
        const recharge: IRecharge = { id: 123 };
        expectedResult = service.addRechargeToCollectionIfMissing([], null, recharge, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recharge);
      });

      it('should return initial array if no Recharge is added', () => {
        const rechargeCollection: IRecharge[] = [{ id: 123 }];
        expectedResult = service.addRechargeToCollectionIfMissing(rechargeCollection, undefined, null);
        expect(expectedResult).toEqual(rechargeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

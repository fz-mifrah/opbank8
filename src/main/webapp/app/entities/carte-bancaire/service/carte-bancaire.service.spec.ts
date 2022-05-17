import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICarteBancaire, CarteBancaire } from '../carte-bancaire.model';

import { CarteBancaireService } from './carte-bancaire.service';

describe('CarteBancaire Service', () => {
  let service: CarteBancaireService;
  let httpMock: HttpTestingController;
  let elemDefault: ICarteBancaire;
  let expectedResult: ICarteBancaire | ICarteBancaire[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CarteBancaireService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      numCarte: 'AAAAAAA',
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

    it('should create a CarteBancaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CarteBancaire()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CarteBancaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numCarte: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CarteBancaire', () => {
      const patchObject = Object.assign({}, new CarteBancaire());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CarteBancaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numCarte: 'BBBBBB',
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

    it('should delete a CarteBancaire', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCarteBancaireToCollectionIfMissing', () => {
      it('should add a CarteBancaire to an empty array', () => {
        const carteBancaire: ICarteBancaire = { id: 123 };
        expectedResult = service.addCarteBancaireToCollectionIfMissing([], carteBancaire);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(carteBancaire);
      });

      it('should not add a CarteBancaire to an array that contains it', () => {
        const carteBancaire: ICarteBancaire = { id: 123 };
        const carteBancaireCollection: ICarteBancaire[] = [
          {
            ...carteBancaire,
          },
          { id: 456 },
        ];
        expectedResult = service.addCarteBancaireToCollectionIfMissing(carteBancaireCollection, carteBancaire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CarteBancaire to an array that doesn't contain it", () => {
        const carteBancaire: ICarteBancaire = { id: 123 };
        const carteBancaireCollection: ICarteBancaire[] = [{ id: 456 }];
        expectedResult = service.addCarteBancaireToCollectionIfMissing(carteBancaireCollection, carteBancaire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(carteBancaire);
      });

      it('should add only unique CarteBancaire to an array', () => {
        const carteBancaireArray: ICarteBancaire[] = [{ id: 123 }, { id: 456 }, { id: 46019 }];
        const carteBancaireCollection: ICarteBancaire[] = [{ id: 123 }];
        expectedResult = service.addCarteBancaireToCollectionIfMissing(carteBancaireCollection, ...carteBancaireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const carteBancaire: ICarteBancaire = { id: 123 };
        const carteBancaire2: ICarteBancaire = { id: 456 };
        expectedResult = service.addCarteBancaireToCollectionIfMissing([], carteBancaire, carteBancaire2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(carteBancaire);
        expect(expectedResult).toContain(carteBancaire2);
      });

      it('should accept null and undefined values', () => {
        const carteBancaire: ICarteBancaire = { id: 123 };
        expectedResult = service.addCarteBancaireToCollectionIfMissing([], null, carteBancaire, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(carteBancaire);
      });

      it('should return initial array if no CarteBancaire is added', () => {
        const carteBancaireCollection: ICarteBancaire[] = [{ id: 123 }];
        expectedResult = service.addCarteBancaireToCollectionIfMissing(carteBancaireCollection, undefined, null);
        expect(expectedResult).toEqual(carteBancaireCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

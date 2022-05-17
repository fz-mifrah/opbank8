import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICarteBancaire, getCarteBancaireIdentifier } from '../carte-bancaire.model';

export type EntityResponseType = HttpResponse<ICarteBancaire>;
export type EntityArrayResponseType = HttpResponse<ICarteBancaire[]>;

@Injectable({ providedIn: 'root' })
export class CarteBancaireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/carte-bancaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(carteBancaire: ICarteBancaire): Observable<EntityResponseType> {
    return this.http.post<ICarteBancaire>(this.resourceUrl, carteBancaire, { observe: 'response' });
  }

  update(carteBancaire: ICarteBancaire): Observable<EntityResponseType> {
    return this.http.put<ICarteBancaire>(`${this.resourceUrl}/${getCarteBancaireIdentifier(carteBancaire) as number}`, carteBancaire, {
      observe: 'response',
    });
  }

  partialUpdate(carteBancaire: ICarteBancaire): Observable<EntityResponseType> {
    return this.http.patch<ICarteBancaire>(`${this.resourceUrl}/${getCarteBancaireIdentifier(carteBancaire) as number}`, carteBancaire, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICarteBancaire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICarteBancaire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCarteBancaireToCollectionIfMissing(
    carteBancaireCollection: ICarteBancaire[],
    ...carteBancairesToCheck: (ICarteBancaire | null | undefined)[]
  ): ICarteBancaire[] {
    const carteBancaires: ICarteBancaire[] = carteBancairesToCheck.filter(isPresent);
    if (carteBancaires.length > 0) {
      const carteBancaireCollectionIdentifiers = carteBancaireCollection.map(
        carteBancaireItem => getCarteBancaireIdentifier(carteBancaireItem)!
      );
      const carteBancairesToAdd = carteBancaires.filter(carteBancaireItem => {
        const carteBancaireIdentifier = getCarteBancaireIdentifier(carteBancaireItem);
        if (carteBancaireIdentifier == null || carteBancaireCollectionIdentifiers.includes(carteBancaireIdentifier)) {
          return false;
        }
        carteBancaireCollectionIdentifiers.push(carteBancaireIdentifier);
        return true;
      });
      return [...carteBancairesToAdd, ...carteBancaireCollection];
    }
    return carteBancaireCollection;
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBanque, getBanqueIdentifier } from '../banque.model';

export type EntityResponseType = HttpResponse<IBanque>;
export type EntityArrayResponseType = HttpResponse<IBanque[]>;

@Injectable({ providedIn: 'root' })
export class BanqueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/banques');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(banque: IBanque): Observable<EntityResponseType> {
    return this.http.post<IBanque>(this.resourceUrl, banque, { observe: 'response' });
  }

  update(banque: IBanque): Observable<EntityResponseType> {
    return this.http.put<IBanque>(`${this.resourceUrl}/${getBanqueIdentifier(banque) as number}`, banque, { observe: 'response' });
  }

  partialUpdate(banque: IBanque): Observable<EntityResponseType> {
    return this.http.patch<IBanque>(`${this.resourceUrl}/${getBanqueIdentifier(banque) as number}`, banque, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBanque>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBanque[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBanqueToCollectionIfMissing(banqueCollection: IBanque[], ...banquesToCheck: (IBanque | null | undefined)[]): IBanque[] {
    const banques: IBanque[] = banquesToCheck.filter(isPresent);
    if (banques.length > 0) {
      const banqueCollectionIdentifiers = banqueCollection.map(banqueItem => getBanqueIdentifier(banqueItem)!);
      const banquesToAdd = banques.filter(banqueItem => {
        const banqueIdentifier = getBanqueIdentifier(banqueItem);
        if (banqueIdentifier == null || banqueCollectionIdentifiers.includes(banqueIdentifier)) {
          return false;
        }
        banqueCollectionIdentifiers.push(banqueIdentifier);
        return true;
      });
      return [...banquesToAdd, ...banqueCollection];
    }
    return banqueCollection;
  }
}

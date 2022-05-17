import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaimentFacture, getPaimentFactureIdentifier } from '../paiment-facture.model';

export type EntityResponseType = HttpResponse<IPaimentFacture>;
export type EntityArrayResponseType = HttpResponse<IPaimentFacture[]>;

@Injectable({ providedIn: 'root' })
export class PaimentFactureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/paiment-factures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paimentFacture: IPaimentFacture): Observable<EntityResponseType> {
    return this.http.post<IPaimentFacture>(this.resourceUrl, paimentFacture, { observe: 'response' });
  }

  update(paimentFacture: IPaimentFacture): Observable<EntityResponseType> {
    return this.http.put<IPaimentFacture>(`${this.resourceUrl}/${getPaimentFactureIdentifier(paimentFacture) as number}`, paimentFacture, {
      observe: 'response',
    });
  }

  partialUpdate(paimentFacture: IPaimentFacture): Observable<EntityResponseType> {
    return this.http.patch<IPaimentFacture>(
      `${this.resourceUrl}/${getPaimentFactureIdentifier(paimentFacture) as number}`,
      paimentFacture,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaimentFacture>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaimentFacture[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPaimentFactureToCollectionIfMissing(
    paimentFactureCollection: IPaimentFacture[],
    ...paimentFacturesToCheck: (IPaimentFacture | null | undefined)[]
  ): IPaimentFacture[] {
    const paimentFactures: IPaimentFacture[] = paimentFacturesToCheck.filter(isPresent);
    if (paimentFactures.length > 0) {
      const paimentFactureCollectionIdentifiers = paimentFactureCollection.map(
        paimentFactureItem => getPaimentFactureIdentifier(paimentFactureItem)!
      );
      const paimentFacturesToAdd = paimentFactures.filter(paimentFactureItem => {
        const paimentFactureIdentifier = getPaimentFactureIdentifier(paimentFactureItem);
        if (paimentFactureIdentifier == null || paimentFactureCollectionIdentifiers.includes(paimentFactureIdentifier)) {
          return false;
        }
        paimentFactureCollectionIdentifiers.push(paimentFactureIdentifier);
        return true;
      });
      return [...paimentFacturesToAdd, ...paimentFactureCollection];
    }
    return paimentFactureCollection;
  }
}

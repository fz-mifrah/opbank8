import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOperateur, getOperateurIdentifier } from '../operateur.model';

export type EntityResponseType = HttpResponse<IOperateur>;
export type EntityArrayResponseType = HttpResponse<IOperateur[]>;

@Injectable({ providedIn: 'root' })
export class OperateurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/operateurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(operateur: IOperateur): Observable<EntityResponseType> {
    return this.http.post<IOperateur>(this.resourceUrl, operateur, { observe: 'response' });
  }

  update(operateur: IOperateur): Observable<EntityResponseType> {
    return this.http.put<IOperateur>(`${this.resourceUrl}/${getOperateurIdentifier(operateur) as number}`, operateur, {
      observe: 'response',
    });
  }

  partialUpdate(operateur: IOperateur): Observable<EntityResponseType> {
    return this.http.patch<IOperateur>(`${this.resourceUrl}/${getOperateurIdentifier(operateur) as number}`, operateur, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOperateur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOperateur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOperateurToCollectionIfMissing(
    operateurCollection: IOperateur[],
    ...operateursToCheck: (IOperateur | null | undefined)[]
  ): IOperateur[] {
    const operateurs: IOperateur[] = operateursToCheck.filter(isPresent);
    if (operateurs.length > 0) {
      const operateurCollectionIdentifiers = operateurCollection.map(operateurItem => getOperateurIdentifier(operateurItem)!);
      const operateursToAdd = operateurs.filter(operateurItem => {
        const operateurIdentifier = getOperateurIdentifier(operateurItem);
        if (operateurIdentifier == null || operateurCollectionIdentifiers.includes(operateurIdentifier)) {
          return false;
        }
        operateurCollectionIdentifiers.push(operateurIdentifier);
        return true;
      });
      return [...operateursToAdd, ...operateurCollection];
    }
    return operateurCollection;
  }
}

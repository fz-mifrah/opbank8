import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompte, getCompteIdentifier } from '../compte.model';

export type EntityResponseType = HttpResponse<ICompte>;
export type EntityArrayResponseType = HttpResponse<ICompte[]>;

@Injectable({ providedIn: 'root' })
export class CompteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/comptes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(compte: ICompte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compte);
    return this.http
      .post<ICompte>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(compte: ICompte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compte);
    return this.http
      .put<ICompte>(`${this.resourceUrl}/${getCompteIdentifier(compte) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(compte: ICompte): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compte);
    return this.http
      .patch<ICompte>(`${this.resourceUrl}/${getCompteIdentifier(compte) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICompte>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICompte[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompteToCollectionIfMissing(compteCollection: ICompte[], ...comptesToCheck: (ICompte | null | undefined)[]): ICompte[] {
    const comptes: ICompte[] = comptesToCheck.filter(isPresent);
    if (comptes.length > 0) {
      const compteCollectionIdentifiers = compteCollection.map(compteItem => getCompteIdentifier(compteItem)!);
      const comptesToAdd = comptes.filter(compteItem => {
        const compteIdentifier = getCompteIdentifier(compteItem);
        if (compteIdentifier == null || compteCollectionIdentifiers.includes(compteIdentifier)) {
          return false;
        }
        compteCollectionIdentifiers.push(compteIdentifier);
        return true;
      });
      return [...comptesToAdd, ...compteCollection];
    }
    return compteCollection;
  }

  protected convertDateFromClient(compte: ICompte): ICompte {
    return Object.assign({}, compte, {
      dateOuverture: compte.dateOuverture?.isValid() ? compte.dateOuverture.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOuverture = res.body.dateOuverture ? dayjs(res.body.dateOuverture) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((compte: ICompte) => {
        compte.dateOuverture = compte.dateOuverture ? dayjs(compte.dateOuverture) : undefined;
      });
    }
    return res;
  }
}

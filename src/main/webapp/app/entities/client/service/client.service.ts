import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClient, getClientIdentifier } from '../client.model';

export type EntityResponseType = HttpResponse<IClient>;
export type EntityArrayResponseType = HttpResponse<IClient[]>;

@Injectable({ providedIn: 'root' })
export class ClientService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/clients');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(client: IClient): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(client);
    return this.http
      .post<IClient>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(client: IClient): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(client);
    return this.http
      .put<IClient>(`${this.resourceUrl}/${getClientIdentifier(client) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(client: IClient): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(client);
    return this.http
      .patch<IClient>(`${this.resourceUrl}/${getClientIdentifier(client) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IClient>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClient[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClientToCollectionIfMissing(clientCollection: IClient[], ...clientsToCheck: (IClient | null | undefined)[]): IClient[] {
    const clients: IClient[] = clientsToCheck.filter(isPresent);
    if (clients.length > 0) {
      const clientCollectionIdentifiers = clientCollection.map(clientItem => getClientIdentifier(clientItem)!);
      const clientsToAdd = clients.filter(clientItem => {
        const clientIdentifier = getClientIdentifier(clientItem);
        if (clientIdentifier == null || clientCollectionIdentifiers.includes(clientIdentifier)) {
          return false;
        }
        clientCollectionIdentifiers.push(clientIdentifier);
        return true;
      });
      return [...clientsToAdd, ...clientCollection];
    }
    return clientCollection;
  }

  protected convertDateFromClient(client: IClient): IClient {
    return Object.assign({}, client, {
      dateNaissence: client.dateNaissence?.isValid() ? client.dateNaissence.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateNaissence = res.body.dateNaissence ? dayjs(res.body.dateNaissence) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((client: IClient) => {
        client.dateNaissence = client.dateNaissence ? dayjs(client.dateNaissence) : undefined;
      });
    }
    return res;
  }
}

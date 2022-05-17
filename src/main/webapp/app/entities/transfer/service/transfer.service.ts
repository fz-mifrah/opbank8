import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITransfer, getTransferIdentifier } from '../transfer.model';

export type EntityResponseType = HttpResponse<ITransfer>;
export type EntityArrayResponseType = HttpResponse<ITransfer[]>;

@Injectable({ providedIn: 'root' })
export class TransferService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transfers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(transfer: ITransfer): Observable<EntityResponseType> {
    return this.http.post<ITransfer>(this.resourceUrl, transfer, { observe: 'response' });
  }

  update(transfer: ITransfer): Observable<EntityResponseType> {
    return this.http.put<ITransfer>(`${this.resourceUrl}/${getTransferIdentifier(transfer) as number}`, transfer, { observe: 'response' });
  }

  partialUpdate(transfer: ITransfer): Observable<EntityResponseType> {
    return this.http.patch<ITransfer>(`${this.resourceUrl}/${getTransferIdentifier(transfer) as number}`, transfer, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITransfer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITransfer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTransferToCollectionIfMissing(transferCollection: ITransfer[], ...transfersToCheck: (ITransfer | null | undefined)[]): ITransfer[] {
    const transfers: ITransfer[] = transfersToCheck.filter(isPresent);
    if (transfers.length > 0) {
      const transferCollectionIdentifiers = transferCollection.map(transferItem => getTransferIdentifier(transferItem)!);
      const transfersToAdd = transfers.filter(transferItem => {
        const transferIdentifier = getTransferIdentifier(transferItem);
        if (transferIdentifier == null || transferCollectionIdentifiers.includes(transferIdentifier)) {
          return false;
        }
        transferCollectionIdentifiers.push(transferIdentifier);
        return true;
      });
      return [...transfersToAdd, ...transferCollection];
    }
    return transferCollection;
  }
}

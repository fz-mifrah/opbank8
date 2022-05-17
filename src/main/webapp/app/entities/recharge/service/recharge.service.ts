import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRecharge, getRechargeIdentifier } from '../recharge.model';

export type EntityResponseType = HttpResponse<IRecharge>;
export type EntityArrayResponseType = HttpResponse<IRecharge[]>;

@Injectable({ providedIn: 'root' })
export class RechargeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/recharges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(recharge: IRecharge): Observable<EntityResponseType> {
    return this.http.post<IRecharge>(this.resourceUrl, recharge, { observe: 'response' });
  }

  update(recharge: IRecharge): Observable<EntityResponseType> {
    return this.http.put<IRecharge>(`${this.resourceUrl}/${getRechargeIdentifier(recharge) as number}`, recharge, { observe: 'response' });
  }

  partialUpdate(recharge: IRecharge): Observable<EntityResponseType> {
    return this.http.patch<IRecharge>(`${this.resourceUrl}/${getRechargeIdentifier(recharge) as number}`, recharge, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRecharge>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRecharge[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRechargeToCollectionIfMissing(rechargeCollection: IRecharge[], ...rechargesToCheck: (IRecharge | null | undefined)[]): IRecharge[] {
    const recharges: IRecharge[] = rechargesToCheck.filter(isPresent);
    if (recharges.length > 0) {
      const rechargeCollectionIdentifiers = rechargeCollection.map(rechargeItem => getRechargeIdentifier(rechargeItem)!);
      const rechargesToAdd = recharges.filter(rechargeItem => {
        const rechargeIdentifier = getRechargeIdentifier(rechargeItem);
        if (rechargeIdentifier == null || rechargeCollectionIdentifiers.includes(rechargeIdentifier)) {
          return false;
        }
        rechargeCollectionIdentifiers.push(rechargeIdentifier);
        return true;
      });
      return [...rechargesToAdd, ...rechargeCollection];
    }
    return rechargeCollection;
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVirement, getVirementIdentifier } from '../virement.model';

export type EntityResponseType = HttpResponse<IVirement>;
export type EntityArrayResponseType = HttpResponse<IVirement[]>;

@Injectable({ providedIn: 'root' })
export class VirementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/virements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(virement: IVirement): Observable<EntityResponseType> {
    return this.http.post<IVirement>(this.resourceUrl, virement, { observe: 'response' });
  }

  update(virement: IVirement): Observable<EntityResponseType> {
    return this.http.put<IVirement>(`${this.resourceUrl}/${getVirementIdentifier(virement) as number}`, virement, { observe: 'response' });
  }

  partialUpdate(virement: IVirement): Observable<EntityResponseType> {
    return this.http.patch<IVirement>(`${this.resourceUrl}/${getVirementIdentifier(virement) as number}`, virement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVirement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVirement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVirementToCollectionIfMissing(virementCollection: IVirement[], ...virementsToCheck: (IVirement | null | undefined)[]): IVirement[] {
    const virements: IVirement[] = virementsToCheck.filter(isPresent);
    if (virements.length > 0) {
      const virementCollectionIdentifiers = virementCollection.map(virementItem => getVirementIdentifier(virementItem)!);
      const virementsToAdd = virements.filter(virementItem => {
        const virementIdentifier = getVirementIdentifier(virementItem);
        if (virementIdentifier == null || virementCollectionIdentifiers.includes(virementIdentifier)) {
          return false;
        }
        virementCollectionIdentifiers.push(virementIdentifier);
        return true;
      });
      return [...virementsToAdd, ...virementCollection];
    }
    return virementCollection;
  }
}

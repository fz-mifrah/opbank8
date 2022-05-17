import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServiceClass, getServiceClassIdentifier } from '../service-class.model';

export type EntityResponseType = HttpResponse<IServiceClass>;
export type EntityArrayResponseType = HttpResponse<IServiceClass[]>;

@Injectable({ providedIn: 'root' })
export class ServiceClassService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/service-classes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(serviceClass: IServiceClass): Observable<EntityResponseType> {
    return this.http.post<IServiceClass>(this.resourceUrl, serviceClass, { observe: 'response' });
  }

  update(serviceClass: IServiceClass): Observable<EntityResponseType> {
    return this.http.put<IServiceClass>(`${this.resourceUrl}/${getServiceClassIdentifier(serviceClass) as number}`, serviceClass, {
      observe: 'response',
    });
  }

  partialUpdate(serviceClass: IServiceClass): Observable<EntityResponseType> {
    return this.http.patch<IServiceClass>(`${this.resourceUrl}/${getServiceClassIdentifier(serviceClass) as number}`, serviceClass, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IServiceClass>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IServiceClass[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addServiceClassToCollectionIfMissing(
    serviceClassCollection: IServiceClass[],
    ...serviceClassesToCheck: (IServiceClass | null | undefined)[]
  ): IServiceClass[] {
    const serviceClasses: IServiceClass[] = serviceClassesToCheck.filter(isPresent);
    if (serviceClasses.length > 0) {
      const serviceClassCollectionIdentifiers = serviceClassCollection.map(
        serviceClassItem => getServiceClassIdentifier(serviceClassItem)!
      );
      const serviceClassesToAdd = serviceClasses.filter(serviceClassItem => {
        const serviceClassIdentifier = getServiceClassIdentifier(serviceClassItem);
        if (serviceClassIdentifier == null || serviceClassCollectionIdentifiers.includes(serviceClassIdentifier)) {
          return false;
        }
        serviceClassCollectionIdentifiers.push(serviceClassIdentifier);
        return true;
      });
      return [...serviceClassesToAdd, ...serviceClassCollection];
    }
    return serviceClassCollection;
  }
}

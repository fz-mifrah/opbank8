import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServiceClass, ServiceClass } from '../service-class.model';
import { ServiceClassService } from '../service/service-class.service';

@Injectable({ providedIn: 'root' })
export class ServiceClassRoutingResolveService implements Resolve<IServiceClass> {
  constructor(protected service: ServiceClassService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IServiceClass> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((serviceClass: HttpResponse<ServiceClass>) => {
          if (serviceClass.body) {
            return of(serviceClass.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ServiceClass());
  }
}

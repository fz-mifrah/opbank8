import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRecharge, Recharge } from '../recharge.model';
import { RechargeService } from '../service/recharge.service';

@Injectable({ providedIn: 'root' })
export class RechargeRoutingResolveService implements Resolve<IRecharge> {
  constructor(protected service: RechargeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRecharge> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((recharge: HttpResponse<Recharge>) => {
          if (recharge.body) {
            return of(recharge.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Recharge());
  }
}

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVirement, Virement } from '../virement.model';
import { VirementService } from '../service/virement.service';

@Injectable({ providedIn: 'root' })
export class VirementRoutingResolveService implements Resolve<IVirement> {
  constructor(protected service: VirementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVirement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((virement: HttpResponse<Virement>) => {
          if (virement.body) {
            return of(virement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Virement());
  }
}

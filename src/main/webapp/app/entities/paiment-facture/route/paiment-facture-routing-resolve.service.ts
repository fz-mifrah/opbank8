import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaimentFacture, PaimentFacture } from '../paiment-facture.model';
import { PaimentFactureService } from '../service/paiment-facture.service';

@Injectable({ providedIn: 'root' })
export class PaimentFactureRoutingResolveService implements Resolve<IPaimentFacture> {
  constructor(protected service: PaimentFactureService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaimentFacture> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paimentFacture: HttpResponse<PaimentFacture>) => {
          if (paimentFacture.body) {
            return of(paimentFacture.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaimentFacture());
  }
}

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompte, Compte } from '../compte.model';
import { CompteService } from '../service/compte.service';

@Injectable({ providedIn: 'root' })
export class CompteRoutingResolveService implements Resolve<ICompte> {
  constructor(protected service: CompteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompte> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((compte: HttpResponse<Compte>) => {
          if (compte.body) {
            return of(compte.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Compte());
  }
}

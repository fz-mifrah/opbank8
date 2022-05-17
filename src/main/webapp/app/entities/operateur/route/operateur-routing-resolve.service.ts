import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOperateur, Operateur } from '../operateur.model';
import { OperateurService } from '../service/operateur.service';

@Injectable({ providedIn: 'root' })
export class OperateurRoutingResolveService implements Resolve<IOperateur> {
  constructor(protected service: OperateurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOperateur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((operateur: HttpResponse<Operateur>) => {
          if (operateur.body) {
            return of(operateur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Operateur());
  }
}

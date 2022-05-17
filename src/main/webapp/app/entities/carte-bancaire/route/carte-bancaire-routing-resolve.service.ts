import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICarteBancaire, CarteBancaire } from '../carte-bancaire.model';
import { CarteBancaireService } from '../service/carte-bancaire.service';

@Injectable({ providedIn: 'root' })
export class CarteBancaireRoutingResolveService implements Resolve<ICarteBancaire> {
  constructor(protected service: CarteBancaireService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICarteBancaire> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((carteBancaire: HttpResponse<CarteBancaire>) => {
          if (carteBancaire.body) {
            return of(carteBancaire.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CarteBancaire());
  }
}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CarteBancaireComponent } from '../list/carte-bancaire.component';
import { CarteBancaireDetailComponent } from '../detail/carte-bancaire-detail.component';
import { CarteBancaireUpdateComponent } from '../update/carte-bancaire-update.component';
import { CarteBancaireRoutingResolveService } from './carte-bancaire-routing-resolve.service';

const carteBancaireRoute: Routes = [
  {
    path: '',
    component: CarteBancaireComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CarteBancaireDetailComponent,
    resolve: {
      carteBancaire: CarteBancaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CarteBancaireUpdateComponent,
    resolve: {
      carteBancaire: CarteBancaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CarteBancaireUpdateComponent,
    resolve: {
      carteBancaire: CarteBancaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(carteBancaireRoute)],
  exports: [RouterModule],
})
export class CarteBancaireRoutingModule {}

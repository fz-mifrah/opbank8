import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaimentFactureComponent } from '../list/paiment-facture.component';
import { PaimentFactureDetailComponent } from '../detail/paiment-facture-detail.component';
import { PaimentFactureUpdateComponent } from '../update/paiment-facture-update.component';
import { PaimentFactureRoutingResolveService } from './paiment-facture-routing-resolve.service';

const paimentFactureRoute: Routes = [
  {
    path: '',
    component: PaimentFactureComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaimentFactureDetailComponent,
    resolve: {
      paimentFacture: PaimentFactureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaimentFactureUpdateComponent,
    resolve: {
      paimentFacture: PaimentFactureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaimentFactureUpdateComponent,
    resolve: {
      paimentFacture: PaimentFactureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paimentFactureRoute)],
  exports: [RouterModule],
})
export class PaimentFactureRoutingModule {}

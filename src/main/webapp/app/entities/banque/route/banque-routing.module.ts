import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BanqueComponent } from '../list/banque.component';
import { BanqueDetailComponent } from '../detail/banque-detail.component';
import { BanqueUpdateComponent } from '../update/banque-update.component';
import { BanqueRoutingResolveService } from './banque-routing-resolve.service';

const banqueRoute: Routes = [
  {
    path: '',
    component: BanqueComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BanqueDetailComponent,
    resolve: {
      banque: BanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BanqueUpdateComponent,
    resolve: {
      banque: BanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BanqueUpdateComponent,
    resolve: {
      banque: BanqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(banqueRoute)],
  exports: [RouterModule],
})
export class BanqueRoutingModule {}

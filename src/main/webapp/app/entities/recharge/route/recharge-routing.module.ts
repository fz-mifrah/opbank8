import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RechargeComponent } from '../list/recharge.component';
import { RechargeDetailComponent } from '../detail/recharge-detail.component';
import { RechargeUpdateComponent } from '../update/recharge-update.component';
import { RechargeRoutingResolveService } from './recharge-routing-resolve.service';

const rechargeRoute: Routes = [
  {
    path: '',
    component: RechargeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RechargeDetailComponent,
    resolve: {
      recharge: RechargeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RechargeUpdateComponent,
    resolve: {
      recharge: RechargeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RechargeUpdateComponent,
    resolve: {
      recharge: RechargeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rechargeRoute)],
  exports: [RouterModule],
})
export class RechargeRoutingModule {}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VirementComponent } from '../list/virement.component';
import { VirementDetailComponent } from '../detail/virement-detail.component';
import { VirementUpdateComponent } from '../update/virement-update.component';
import { VirementRoutingResolveService } from './virement-routing-resolve.service';

const virementRoute: Routes = [
  {
    path: '',
    component: VirementComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VirementDetailComponent,
    resolve: {
      virement: VirementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VirementUpdateComponent,
    resolve: {
      virement: VirementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VirementUpdateComponent,
    resolve: {
      virement: VirementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(virementRoute)],
  exports: [RouterModule],
})
export class VirementRoutingModule {}

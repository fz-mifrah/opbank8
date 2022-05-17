import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompteComponent } from '../list/compte.component';
import { CompteDetailComponent } from '../detail/compte-detail.component';
import { CompteUpdateComponent } from '../update/compte-update.component';
import { CompteRoutingResolveService } from './compte-routing-resolve.service';

const compteRoute: Routes = [
  {
    path: '',
    component: CompteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompteDetailComponent,
    resolve: {
      compte: CompteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompteUpdateComponent,
    resolve: {
      compte: CompteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompteUpdateComponent,
    resolve: {
      compte: CompteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(compteRoute)],
  exports: [RouterModule],
})
export class CompteRoutingModule {}

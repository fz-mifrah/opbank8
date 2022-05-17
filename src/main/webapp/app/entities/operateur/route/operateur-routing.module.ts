import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OperateurComponent } from '../list/operateur.component';
import { OperateurDetailComponent } from '../detail/operateur-detail.component';
import { OperateurUpdateComponent } from '../update/operateur-update.component';
import { OperateurRoutingResolveService } from './operateur-routing-resolve.service';

const operateurRoute: Routes = [
  {
    path: '',
    component: OperateurComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OperateurDetailComponent,
    resolve: {
      operateur: OperateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OperateurUpdateComponent,
    resolve: {
      operateur: OperateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OperateurUpdateComponent,
    resolve: {
      operateur: OperateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(operateurRoute)],
  exports: [RouterModule],
})
export class OperateurRoutingModule {}

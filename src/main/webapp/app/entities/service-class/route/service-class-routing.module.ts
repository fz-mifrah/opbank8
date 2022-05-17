import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ServiceClassComponent } from '../list/service-class.component';
import { ServiceClassDetailComponent } from '../detail/service-class-detail.component';
import { ServiceClassUpdateComponent } from '../update/service-class-update.component';
import { ServiceClassRoutingResolveService } from './service-class-routing-resolve.service';

const serviceClassRoute: Routes = [
  {
    path: '',
    component: ServiceClassComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ServiceClassDetailComponent,
    resolve: {
      serviceClass: ServiceClassRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ServiceClassUpdateComponent,
    resolve: {
      serviceClass: ServiceClassRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ServiceClassUpdateComponent,
    resolve: {
      serviceClass: ServiceClassRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(serviceClassRoute)],
  exports: [RouterModule],
})
export class ServiceClassRoutingModule {}

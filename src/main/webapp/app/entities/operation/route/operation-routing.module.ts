import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OperationComponent } from '../list/operation.component';
import { OperationDetailComponent } from '../detail/operation-detail.component';
import { OperationUpdateComponent } from '../update/operation-update.component';
import { OperationHistoryComponent } from '../history/operation-history.component';
import { OperationRoutingResolveService } from './operation-routing-resolve.service';

const operationRoute: Routes = [
  {
    path: '',
    component: OperationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OperationDetailComponent,
    resolve: {
      operation: OperationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OperationUpdateComponent,
    resolve: {
      operation: OperationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'operationHistory',
    component: OperationHistoryComponent,
    data: {
          defaultSort: 'id,asc',
        },
        canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OperationUpdateComponent,
    resolve: {
      operation: OperationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(operationRoute)],
  exports: [RouterModule],
})
export class OperationRoutingModule {}

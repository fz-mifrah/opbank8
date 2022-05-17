import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TransferComponent } from '../list/transfer.component';
import { TransferDetailComponent } from '../detail/transfer-detail.component';
import { TransferUpdateComponent } from '../update/transfer-update.component';
import { TransferRoutingResolveService } from './transfer-routing-resolve.service';

const transferRoute: Routes = [
  {
    path: '',
    component: TransferComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransferDetailComponent,
    resolve: {
      transfer: TransferRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransferUpdateComponent,
    resolve: {
      transfer: TransferRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransferUpdateComponent,
    resolve: {
      transfer: TransferRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(transferRoute)],
  exports: [RouterModule],
})
export class TransferRoutingModule {}

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RechargeComponent } from './list/recharge.component';
import { RechargeDetailComponent } from './detail/recharge-detail.component';
import { RechargeUpdateComponent } from './update/recharge-update.component';
import { RechargeDeleteDialogComponent } from './delete/recharge-delete-dialog.component';
import { RechargeRoutingModule } from './route/recharge-routing.module';

@NgModule({
  imports: [SharedModule, RechargeRoutingModule],
  declarations: [RechargeComponent, RechargeDetailComponent, RechargeUpdateComponent, RechargeDeleteDialogComponent],
  entryComponents: [RechargeDeleteDialogComponent],
})
export class RechargeModule {}

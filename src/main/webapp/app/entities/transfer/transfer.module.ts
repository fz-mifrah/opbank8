import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TransferComponent } from './list/transfer.component';
import { TransferDetailComponent } from './detail/transfer-detail.component';
import { TransferUpdateComponent } from './update/transfer-update.component';
import { TransferDeleteDialogComponent } from './delete/transfer-delete-dialog.component';
import { TransferRoutingModule } from './route/transfer-routing.module';

@NgModule({
  imports: [SharedModule, TransferRoutingModule],
  declarations: [TransferComponent, TransferDetailComponent, TransferUpdateComponent, TransferDeleteDialogComponent],
  entryComponents: [TransferDeleteDialogComponent],
})
export class TransferModule {}

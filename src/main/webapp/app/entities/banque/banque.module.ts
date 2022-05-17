import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BanqueComponent } from './list/banque.component';
import { BanqueDetailComponent } from './detail/banque-detail.component';
import { BanqueUpdateComponent } from './update/banque-update.component';
import { BanqueDeleteDialogComponent } from './delete/banque-delete-dialog.component';
import { BanqueRoutingModule } from './route/banque-routing.module';

@NgModule({
  imports: [SharedModule, BanqueRoutingModule],
  declarations: [BanqueComponent, BanqueDetailComponent, BanqueUpdateComponent, BanqueDeleteDialogComponent],
  entryComponents: [BanqueDeleteDialogComponent],
})
export class BanqueModule {}

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ClientComponent } from './list/client.component';
import { ClientDetailComponent } from './detail/client-detail.component';
import { ClientUpdateComponent } from './update/client-update.component';
import { ClientDeleteDialogComponent } from './delete/client-delete-dialog.component';
import { ClientRoutingModule } from './route/client-routing.module';

@NgModule({
  imports: [SharedModule, ClientRoutingModule],
  declarations: [ClientComponent, ClientDetailComponent, ClientUpdateComponent, ClientDeleteDialogComponent],
  entryComponents: [ClientDeleteDialogComponent],
})
export class ClientModule {}

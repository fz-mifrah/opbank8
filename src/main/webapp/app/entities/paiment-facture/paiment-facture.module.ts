import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaimentFactureComponent } from './list/paiment-facture.component';
import { PaimentFactureDetailComponent } from './detail/paiment-facture-detail.component';
import { PaimentFactureUpdateComponent } from './update/paiment-facture-update.component';
import { PaimentFactureDeleteDialogComponent } from './delete/paiment-facture-delete-dialog.component';
import { PaimentFactureRoutingModule } from './route/paiment-facture-routing.module';

@NgModule({
  imports: [SharedModule, PaimentFactureRoutingModule],
  declarations: [
    PaimentFactureComponent,
    PaimentFactureDetailComponent,
    PaimentFactureUpdateComponent,
    PaimentFactureDeleteDialogComponent,
  ],
  entryComponents: [PaimentFactureDeleteDialogComponent],
})
export class PaimentFactureModule {}

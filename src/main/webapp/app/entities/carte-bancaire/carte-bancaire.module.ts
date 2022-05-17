import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CarteBancaireComponent } from './list/carte-bancaire.component';
import { CarteBancaireDetailComponent } from './detail/carte-bancaire-detail.component';
import { CarteBancaireUpdateComponent } from './update/carte-bancaire-update.component';
import { CarteBancaireDeleteDialogComponent } from './delete/carte-bancaire-delete-dialog.component';
import { CarteBancaireRoutingModule } from './route/carte-bancaire-routing.module';

@NgModule({
  imports: [SharedModule, CarteBancaireRoutingModule],
  declarations: [CarteBancaireComponent, CarteBancaireDetailComponent, CarteBancaireUpdateComponent, CarteBancaireDeleteDialogComponent],
  entryComponents: [CarteBancaireDeleteDialogComponent],
})
export class CarteBancaireModule {}

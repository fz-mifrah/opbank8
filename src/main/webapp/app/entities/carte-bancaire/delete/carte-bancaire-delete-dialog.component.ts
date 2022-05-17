import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICarteBancaire } from '../carte-bancaire.model';
import { CarteBancaireService } from '../service/carte-bancaire.service';

@Component({
  templateUrl: './carte-bancaire-delete-dialog.component.html',
})
export class CarteBancaireDeleteDialogComponent {
  carteBancaire?: ICarteBancaire;

  constructor(protected carteBancaireService: CarteBancaireService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.carteBancaireService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

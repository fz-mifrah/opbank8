import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaimentFacture } from '../paiment-facture.model';
import { PaimentFactureService } from '../service/paiment-facture.service';

@Component({
  templateUrl: './paiment-facture-delete-dialog.component.html',
})
export class PaimentFactureDeleteDialogComponent {
  paimentFacture?: IPaimentFacture;

  constructor(protected paimentFactureService: PaimentFactureService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paimentFactureService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

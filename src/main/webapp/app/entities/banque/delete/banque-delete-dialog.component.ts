import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBanque } from '../banque.model';
import { BanqueService } from '../service/banque.service';

@Component({
  templateUrl: './banque-delete-dialog.component.html',
})
export class BanqueDeleteDialogComponent {
  banque?: IBanque;

  constructor(protected banqueService: BanqueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.banqueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompte } from '../compte.model';
import { CompteService } from '../service/compte.service';

@Component({
  templateUrl: './compte-delete-dialog.component.html',
})
export class CompteDeleteDialogComponent {
  compte?: ICompte;

  constructor(protected compteService: CompteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.compteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

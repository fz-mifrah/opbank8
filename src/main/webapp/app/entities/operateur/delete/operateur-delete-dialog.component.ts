import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOperateur } from '../operateur.model';
import { OperateurService } from '../service/operateur.service';

@Component({
  templateUrl: './operateur-delete-dialog.component.html',
})
export class OperateurDeleteDialogComponent {
  operateur?: IOperateur;

  constructor(protected operateurService: OperateurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.operateurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

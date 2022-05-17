import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVirement } from '../virement.model';
import { VirementService } from '../service/virement.service';

@Component({
  templateUrl: './virement-delete-dialog.component.html',
})
export class VirementDeleteDialogComponent {
  virement?: IVirement;

  constructor(protected virementService: VirementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.virementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

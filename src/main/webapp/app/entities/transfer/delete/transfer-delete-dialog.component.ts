import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITransfer } from '../transfer.model';
import { TransferService } from '../service/transfer.service';

@Component({
  templateUrl: './transfer-delete-dialog.component.html',
})
export class TransferDeleteDialogComponent {
  transfer?: ITransfer;

  constructor(protected transferService: TransferService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transferService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

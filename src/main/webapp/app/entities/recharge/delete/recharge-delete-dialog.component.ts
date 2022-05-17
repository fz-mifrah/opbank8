import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecharge } from '../recharge.model';
import { RechargeService } from '../service/recharge.service';

@Component({
  templateUrl: './recharge-delete-dialog.component.html',
})
export class RechargeDeleteDialogComponent {
  recharge?: IRecharge;

  constructor(protected rechargeService: RechargeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rechargeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

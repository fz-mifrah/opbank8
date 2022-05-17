import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClient } from '../client.model';
import { ClientService } from '../service/client.service';

@Component({
  templateUrl: './client-delete-dialog.component.html',
})
export class ClientDeleteDialogComponent {
  client?: IClient;

  constructor(protected clientService: ClientService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.clientService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

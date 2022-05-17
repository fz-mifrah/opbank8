import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IServiceClass } from '../service-class.model';
import { ServiceClassService } from '../service/service-class.service';

@Component({
  templateUrl: './service-class-delete-dialog.component.html',
})
export class ServiceClassDeleteDialogComponent {
  serviceClass?: IServiceClass;

  constructor(protected serviceClassService: ServiceClassService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceClassService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOperateur } from '../operateur.model';
import { OperateurService } from '../service/operateur.service';
import { OperateurDeleteDialogComponent } from '../delete/operateur-delete-dialog.component';

@Component({
  selector: 'jhi-operateur',
  templateUrl: './operateur.component.html',
})
export class OperateurComponent implements OnInit {
  operateurs?: IOperateur[];
  isLoading = false;

  constructor(protected operateurService: OperateurService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.operateurService.query().subscribe({
      next: (res: HttpResponse<IOperateur[]>) => {
        this.isLoading = false;
        this.operateurs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IOperateur): number {
    return item.id!;
  }

  delete(operateur: IOperateur): void {
    const modalRef = this.modalService.open(OperateurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.operateur = operateur;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBanque } from '../banque.model';
import { BanqueService } from '../service/banque.service';
import { BanqueDeleteDialogComponent } from '../delete/banque-delete-dialog.component';

@Component({
  selector: 'jhi-banque',
  templateUrl: './banque.component.html',
})
export class BanqueComponent implements OnInit {
  banques?: IBanque[];
  isLoading = false;

  constructor(protected banqueService: BanqueService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.banqueService.query().subscribe({
      next: (res: HttpResponse<IBanque[]>) => {
        this.isLoading = false;
        this.banques = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IBanque): number {
    return item.id!;
  }

  delete(banque: IBanque): void {
    const modalRef = this.modalService.open(BanqueDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.banque = banque;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

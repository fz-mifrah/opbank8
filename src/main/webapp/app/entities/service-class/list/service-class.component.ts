import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IServiceClass } from '../service-class.model';
import { ServiceClassService } from '../service/service-class.service';
import { ServiceClassDeleteDialogComponent } from '../delete/service-class-delete-dialog.component';

@Component({
  selector: 'jhi-service-class',
  templateUrl: './service-class.component.html',
})
export class ServiceClassComponent implements OnInit {
  serviceClasses?: IServiceClass[];
  isLoading = false;

  constructor(protected serviceClassService: ServiceClassService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.serviceClassService.query().subscribe({
      next: (res: HttpResponse<IServiceClass[]>) => {
        this.isLoading = false;
        this.serviceClasses = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IServiceClass): number {
    return item.id!;
  }

  delete(serviceClass: IServiceClass): void {
    const modalRef = this.modalService.open(ServiceClassDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.serviceClass = serviceClass;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

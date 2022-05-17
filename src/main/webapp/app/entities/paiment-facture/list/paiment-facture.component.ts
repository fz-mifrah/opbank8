import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaimentFacture } from '../paiment-facture.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { PaimentFactureService } from '../service/paiment-facture.service';
import { PaimentFactureDeleteDialogComponent } from '../delete/paiment-facture-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-paiment-facture',
  templateUrl: './paiment-facture.component.html',
})
export class PaimentFactureComponent implements OnInit {
  paimentFactures: IPaimentFacture[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected paimentFactureService: PaimentFactureService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.paimentFactures = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.paimentFactureService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IPaimentFacture[]>) => {
          this.isLoading = false;
          this.paginatePaimentFactures(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.paimentFactures = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPaimentFacture): number {
    return item.id!;
  }

  delete(paimentFacture: IPaimentFacture): void {
    const modalRef = this.modalService.open(PaimentFactureDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.paimentFacture = paimentFacture;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePaimentFactures(data: IPaimentFacture[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.paimentFactures.push(d);
      }
    }
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITransfer } from '../transfer.model';

@Component({
  selector: 'jhi-transfer-detail',
  templateUrl: './transfer-detail.component.html',
})
export class TransferDetailComponent implements OnInit {
  transfer: ITransfer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transfer }) => {
      this.transfer = transfer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

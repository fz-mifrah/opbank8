import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaimentFacture } from '../paiment-facture.model';

@Component({
  selector: 'jhi-paiment-facture-detail',
  templateUrl: './paiment-facture-detail.component.html',
})
export class PaimentFactureDetailComponent implements OnInit {
  paimentFacture: IPaimentFacture | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paimentFacture }) => {
      this.paimentFacture = paimentFacture;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

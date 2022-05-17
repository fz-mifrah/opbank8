import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompte } from '../compte.model';

@Component({
  selector: 'jhi-compte-detail',
  templateUrl: './compte-detail.component.html',
})
export class CompteDetailComponent implements OnInit {
  compte: ICompte | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compte }) => {
      this.compte = compte;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

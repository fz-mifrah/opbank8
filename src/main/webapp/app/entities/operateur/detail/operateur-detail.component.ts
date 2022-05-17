import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOperateur } from '../operateur.model';

@Component({
  selector: 'jhi-operateur-detail',
  templateUrl: './operateur-detail.component.html',
})
export class OperateurDetailComponent implements OnInit {
  operateur: IOperateur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operateur }) => {
      this.operateur = operateur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

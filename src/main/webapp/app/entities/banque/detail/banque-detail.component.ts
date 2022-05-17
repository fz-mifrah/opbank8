import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBanque } from '../banque.model';

@Component({
  selector: 'jhi-banque-detail',
  templateUrl: './banque-detail.component.html',
})
export class BanqueDetailComponent implements OnInit {
  banque: IBanque | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ banque }) => {
      this.banque = banque;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

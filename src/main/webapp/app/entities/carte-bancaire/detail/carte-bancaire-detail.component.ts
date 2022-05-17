import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICarteBancaire } from '../carte-bancaire.model';

@Component({
  selector: 'jhi-carte-bancaire-detail',
  templateUrl: './carte-bancaire-detail.component.html',
})
export class CarteBancaireDetailComponent implements OnInit {
  carteBancaire: ICarteBancaire | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carteBancaire }) => {
      this.carteBancaire = carteBancaire;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

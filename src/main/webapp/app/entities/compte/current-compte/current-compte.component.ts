import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompte } from '../compte.model';

@Component({
  selector: 'jhi-current-compte',
  templateUrl: './current-compte.component.html',
})
export class CurrentCompteComponent implements OnInit {
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

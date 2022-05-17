import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRecharge } from '../recharge.model';

@Component({
  selector: 'jhi-recharge-detail',
  templateUrl: './recharge-detail.component.html',
})
export class RechargeDetailComponent implements OnInit {
  recharge: IRecharge | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recharge }) => {
      this.recharge = recharge;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

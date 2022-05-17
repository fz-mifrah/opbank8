import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClient } from '../client.model';

@Component({
  selector: 'jhi-client-detail',
  templateUrl: './client-detail.component.html',
})
export class ClientDetailComponent implements OnInit {
  client: IClient | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ client }) => {
      this.client = client;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

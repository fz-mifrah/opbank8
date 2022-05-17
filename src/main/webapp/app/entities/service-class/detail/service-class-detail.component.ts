import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IServiceClass } from '../service-class.model';

@Component({
  selector: 'jhi-service-class-detail',
  templateUrl: './service-class-detail.component.html',
})
export class ServiceClassDetailComponent implements OnInit {
  serviceClass: IServiceClass | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceClass }) => {
      this.serviceClass = serviceClass;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

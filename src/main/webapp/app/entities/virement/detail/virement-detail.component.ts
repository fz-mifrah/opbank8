import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVirement } from '../virement.model';

@Component({
  selector: 'jhi-virement-detail',
  templateUrl: './virement-detail.component.html',
})
export class VirementDetailComponent implements OnInit {
  virement: IVirement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ virement }) => {
      this.virement = virement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

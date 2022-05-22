import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { IVirement, Virement, IOrderVirement , OrderVirement } from '../virement.model';
import { VirementService } from '../service/virement.service';

@Component({
  selector: 'jhi-effectuer-virement',
  templateUrl: './effectuer-virement.component.html',
})
export class EffectuerVirementComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    monCompte: ['21780446122851'],
    compteDestinataire: [],
    montant: [],
    label:  [],

  });

  constructor(
    protected virementService: VirementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
return;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const virement = this.createFromForm();
     alert(virement);
      this.subscribeToSaveResponse(this.virementService.effectuerVirement(virement));
  }


  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVirement>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }



  protected createFromForm(): IOrderVirement {
    return {
      ...new OrderVirement(),
      monCompte: this.editForm.get(['monCompte'])!.value,
      compteDestinataire: this.editForm.get(['compteDestinataire'])!.value,
      montant: this.editForm.get(['montant'])!.value,
      label: this.editForm.get(['label'])!.value
    };

  }
}

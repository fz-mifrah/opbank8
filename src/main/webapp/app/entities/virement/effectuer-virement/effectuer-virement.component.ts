import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { finalize, map } from 'rxjs/operators';
import { IVirement, Virement, IOrderVirement , OrderVirement } from '../virement.model';
import { VirementService } from '../service/virement.service';
import { CompteService } from '../../compte/service/compte.service';
import { ICompte , Compte } from '../../compte/compte.model';

import { Observable, of, EMPTY } from 'rxjs';

@Component({
  selector: 'jhi-effectuer-virement',
  templateUrl: './effectuer-virement.component.html',
})
export class EffectuerVirementComponent implements OnInit {
  isSaving = false;

  currentCompte: ICompte | null = null;

  editForm = this.fb.group({
    monCompte: [],
    compteDestinataire: [],
    montant: [],
    label:  [],
  });


  constructor(
    protected virementService: VirementService,
    protected compteService: CompteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {

 this.compteService.currentCompte().subscribe( compte => {this.editForm.patchValue({monCompte: compte.body?.rib});});

return;  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    if (confirm("voulez vous vraiment confirmer votre operation ?")) {
    this.isSaving = true;
    const virement = this.createFromForm();
      this.subscribeToSaveResponse(this.virementService.effectuerVirement(virement));
      }
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
    }

   }
  }


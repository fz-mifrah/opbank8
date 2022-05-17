import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVirement, Virement } from '../virement.model';
import { VirementService } from '../service/virement.service';
import { ICompte } from 'app/entities/compte/compte.model';
import { CompteService } from 'app/entities/compte/service/compte.service';

@Component({
  selector: 'jhi-virement-update',
  templateUrl: './virement-update.component.html',
})
export class VirementUpdateComponent implements OnInit {
  isSaving = false;

  comptesSharedCollection: ICompte[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    compte: [],
  });

  constructor(
    protected virementService: VirementService,
    protected compteService: CompteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ virement }) => {
      this.updateForm(virement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const virement = this.createFromForm();
    if (virement.id !== undefined) {
      this.subscribeToSaveResponse(this.virementService.update(virement));
    } else {
      this.subscribeToSaveResponse(this.virementService.create(virement));
    }
  }

  trackCompteById(_index: number, item: ICompte): number {
    return item.id!;
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

  protected updateForm(virement: IVirement): void {
    this.editForm.patchValue({
      id: virement.id,
      description: virement.description,
      compte: virement.compte,
    });

    this.comptesSharedCollection = this.compteService.addCompteToCollectionIfMissing(this.comptesSharedCollection, virement.compte);
  }

  protected loadRelationshipsOptions(): void {
    this.compteService
      .query()
      .pipe(map((res: HttpResponse<ICompte[]>) => res.body ?? []))
      .pipe(map((comptes: ICompte[]) => this.compteService.addCompteToCollectionIfMissing(comptes, this.editForm.get('compte')!.value)))
      .subscribe((comptes: ICompte[]) => (this.comptesSharedCollection = comptes));
  }

  protected createFromForm(): IVirement {
    return {
      ...new Virement(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      compte: this.editForm.get(['compte'])!.value,
    };
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IOperateur, Operateur } from '../operateur.model';
import { OperateurService } from '../service/operateur.service';

@Component({
  selector: 'jhi-operateur-update',
  templateUrl: './operateur-update.component.html',
})
export class OperateurUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomOp: [null, [Validators.required]],
  });

  constructor(protected operateurService: OperateurService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operateur }) => {
      this.updateForm(operateur);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operateur = this.createFromForm();
    if (operateur.id !== undefined) {
      this.subscribeToSaveResponse(this.operateurService.update(operateur));
    } else {
      this.subscribeToSaveResponse(this.operateurService.create(operateur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperateur>>): void {
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

  protected updateForm(operateur: IOperateur): void {
    this.editForm.patchValue({
      id: operateur.id,
      nomOp: operateur.nomOp,
    });
  }

  protected createFromForm(): IOperateur {
    return {
      ...new Operateur(),
      id: this.editForm.get(['id'])!.value,
      nomOp: this.editForm.get(['nomOp'])!.value,
    };
  }
}

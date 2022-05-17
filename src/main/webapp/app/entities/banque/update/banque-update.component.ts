import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBanque, Banque } from '../banque.model';
import { BanqueService } from '../service/banque.service';

@Component({
  selector: 'jhi-banque-update',
  templateUrl: './banque-update.component.html',
})
export class BanqueUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    adresse: [],
    email: [null, [Validators.required]],
  });

  constructor(protected banqueService: BanqueService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ banque }) => {
      this.updateForm(banque);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const banque = this.createFromForm();
    if (banque.id !== undefined) {
      this.subscribeToSaveResponse(this.banqueService.update(banque));
    } else {
      this.subscribeToSaveResponse(this.banqueService.create(banque));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBanque>>): void {
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

  protected updateForm(banque: IBanque): void {
    this.editForm.patchValue({
      id: banque.id,
      nom: banque.nom,
      adresse: banque.adresse,
      email: banque.email,
    });
  }

  protected createFromForm(): IBanque {
    return {
      ...new Banque(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      email: this.editForm.get(['email'])!.value,
    };
  }
}

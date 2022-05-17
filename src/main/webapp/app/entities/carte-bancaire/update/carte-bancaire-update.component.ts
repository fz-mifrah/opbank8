import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICarteBancaire, CarteBancaire } from '../carte-bancaire.model';
import { CarteBancaireService } from '../service/carte-bancaire.service';

@Component({
  selector: 'jhi-carte-bancaire-update',
  templateUrl: './carte-bancaire-update.component.html',
})
export class CarteBancaireUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    numCarte: [null, [Validators.required]],
  });

  constructor(protected carteBancaireService: CarteBancaireService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carteBancaire }) => {
      this.updateForm(carteBancaire);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const carteBancaire = this.createFromForm();
    if (carteBancaire.id !== undefined) {
      this.subscribeToSaveResponse(this.carteBancaireService.update(carteBancaire));
    } else {
      this.subscribeToSaveResponse(this.carteBancaireService.create(carteBancaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICarteBancaire>>): void {
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

  protected updateForm(carteBancaire: ICarteBancaire): void {
    this.editForm.patchValue({
      id: carteBancaire.id,
      numCarte: carteBancaire.numCarte,
    });
  }

  protected createFromForm(): ICarteBancaire {
    return {
      ...new CarteBancaire(),
      id: this.editForm.get(['id'])!.value,
      numCarte: this.editForm.get(['numCarte'])!.value,
    };
  }
}

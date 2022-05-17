import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICompte, Compte } from '../compte.model';
import { CompteService } from '../service/compte.service';
import { ICarteBancaire } from 'app/entities/carte-bancaire/carte-bancaire.model';
import { CarteBancaireService } from 'app/entities/carte-bancaire/service/carte-bancaire.service';
import { IBanque } from 'app/entities/banque/banque.model';
import { BanqueService } from 'app/entities/banque/service/banque.service';

@Component({
  selector: 'jhi-compte-update',
  templateUrl: './compte-update.component.html',
})
export class CompteUpdateComponent implements OnInit {
  isSaving = false;

  carteBancairesCollection: ICarteBancaire[] = [];
  banquesSharedCollection: IBanque[] = [];

  editForm = this.fb.group({
    id: [],
    rib: [null, [Validators.required]],
    dateOuverture: [],
    code: [null, [Validators.required]],
    solde: [null, [Validators.required]],
    carteBancaire: [],
    banque: [],
  });

  constructor(
    protected compteService: CompteService,
    protected carteBancaireService: CarteBancaireService,
    protected banqueService: BanqueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compte }) => {
      this.updateForm(compte);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compte = this.createFromForm();
    if (compte.id !== undefined) {
      this.subscribeToSaveResponse(this.compteService.update(compte));
    } else {
      this.subscribeToSaveResponse(this.compteService.create(compte));
    }
  }

  trackCarteBancaireById(_index: number, item: ICarteBancaire): number {
    return item.id!;
  }

  trackBanqueById(_index: number, item: IBanque): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompte>>): void {
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

  protected updateForm(compte: ICompte): void {
    this.editForm.patchValue({
      id: compte.id,
      rib: compte.rib,
      dateOuverture: compte.dateOuverture,
      code: compte.code,
      solde: compte.solde,
      carteBancaire: compte.carteBancaire,
      banque: compte.banque,
    });

    this.carteBancairesCollection = this.carteBancaireService.addCarteBancaireToCollectionIfMissing(
      this.carteBancairesCollection,
      compte.carteBancaire
    );
    this.banquesSharedCollection = this.banqueService.addBanqueToCollectionIfMissing(this.banquesSharedCollection, compte.banque);
  }

  protected loadRelationshipsOptions(): void {
    this.carteBancaireService
      .query({ filter: 'compte-is-null' })
      .pipe(map((res: HttpResponse<ICarteBancaire[]>) => res.body ?? []))
      .pipe(
        map((carteBancaires: ICarteBancaire[]) =>
          this.carteBancaireService.addCarteBancaireToCollectionIfMissing(carteBancaires, this.editForm.get('carteBancaire')!.value)
        )
      )
      .subscribe((carteBancaires: ICarteBancaire[]) => (this.carteBancairesCollection = carteBancaires));

    this.banqueService
      .query()
      .pipe(map((res: HttpResponse<IBanque[]>) => res.body ?? []))
      .pipe(map((banques: IBanque[]) => this.banqueService.addBanqueToCollectionIfMissing(banques, this.editForm.get('banque')!.value)))
      .subscribe((banques: IBanque[]) => (this.banquesSharedCollection = banques));
  }

  protected createFromForm(): ICompte {
    return {
      ...new Compte(),
      id: this.editForm.get(['id'])!.value,
      rib: this.editForm.get(['rib'])!.value,
      dateOuverture: this.editForm.get(['dateOuverture'])!.value,
      code: this.editForm.get(['code'])!.value,
      solde: this.editForm.get(['solde'])!.value,
      carteBancaire: this.editForm.get(['carteBancaire'])!.value,
      banque: this.editForm.get(['banque'])!.value,
    };
  }
}

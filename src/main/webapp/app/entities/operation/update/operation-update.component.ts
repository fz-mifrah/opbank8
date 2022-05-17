import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOperation, Operation } from '../operation.model';
import { OperationService } from '../service/operation.service';
import { IVirement } from 'app/entities/virement/virement.model';
import { VirementService } from 'app/entities/virement/service/virement.service';
import { ITransfer } from 'app/entities/transfer/transfer.model';
import { TransferService } from 'app/entities/transfer/service/transfer.service';
import { IRecharge } from 'app/entities/recharge/recharge.model';
import { RechargeService } from 'app/entities/recharge/service/recharge.service';
import { IPaimentFacture } from 'app/entities/paiment-facture/paiment-facture.model';
import { PaimentFactureService } from 'app/entities/paiment-facture/service/paiment-facture.service';
import { ICompte } from 'app/entities/compte/compte.model';
import { CompteService } from 'app/entities/compte/service/compte.service';
import { TypeOperation } from 'app/entities/enumerations/type-operation.model';
import { EtatOperation } from 'app/entities/enumerations/etat-operation.model';

@Component({
  selector: 'jhi-operation-update',
  templateUrl: './operation-update.component.html',
})
export class OperationUpdateComponent implements OnInit {
  isSaving = false;
  typeOperationValues = Object.keys(TypeOperation);
  etatOperationValues = Object.keys(EtatOperation);

  virementsCollection: IVirement[] = [];
  transfersCollection: ITransfer[] = [];
  rechargesCollection: IRecharge[] = [];
  paimentFacturesCollection: IPaimentFacture[] = [];
  comptesSharedCollection: ICompte[] = [];

  editForm = this.fb.group({
    id: [],
    numOperation: [null, [Validators.required]],
    date: [],
    typeOperatin: [],
    etatOperation: [],
    montant: [null, [Validators.required]],
    virement: [],
    transfer: [],
    recharge: [],
    paimentFacture: [],
    compte: [],
  });

  constructor(
    protected operationService: OperationService,
    protected virementService: VirementService,
    protected transferService: TransferService,
    protected rechargeService: RechargeService,
    protected paimentFactureService: PaimentFactureService,
    protected compteService: CompteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operation }) => {
      this.updateForm(operation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operation = this.createFromForm();
    if (operation.id !== undefined) {
      this.subscribeToSaveResponse(this.operationService.update(operation));
    } else {
      this.subscribeToSaveResponse(this.operationService.create(operation));
    }
  }

  trackVirementById(_index: number, item: IVirement): number {
    return item.id!;
  }

  trackTransferById(_index: number, item: ITransfer): number {
    return item.id!;
  }

  trackRechargeById(_index: number, item: IRecharge): number {
    return item.id!;
  }

  trackPaimentFactureById(_index: number, item: IPaimentFacture): number {
    return item.id!;
  }

  trackCompteById(_index: number, item: ICompte): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperation>>): void {
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

  protected updateForm(operation: IOperation): void {
    this.editForm.patchValue({
      id: operation.id,
      numOperation: operation.numOperation,
      date: operation.date,
      typeOperatin: operation.typeOperatin,
      etatOperation: operation.etatOperation,
      montant: operation.montant,
      virement: operation.virement,
      transfer: operation.transfer,
      recharge: operation.recharge,
      paimentFacture: operation.paimentFacture,
      compte: operation.compte,
    });

    this.virementsCollection = this.virementService.addVirementToCollectionIfMissing(this.virementsCollection, operation.virement);
    this.transfersCollection = this.transferService.addTransferToCollectionIfMissing(this.transfersCollection, operation.transfer);
    this.rechargesCollection = this.rechargeService.addRechargeToCollectionIfMissing(this.rechargesCollection, operation.recharge);
    this.paimentFacturesCollection = this.paimentFactureService.addPaimentFactureToCollectionIfMissing(
      this.paimentFacturesCollection,
      operation.paimentFacture
    );
    this.comptesSharedCollection = this.compteService.addCompteToCollectionIfMissing(this.comptesSharedCollection, operation.compte);
  }

  protected loadRelationshipsOptions(): void {
    this.virementService
      .query({ filter: 'operation-is-null' })
      .pipe(map((res: HttpResponse<IVirement[]>) => res.body ?? []))
      .pipe(
        map((virements: IVirement[]) =>
          this.virementService.addVirementToCollectionIfMissing(virements, this.editForm.get('virement')!.value)
        )
      )
      .subscribe((virements: IVirement[]) => (this.virementsCollection = virements));

    this.transferService
      .query({ filter: 'operation-is-null' })
      .pipe(map((res: HttpResponse<ITransfer[]>) => res.body ?? []))
      .pipe(
        map((transfers: ITransfer[]) =>
          this.transferService.addTransferToCollectionIfMissing(transfers, this.editForm.get('transfer')!.value)
        )
      )
      .subscribe((transfers: ITransfer[]) => (this.transfersCollection = transfers));

    this.rechargeService
      .query({ filter: 'operation-is-null' })
      .pipe(map((res: HttpResponse<IRecharge[]>) => res.body ?? []))
      .pipe(
        map((recharges: IRecharge[]) =>
          this.rechargeService.addRechargeToCollectionIfMissing(recharges, this.editForm.get('recharge')!.value)
        )
      )
      .subscribe((recharges: IRecharge[]) => (this.rechargesCollection = recharges));

    this.paimentFactureService
      .query({ filter: 'operation-is-null' })
      .pipe(map((res: HttpResponse<IPaimentFacture[]>) => res.body ?? []))
      .pipe(
        map((paimentFactures: IPaimentFacture[]) =>
          this.paimentFactureService.addPaimentFactureToCollectionIfMissing(paimentFactures, this.editForm.get('paimentFacture')!.value)
        )
      )
      .subscribe((paimentFactures: IPaimentFacture[]) => (this.paimentFacturesCollection = paimentFactures));

    this.compteService
      .query()
      .pipe(map((res: HttpResponse<ICompte[]>) => res.body ?? []))
      .pipe(map((comptes: ICompte[]) => this.compteService.addCompteToCollectionIfMissing(comptes, this.editForm.get('compte')!.value)))
      .subscribe((comptes: ICompte[]) => (this.comptesSharedCollection = comptes));
  }

  protected createFromForm(): IOperation {
    return {
      ...new Operation(),
      id: this.editForm.get(['id'])!.value,
      numOperation: this.editForm.get(['numOperation'])!.value,
      date: this.editForm.get(['date'])!.value,
      typeOperatin: this.editForm.get(['typeOperatin'])!.value,
      etatOperation: this.editForm.get(['etatOperation'])!.value,
      montant: this.editForm.get(['montant'])!.value,
      virement: this.editForm.get(['virement'])!.value,
      transfer: this.editForm.get(['transfer'])!.value,
      recharge: this.editForm.get(['recharge'])!.value,
      paimentFacture: this.editForm.get(['paimentFacture'])!.value,
      compte: this.editForm.get(['compte'])!.value,
    };
  }
}

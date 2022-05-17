import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITransfer, Transfer } from '../transfer.model';
import { TransferService } from '../service/transfer.service';

@Component({
  selector: 'jhi-transfer-update',
  templateUrl: './transfer-update.component.html',
})
export class TransferUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    cin: [null, [Validators.required]],
    nomPrenom: [null, [Validators.required]],
    tel: [],
  });

  constructor(protected transferService: TransferService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transfer }) => {
      this.updateForm(transfer);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transfer = this.createFromForm();
    if (transfer.id !== undefined) {
      this.subscribeToSaveResponse(this.transferService.update(transfer));
    } else {
      this.subscribeToSaveResponse(this.transferService.create(transfer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransfer>>): void {
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

  protected updateForm(transfer: ITransfer): void {
    this.editForm.patchValue({
      id: transfer.id,
      cin: transfer.cin,
      nomPrenom: transfer.nomPrenom,
      tel: transfer.tel,
    });
  }

  protected createFromForm(): ITransfer {
    return {
      ...new Transfer(),
      id: this.editForm.get(['id'])!.value,
      cin: this.editForm.get(['cin'])!.value,
      nomPrenom: this.editForm.get(['nomPrenom'])!.value,
      tel: this.editForm.get(['tel'])!.value,
    };
  }
}

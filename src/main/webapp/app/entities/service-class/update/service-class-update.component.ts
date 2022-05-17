import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IServiceClass, ServiceClass } from '../service-class.model';
import { ServiceClassService } from '../service/service-class.service';

@Component({
  selector: 'jhi-service-class-update',
  templateUrl: './service-class-update.component.html',
})
export class ServiceClassUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomService: [null, [Validators.required]],
  });

  constructor(protected serviceClassService: ServiceClassService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceClass }) => {
      this.updateForm(serviceClass);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceClass = this.createFromForm();
    if (serviceClass.id !== undefined) {
      this.subscribeToSaveResponse(this.serviceClassService.update(serviceClass));
    } else {
      this.subscribeToSaveResponse(this.serviceClassService.create(serviceClass));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceClass>>): void {
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

  protected updateForm(serviceClass: IServiceClass): void {
    this.editForm.patchValue({
      id: serviceClass.id,
      nomService: serviceClass.nomService,
    });
  }

  protected createFromForm(): IServiceClass {
    return {
      ...new ServiceClass(),
      id: this.editForm.get(['id'])!.value,
      nomService: this.editForm.get(['nomService'])!.value,
    };
  }
}

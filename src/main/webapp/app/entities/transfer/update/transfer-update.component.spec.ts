import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TransferService } from '../service/transfer.service';
import { ITransfer, Transfer } from '../transfer.model';

import { TransferUpdateComponent } from './transfer-update.component';

describe('Transfer Management Update Component', () => {
  let comp: TransferUpdateComponent;
  let fixture: ComponentFixture<TransferUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transferService: TransferService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TransferUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TransferUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransferUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transferService = TestBed.inject(TransferService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const transfer: ITransfer = { id: 456 };

      activatedRoute.data = of({ transfer });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(transfer));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Transfer>>();
      const transfer = { id: 123 };
      jest.spyOn(transferService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transfer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transfer }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(transferService.update).toHaveBeenCalledWith(transfer);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Transfer>>();
      const transfer = new Transfer();
      jest.spyOn(transferService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transfer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transfer }));
      saveSubject.complete();

      // THEN
      expect(transferService.create).toHaveBeenCalledWith(transfer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Transfer>>();
      const transfer = { id: 123 };
      jest.spyOn(transferService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transfer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transferService.update).toHaveBeenCalledWith(transfer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

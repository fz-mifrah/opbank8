import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OperationService } from '../service/operation.service';
import { IOperation, Operation } from '../operation.model';
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

import { OperationUpdateComponent } from './operation-update.component';

describe('Operation Management Update Component', () => {
  let comp: OperationUpdateComponent;
  let fixture: ComponentFixture<OperationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let operationService: OperationService;
  let virementService: VirementService;
  let transferService: TransferService;
  let rechargeService: RechargeService;
  let paimentFactureService: PaimentFactureService;
  let compteService: CompteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OperationUpdateComponent],
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
      .overrideTemplate(OperationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    operationService = TestBed.inject(OperationService);
    virementService = TestBed.inject(VirementService);
    transferService = TestBed.inject(TransferService);
    rechargeService = TestBed.inject(RechargeService);
    paimentFactureService = TestBed.inject(PaimentFactureService);
    compteService = TestBed.inject(CompteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call virement query and add missing value', () => {
      const operation: IOperation = { id: 456 };
      const virement: IVirement = { id: 76178 };
      operation.virement = virement;

      const virementCollection: IVirement[] = [{ id: 32819 }];
      jest.spyOn(virementService, 'query').mockReturnValue(of(new HttpResponse({ body: virementCollection })));
      const expectedCollection: IVirement[] = [virement, ...virementCollection];
      jest.spyOn(virementService, 'addVirementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      expect(virementService.query).toHaveBeenCalled();
      expect(virementService.addVirementToCollectionIfMissing).toHaveBeenCalledWith(virementCollection, virement);
      expect(comp.virementsCollection).toEqual(expectedCollection);
    });

    it('Should call transfer query and add missing value', () => {
      const operation: IOperation = { id: 456 };
      const transfer: ITransfer = { id: 48065 };
      operation.transfer = transfer;

      const transferCollection: ITransfer[] = [{ id: 28217 }];
      jest.spyOn(transferService, 'query').mockReturnValue(of(new HttpResponse({ body: transferCollection })));
      const expectedCollection: ITransfer[] = [transfer, ...transferCollection];
      jest.spyOn(transferService, 'addTransferToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      expect(transferService.query).toHaveBeenCalled();
      expect(transferService.addTransferToCollectionIfMissing).toHaveBeenCalledWith(transferCollection, transfer);
      expect(comp.transfersCollection).toEqual(expectedCollection);
    });

    it('Should call recharge query and add missing value', () => {
      const operation: IOperation = { id: 456 };
      const recharge: IRecharge = { id: 30482 };
      operation.recharge = recharge;

      const rechargeCollection: IRecharge[] = [{ id: 47405 }];
      jest.spyOn(rechargeService, 'query').mockReturnValue(of(new HttpResponse({ body: rechargeCollection })));
      const expectedCollection: IRecharge[] = [recharge, ...rechargeCollection];
      jest.spyOn(rechargeService, 'addRechargeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      expect(rechargeService.query).toHaveBeenCalled();
      expect(rechargeService.addRechargeToCollectionIfMissing).toHaveBeenCalledWith(rechargeCollection, recharge);
      expect(comp.rechargesCollection).toEqual(expectedCollection);
    });

    it('Should call paimentFacture query and add missing value', () => {
      const operation: IOperation = { id: 456 };
      const paimentFacture: IPaimentFacture = { id: 45535 };
      operation.paimentFacture = paimentFacture;

      const paimentFactureCollection: IPaimentFacture[] = [{ id: 6291 }];
      jest.spyOn(paimentFactureService, 'query').mockReturnValue(of(new HttpResponse({ body: paimentFactureCollection })));
      const expectedCollection: IPaimentFacture[] = [paimentFacture, ...paimentFactureCollection];
      jest.spyOn(paimentFactureService, 'addPaimentFactureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      expect(paimentFactureService.query).toHaveBeenCalled();
      expect(paimentFactureService.addPaimentFactureToCollectionIfMissing).toHaveBeenCalledWith(paimentFactureCollection, paimentFacture);
      expect(comp.paimentFacturesCollection).toEqual(expectedCollection);
    });

    it('Should call Compte query and add missing value', () => {
      const operation: IOperation = { id: 456 };
      const compte: ICompte = { id: 89578 };
      operation.compte = compte;

      const compteCollection: ICompte[] = [{ id: 56649 }];
      jest.spyOn(compteService, 'query').mockReturnValue(of(new HttpResponse({ body: compteCollection })));
      const additionalComptes = [compte];
      const expectedCollection: ICompte[] = [...additionalComptes, ...compteCollection];
      jest.spyOn(compteService, 'addCompteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      expect(compteService.query).toHaveBeenCalled();
      expect(compteService.addCompteToCollectionIfMissing).toHaveBeenCalledWith(compteCollection, ...additionalComptes);
      expect(comp.comptesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const operation: IOperation = { id: 456 };
      const virement: IVirement = { id: 43444 };
      operation.virement = virement;
      const transfer: ITransfer = { id: 4645 };
      operation.transfer = transfer;
      const recharge: IRecharge = { id: 57369 };
      operation.recharge = recharge;
      const paimentFacture: IPaimentFacture = { id: 41763 };
      operation.paimentFacture = paimentFacture;
      const compte: ICompte = { id: 7645 };
      operation.compte = compte;

      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(operation));
      expect(comp.virementsCollection).toContain(virement);
      expect(comp.transfersCollection).toContain(transfer);
      expect(comp.rechargesCollection).toContain(recharge);
      expect(comp.paimentFacturesCollection).toContain(paimentFacture);
      expect(comp.comptesSharedCollection).toContain(compte);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Operation>>();
      const operation = { id: 123 };
      jest.spyOn(operationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operation }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(operationService.update).toHaveBeenCalledWith(operation);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Operation>>();
      const operation = new Operation();
      jest.spyOn(operationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operation }));
      saveSubject.complete();

      // THEN
      expect(operationService.create).toHaveBeenCalledWith(operation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Operation>>();
      const operation = { id: 123 };
      jest.spyOn(operationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(operationService.update).toHaveBeenCalledWith(operation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackVirementById', () => {
      it('Should return tracked Virement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVirementById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTransferById', () => {
      it('Should return tracked Transfer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTransferById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackRechargeById', () => {
      it('Should return tracked Recharge primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRechargeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPaimentFactureById', () => {
      it('Should return tracked PaimentFacture primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPaimentFactureById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCompteById', () => {
      it('Should return tracked Compte primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});

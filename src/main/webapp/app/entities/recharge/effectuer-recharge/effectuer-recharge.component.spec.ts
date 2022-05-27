import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RechargeService } from '../service/recharge.service';
import { IRecharge, Recharge } from '../recharge.model';
import { IOperateur } from 'app/entities/operateur/operateur.model';
import { OperateurService } from 'app/entities/operateur/service/operateur.service';

import { RechargeUpdateComponent } from './recharge-update.component';

describe('Recharge Management Update Component', () => {
  let comp: RechargeUpdateComponent;
  let fixture: ComponentFixture<RechargeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rechargeService: RechargeService;
  let operateurService: OperateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RechargeUpdateComponent],
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
      .overrideTemplate(RechargeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RechargeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rechargeService = TestBed.inject(RechargeService);
    operateurService = TestBed.inject(OperateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Operateur query and add missing value', () => {
      const recharge: IRecharge = { id: 456 };
      const operateur: IOperateur = { id: 28827 };
      recharge.operateur = operateur;

      const operateurCollection: IOperateur[] = [{ id: 20244 }];
      jest.spyOn(operateurService, 'query').mockReturnValue(of(new HttpResponse({ body: operateurCollection })));
      const additionalOperateurs = [operateur];
      const expectedCollection: IOperateur[] = [...additionalOperateurs, ...operateurCollection];
      jest.spyOn(operateurService, 'addOperateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recharge });
      comp.ngOnInit();

      expect(operateurService.query).toHaveBeenCalled();
      expect(operateurService.addOperateurToCollectionIfMissing).toHaveBeenCalledWith(operateurCollection, ...additionalOperateurs);
      expect(comp.operateursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const recharge: IRecharge = { id: 456 };
      const operateur: IOperateur = { id: 61044 };
      recharge.operateur = operateur;

      activatedRoute.data = of({ recharge });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(recharge));
      expect(comp.operateursSharedCollection).toContain(operateur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Recharge>>();
      const recharge = { id: 123 };
      jest.spyOn(rechargeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recharge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recharge }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(rechargeService.update).toHaveBeenCalledWith(recharge);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Recharge>>();
      const recharge = new Recharge();
      jest.spyOn(rechargeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recharge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recharge }));
      saveSubject.complete();

      // THEN
      expect(rechargeService.create).toHaveBeenCalledWith(recharge);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Recharge>>();
      const recharge = { id: 123 };
      jest.spyOn(rechargeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recharge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rechargeService.update).toHaveBeenCalledWith(recharge);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackOperateurById', () => {
      it('Should return tracked Operateur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOperateurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});

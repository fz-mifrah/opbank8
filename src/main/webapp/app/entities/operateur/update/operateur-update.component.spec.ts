import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OperateurService } from '../service/operateur.service';
import { IOperateur, Operateur } from '../operateur.model';

import { OperateurUpdateComponent } from './operateur-update.component';

describe('Operateur Management Update Component', () => {
  let comp: OperateurUpdateComponent;
  let fixture: ComponentFixture<OperateurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let operateurService: OperateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OperateurUpdateComponent],
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
      .overrideTemplate(OperateurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperateurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    operateurService = TestBed.inject(OperateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const operateur: IOperateur = { id: 456 };

      activatedRoute.data = of({ operateur });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(operateur));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Operateur>>();
      const operateur = { id: 123 };
      jest.spyOn(operateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operateur }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(operateurService.update).toHaveBeenCalledWith(operateur);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Operateur>>();
      const operateur = new Operateur();
      jest.spyOn(operateurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operateur }));
      saveSubject.complete();

      // THEN
      expect(operateurService.create).toHaveBeenCalledWith(operateur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Operateur>>();
      const operateur = { id: 123 };
      jest.spyOn(operateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(operateurService.update).toHaveBeenCalledWith(operateur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

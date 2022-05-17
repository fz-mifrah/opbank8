import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CarteBancaireService } from '../service/carte-bancaire.service';
import { ICarteBancaire, CarteBancaire } from '../carte-bancaire.model';

import { CarteBancaireUpdateComponent } from './carte-bancaire-update.component';

describe('CarteBancaire Management Update Component', () => {
  let comp: CarteBancaireUpdateComponent;
  let fixture: ComponentFixture<CarteBancaireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let carteBancaireService: CarteBancaireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CarteBancaireUpdateComponent],
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
      .overrideTemplate(CarteBancaireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CarteBancaireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    carteBancaireService = TestBed.inject(CarteBancaireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const carteBancaire: ICarteBancaire = { id: 456 };

      activatedRoute.data = of({ carteBancaire });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(carteBancaire));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CarteBancaire>>();
      const carteBancaire = { id: 123 };
      jest.spyOn(carteBancaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ carteBancaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: carteBancaire }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(carteBancaireService.update).toHaveBeenCalledWith(carteBancaire);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CarteBancaire>>();
      const carteBancaire = new CarteBancaire();
      jest.spyOn(carteBancaireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ carteBancaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: carteBancaire }));
      saveSubject.complete();

      // THEN
      expect(carteBancaireService.create).toHaveBeenCalledWith(carteBancaire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CarteBancaire>>();
      const carteBancaire = { id: 123 };
      jest.spyOn(carteBancaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ carteBancaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(carteBancaireService.update).toHaveBeenCalledWith(carteBancaire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

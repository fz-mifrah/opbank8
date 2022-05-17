import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompteService } from '../service/compte.service';
import { ICompte, Compte } from '../compte.model';
import { ICarteBancaire } from 'app/entities/carte-bancaire/carte-bancaire.model';
import { CarteBancaireService } from 'app/entities/carte-bancaire/service/carte-bancaire.service';
import { IBanque } from 'app/entities/banque/banque.model';
import { BanqueService } from 'app/entities/banque/service/banque.service';

import { CompteUpdateComponent } from './compte-update.component';

describe('Compte Management Update Component', () => {
  let comp: CompteUpdateComponent;
  let fixture: ComponentFixture<CompteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let compteService: CompteService;
  let carteBancaireService: CarteBancaireService;
  let banqueService: BanqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompteUpdateComponent],
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
      .overrideTemplate(CompteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    compteService = TestBed.inject(CompteService);
    carteBancaireService = TestBed.inject(CarteBancaireService);
    banqueService = TestBed.inject(BanqueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call carteBancaire query and add missing value', () => {
      const compte: ICompte = { id: 456 };
      const carteBancaire: ICarteBancaire = { id: 12922 };
      compte.carteBancaire = carteBancaire;

      const carteBancaireCollection: ICarteBancaire[] = [{ id: 97704 }];
      jest.spyOn(carteBancaireService, 'query').mockReturnValue(of(new HttpResponse({ body: carteBancaireCollection })));
      const expectedCollection: ICarteBancaire[] = [carteBancaire, ...carteBancaireCollection];
      jest.spyOn(carteBancaireService, 'addCarteBancaireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ compte });
      comp.ngOnInit();

      expect(carteBancaireService.query).toHaveBeenCalled();
      expect(carteBancaireService.addCarteBancaireToCollectionIfMissing).toHaveBeenCalledWith(carteBancaireCollection, carteBancaire);
      expect(comp.carteBancairesCollection).toEqual(expectedCollection);
    });

    it('Should call Banque query and add missing value', () => {
      const compte: ICompte = { id: 456 };
      const banque: IBanque = { id: 73188 };
      compte.banque = banque;

      const banqueCollection: IBanque[] = [{ id: 99241 }];
      jest.spyOn(banqueService, 'query').mockReturnValue(of(new HttpResponse({ body: banqueCollection })));
      const additionalBanques = [banque];
      const expectedCollection: IBanque[] = [...additionalBanques, ...banqueCollection];
      jest.spyOn(banqueService, 'addBanqueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ compte });
      comp.ngOnInit();

      expect(banqueService.query).toHaveBeenCalled();
      expect(banqueService.addBanqueToCollectionIfMissing).toHaveBeenCalledWith(banqueCollection, ...additionalBanques);
      expect(comp.banquesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const compte: ICompte = { id: 456 };
      const carteBancaire: ICarteBancaire = { id: 25178 };
      compte.carteBancaire = carteBancaire;
      const banque: IBanque = { id: 53696 };
      compte.banque = banque;

      activatedRoute.data = of({ compte });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(compte));
      expect(comp.carteBancairesCollection).toContain(carteBancaire);
      expect(comp.banquesSharedCollection).toContain(banque);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Compte>>();
      const compte = { id: 123 };
      jest.spyOn(compteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compte }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(compteService.update).toHaveBeenCalledWith(compte);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Compte>>();
      const compte = new Compte();
      jest.spyOn(compteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compte }));
      saveSubject.complete();

      // THEN
      expect(compteService.create).toHaveBeenCalledWith(compte);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Compte>>();
      const compte = { id: 123 };
      jest.spyOn(compteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(compteService.update).toHaveBeenCalledWith(compte);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCarteBancaireById', () => {
      it('Should return tracked CarteBancaire primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCarteBancaireById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackBanqueById', () => {
      it('Should return tracked Banque primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBanqueById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});

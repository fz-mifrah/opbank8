import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ServiceClassService } from '../service/service-class.service';
import { IServiceClass, ServiceClass } from '../service-class.model';

import { ServiceClassUpdateComponent } from './service-class-update.component';

describe('ServiceClass Management Update Component', () => {
  let comp: ServiceClassUpdateComponent;
  let fixture: ComponentFixture<ServiceClassUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let serviceClassService: ServiceClassService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ServiceClassUpdateComponent],
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
      .overrideTemplate(ServiceClassUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServiceClassUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serviceClassService = TestBed.inject(ServiceClassService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const serviceClass: IServiceClass = { id: 456 };

      activatedRoute.data = of({ serviceClass });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(serviceClass));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ServiceClass>>();
      const serviceClass = { id: 123 };
      jest.spyOn(serviceClassService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceClass });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceClass }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(serviceClassService.update).toHaveBeenCalledWith(serviceClass);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ServiceClass>>();
      const serviceClass = new ServiceClass();
      jest.spyOn(serviceClassService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceClass });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceClass }));
      saveSubject.complete();

      // THEN
      expect(serviceClassService.create).toHaveBeenCalledWith(serviceClass);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ServiceClass>>();
      const serviceClass = { id: 123 };
      jest.spyOn(serviceClassService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceClass });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serviceClassService.update).toHaveBeenCalledWith(serviceClass);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

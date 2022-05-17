import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ServiceClassDetailComponent } from './service-class-detail.component';

describe('ServiceClass Management Detail Component', () => {
  let comp: ServiceClassDetailComponent;
  let fixture: ComponentFixture<ServiceClassDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ServiceClassDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ serviceClass: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ServiceClassDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ServiceClassDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load serviceClass on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.serviceClass).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

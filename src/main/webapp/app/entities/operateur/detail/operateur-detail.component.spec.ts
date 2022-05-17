import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OperateurDetailComponent } from './operateur-detail.component';

describe('Operateur Management Detail Component', () => {
  let comp: OperateurDetailComponent;
  let fixture: ComponentFixture<OperateurDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OperateurDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ operateur: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OperateurDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OperateurDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load operateur on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.operateur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

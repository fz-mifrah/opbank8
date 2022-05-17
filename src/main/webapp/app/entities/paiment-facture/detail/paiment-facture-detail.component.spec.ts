import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaimentFactureDetailComponent } from './paiment-facture-detail.component';

describe('PaimentFacture Management Detail Component', () => {
  let comp: PaimentFactureDetailComponent;
  let fixture: ComponentFixture<PaimentFactureDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaimentFactureDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ paimentFacture: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PaimentFactureDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PaimentFactureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load paimentFacture on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.paimentFacture).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

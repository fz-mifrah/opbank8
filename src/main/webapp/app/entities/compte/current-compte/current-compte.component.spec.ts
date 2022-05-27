import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompteDetailComponent } from './compte-detail.component';

describe('Compte Management Detail Component', () => {
  let comp: CompteDetailComponent;
  let fixture: ComponentFixture<CompteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ compte: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CompteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CompteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load compte on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.compte).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

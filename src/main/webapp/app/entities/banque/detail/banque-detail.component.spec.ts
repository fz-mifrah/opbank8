import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BanqueDetailComponent } from './banque-detail.component';

describe('Banque Management Detail Component', () => {
  let comp: BanqueDetailComponent;
  let fixture: ComponentFixture<BanqueDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BanqueDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ banque: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BanqueDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BanqueDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load banque on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.banque).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

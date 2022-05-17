import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CarteBancaireDetailComponent } from './carte-bancaire-detail.component';

describe('CarteBancaire Management Detail Component', () => {
  let comp: CarteBancaireDetailComponent;
  let fixture: ComponentFixture<CarteBancaireDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CarteBancaireDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ carteBancaire: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CarteBancaireDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CarteBancaireDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load carteBancaire on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.carteBancaire).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

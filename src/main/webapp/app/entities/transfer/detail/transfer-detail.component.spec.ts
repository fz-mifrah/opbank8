import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TransferDetailComponent } from './transfer-detail.component';

describe('Transfer Management Detail Component', () => {
  let comp: TransferDetailComponent;
  let fixture: ComponentFixture<TransferDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TransferDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ transfer: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TransferDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TransferDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load transfer on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.transfer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

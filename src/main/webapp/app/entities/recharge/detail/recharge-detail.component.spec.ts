import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RechargeDetailComponent } from './recharge-detail.component';

describe('Recharge Management Detail Component', () => {
  let comp: RechargeDetailComponent;
  let fixture: ComponentFixture<RechargeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RechargeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ recharge: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RechargeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RechargeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load recharge on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.recharge).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

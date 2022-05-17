import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VirementDetailComponent } from './virement-detail.component';

describe('Virement Management Detail Component', () => {
  let comp: VirementDetailComponent;
  let fixture: ComponentFixture<VirementDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VirementDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ virement: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VirementDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VirementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load virement on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.virement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

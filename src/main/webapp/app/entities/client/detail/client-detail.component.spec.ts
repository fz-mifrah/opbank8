import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClientDetailComponent } from './client-detail.component';

describe('Client Management Detail Component', () => {
  let comp: ClientDetailComponent;
  let fixture: ComponentFixture<ClientDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClientDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ client: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ClientDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ClientDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load client on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.client).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

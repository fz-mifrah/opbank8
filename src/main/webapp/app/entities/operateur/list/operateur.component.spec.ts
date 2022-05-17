import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OperateurService } from '../service/operateur.service';

import { OperateurComponent } from './operateur.component';

describe('Operateur Management Component', () => {
  let comp: OperateurComponent;
  let fixture: ComponentFixture<OperateurComponent>;
  let service: OperateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OperateurComponent],
    })
      .overrideTemplate(OperateurComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperateurComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OperateurService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.operateurs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});

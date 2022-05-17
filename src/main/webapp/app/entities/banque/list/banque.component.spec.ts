import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BanqueService } from '../service/banque.service';

import { BanqueComponent } from './banque.component';

describe('Banque Management Component', () => {
  let comp: BanqueComponent;
  let fixture: ComponentFixture<BanqueComponent>;
  let service: BanqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BanqueComponent],
    })
      .overrideTemplate(BanqueComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BanqueComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BanqueService);

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
    expect(comp.banques?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});

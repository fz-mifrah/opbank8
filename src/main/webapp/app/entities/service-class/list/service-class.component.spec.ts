import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ServiceClassService } from '../service/service-class.service';

import { ServiceClassComponent } from './service-class.component';

describe('ServiceClass Management Component', () => {
  let comp: ServiceClassComponent;
  let fixture: ComponentFixture<ServiceClassComponent>;
  let service: ServiceClassService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ServiceClassComponent],
    })
      .overrideTemplate(ServiceClassComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServiceClassComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ServiceClassService);

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
    expect(comp.serviceClasses?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});

import { TestBed } from '@angular/core/testing';

import { StockpricesService } from './stockprices.service';

describe('StockpricesService', () => {
  let service: StockpricesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StockpricesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

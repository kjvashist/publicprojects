import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StockpricesComponent } from './stockprices.component';

describe('StockpricesComponent', () => {
  let component: StockpricesComponent;
  let fixture: ComponentFixture<StockpricesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StockpricesComponent]
    });
    fixture = TestBed.createComponent(StockpricesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

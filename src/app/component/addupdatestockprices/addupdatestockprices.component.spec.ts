import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddUpdateStockPricesComponent } from './addupdatestockprices.component';

describe('AddUpdateStockPricesComponent', () => {
  let component: AddUpdateStockPricesComponent;
  let fixture: ComponentFixture<AddUpdateStockPricesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddUpdateStockPricesComponent]
    });
    fixture = TestBed.createComponent(AddUpdateStockPricesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { StockpricesComponent } from './component/stockprices/stockprices.component';
import { AddUpdateStockPricesComponent } from './component/addupdatestockprices/addupdatestockprices.component';

@NgModule({
  declarations: [
    AppComponent,
    StockpricesComponent,
    AddUpdateStockPricesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

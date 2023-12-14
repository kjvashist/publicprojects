import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StockpricesComponent } from './component/stockprices/stockprices.component';
import { AddUpdateStockPricesComponent } from './component/addupdatestockprices/addupdatestockprices.component';

const routes: Routes = [
{
  path:'ui',
  component: StockpricesComponent
},
{
  path:'ui/stockprices',
  component: StockpricesComponent
},
{
  path:'ui/stockprices/edit',
  component: AddUpdateStockPricesComponent
}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

import { Component, OnInit } from '@angular/core';
import { StockpricesService } from './services/stockprices.service';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'stockangular';
  constructor(private stockPricesService: StockpricesService, private router: Router){}
 
  currTicker: string = 'AAPL';

  tickers: string[] = [];
  ngOnInit(): void {
    this.stockPricesService.getTickers().subscribe(
      {
        next:(tickersArr) => {this.tickers = tickersArr;},
        error:(response) => {console.log(response);}
      }
      );
  }
 
  onSelectionChange(event: Event): void {
    const target = event.target as HTMLInputElement;
    const selectedValue = target.value;
    //console.log('Selected ticker:', selectedValue)
    //alert('selectedValue='+selectedValue);
    this.currTicker=selectedValue;
    this.stockPricesService.handleChangeTicker(this.currTicker);
  }

  public getDisableCombbo():Boolean {
    return this.stockPricesService.getDisableCombbo();
}


}



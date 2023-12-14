import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { StockPrices } from 'src/app/models/stockprices.model';
import { FormControl } from '@angular/forms';
import { StockpricesService } from 'src/app/services/stockprices.service';
import { AppComponent } from 'src/app/app.component';
import { Observable, Subscription } from 'rxjs';

@Component({
  selector: 'app-stockprices',
  templateUrl: './stockprices.component.html',
  styleUrls: ['./stockprices.component.css']
})


 export class StockpricesComponent implements OnInit,  OnDestroy  {
  constructor(private stockPricesService: StockpricesService, private appComponent:AppComponent, private router: Router){
  }
  public refreshSubscription: Subscription = new Subscription;

  ngOnDestroy(): void {
      // Always unsubscribe (or use takeUntil, etc.) 
      if (this.refreshSubscription) {
        this.refreshSubscription.unsubscribe();
      }
    }
  
  //tickers: string[] = [];

  ngOnInit(): void {
    // this.stockPricesService.getTickers().subscribe(
    //   {
    //     next:(tickersArr) => {this.tickers = tickersArr;},
    //     error:(response) => {console.log(response);}
    //   }
    //   );

      // do whatever operations you need.
      this.refreshSubscription = this.stockPricesService.needToRefresh.subscribe(() => this.refreshStockPrices());
      this.refreshStockPrices();
  }
  refreshStockPrices():void {
    this.stockPricesService.getTickerPrices(this.appComponent.currTicker).subscribe(
      {
        next:(stockPricesArr) => {this.stockPrices = stockPricesArr;},
        error:(response) => {console.log(response);}
      }
    );
    //this.stockPricesService.resetChangeTicker();
  }

  deleteStockPricesRecordForTickkerAndDate(ticker:string,priceDate:string){
    this.stockPricesService.deleteStockPricesForTickerAndDate(ticker,priceDate).subscribe(
      {
        next: (Response) => {
          this.stockPricesService.handleChangeTicker(ticker);
          let currentUrl = this.router.url;
          this.router.navigateByUrl("/", {skipLocationChange: true})
          .then(() => {
            this.router.navigate([currentUrl]);
          });
        }
      }
    );
    
  }

  stockPrices: StockPrices[] = [];
  // {
  //   ticker:'AAPL',
  //   priceDate:'2023-11-21',
  //   open:191.41,
  //   high:191.50,
  //   low:189.74,
  //   close:190.64,
  //   adjClose:190.64,
  //   volume:36857230
  // },
  // {
  //   ticker:'MSFT',
  //   priceDate:'2023-11-21',
  //   open:191.41,
  //   high:191.50,
  //   low:189.74,
  //   close:190.64,
  //   adjClose:190.64,
  //   volume:36857230
  // }
  // ,
  // {
  //   ticker:'IBM',
  //   priceDate:'2023-11-20',
  //   open:192,
  //   high:190,
  //   low:189,
  //   close:190.64,
  //   adjClose:190.64,
  //   volume:36857230
  // }
  // ,
  // {
  //   ticker:'META',
  //   priceDate:'2023-01-21',
  //   open:191.41,
  //   high:191.50,
  //   low:189.74,
  //   close:190.64,
  //   adjClose:190.64,
  //   volume:36857230
  // }  
//];
}

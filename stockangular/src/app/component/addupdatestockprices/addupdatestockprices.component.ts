import { Component, OnDestroy, OnInit } from '@angular/core';
import { tick } from '@angular/core/testing';
import {NgForm} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { StockPrices } from 'src/app/models/stockprices.model';
import { StockpricesService } from 'src/app/services/stockprices.service';




@Component({
  selector: 'app-addupdatestockprices',
  templateUrl: './addupdatestockprices.component.html',
  styleUrls: ['./addupdatestockprices.component.css']
})


  
export class AddUpdateStockPricesComponent implements OnInit {
  constructor(private stockPricesService: StockpricesService, private router: Router, private activeRout: ActivatedRoute){  }



  ngOnInit(): void {
        this.activeRout.queryParams.subscribe(params =>{
            const ticker = params['ticker'];
            const priceDate = params['priceDate'];
            //alert("ticker="+ticker + "priceDate=" + priceDate)
            if(ticker && priceDate)
            {
                this.stockPricesService.getStockPricesForTickerDate(String(ticker),String(priceDate)).subscribe({ 
                  next:(response) => {
                    this.stockPricesForTickerAndDate=response;
                  }
              });
            }
        });
      }
        
  
      stockPricesForTickerAndDate:StockPrices = {
      ticker: '',
      priceDate: '',
      open: 0,
      high: 0,
      low: 0,
      close: 0,
      adjClose: 0,
      volume: 0
    };

    updateStockPricesForTickerAndDate(){
    //alert('this.stockPricesForTickerAndDate.priceDate='+this.stockPricesForTickerAndDate.priceDate);
      this.stockPricesService.updateStockPricesForTickerAndDate(this.stockPricesForTickerAndDate).subscribe({next:(stockPricesForTickerAndDate) =>{
        this.router.navigate(['ui/stockprices']);
      },
      error: (response) => {
        console.log(response)
      }
    });
  }
  onBackButtonClickHandler(){
    //alert('onBackButtonClickHandler');
    this.router.navigate(['ui/stockprices']);
  }

}


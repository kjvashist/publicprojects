import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { StockPrices } from '../models/stockprices.model';

@Injectable({
  providedIn: 'root'
})
export class StockpricesService {
  //baseUrl: string="https://localhost:7125";
  baseUrl: string="https://www.kjvashist.com/stock_asp_api";
  constructor(private http:HttpClient) { }

  private refreshSubject = new Subject<boolean>()
  public get needToRefresh(): Observable<boolean> { 
    return this.refreshSubject.asObservable();
  }
  
  disableCombbo:Boolean = false;

  public currTicker:string='';
  public handleChangeTicker(ticker:string):void {
    this.currTicker=ticker;
    this.refreshSubject.next(true);
  }

  public getDisableCombbo():Boolean {
    return this.disableCombbo;
  }

  getTickers(): Observable<string[]> {
    return this.http.get<string[]>(this.baseUrl+'/StockPrice/Tickers');
  }

  getTickerPrices(ticker?:string): Observable<StockPrices[]> {
    this.disableCombbo = false;
    return this.http.get<StockPrices[]>(this.baseUrl+'/StockPrice/TickerPrices?ticker='+ticker);
  }

  getStockPricesForTickerDate(ticker:string, priceDate:string): Observable<StockPrices> {
    this.disableCombbo = true;
    return this.http.get<StockPrices>(this.baseUrl+'/StockPrice/TickerPricesForDate/'+ticker+"/"+priceDate);
  }

  updateStockPricesForTickerAndDate(stockPricesToUpdate:StockPrices):Observable<StockPrices>{
    return this.http.put<StockPrices>(this.baseUrl+'/StockPrice/Update',stockPricesToUpdate);
  }

  deleteStockPricesForTickerAndDate(ticker:string, priceDate:string): Observable<StockPrices>{
    console.log(this.baseUrl+'/StockPrice/DeleteStockPrices/'+ticker+'/'+priceDate);
    //alert(this.baseUrl+'/StockPrice/DeleteStockPrices/'+ticker+'/'+priceDate);
    return this.http.delete<StockPrices>(this.baseUrl+'/StockPrice/DeleteStockPrices/'+ticker+'/'+priceDate);
  }


}


declare global {
    interface Date {
      setTimezoneOffset(offset: number): void;
    }
  }
  
  if (!Date.prototype.setTimezoneOffset) {
    Date.prototype.setTimezoneOffset = function (offset) {
      this.setUTCMinutes(this.getUTCMinutes() + offset);
    };
  }
  
export function adjustForTimeZone(date: Date): Date {
    // Get the offset of the date object to UTC in milliseconds.
    var localTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
    const utcOffset = date.getTimezoneOffset();
  
    // Calculate the offset of the local time zone to UTC in milliseconds.
    const localTimeZoneOffset = -(new Date().getTimezoneOffset());
  
    // Subtract the offset of the local time zone from the offset of the date object.
    const newOffset = utcOffset - localTimeZoneOffset;
  
    // Set the offset of the date object to the new value.
    date.setTimezoneOffset(newOffset);
  
    // Return the adjusted date object.
    return date;
  }

export interface IStockPrice{
    ticker:string;
    priceDate:string;
    open:number;
    high:number;
    low:number;
    close:number;
    adjClose:number;
    volume:number;
};

export const dummyTickersList: string[] = ["AAPL","MSFT"];

export const emptyStockPriceList: IStockPrice[] = [{
    ticker:"DummyTicker",
    priceDate:adjustForTimeZone(new Date()).toJSON().toString().substring(0,10),
    open:0.0,
    high:0.0,
    low:0.0,
    close:0.0,
    adjClose:0.0,
    volume:0.0
}];



export enum PageTypeEnum {
  list,
  add,
  edit
}




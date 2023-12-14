import { useState } from "react";
import "./AddUpdateStockPriceRecordForm.style.css"
import { IStockPrice } from "../model/StockPrice.type";

type Props = {
    stockPriceToEditIndex:number;
    stockPriceList:IStockPrice[];
    onBackButtonClickHandler :()=> void;
    onSubmitFormAddPricesHandler :(stockPriceToEditIndex:number, data:IStockPrice)=> void
    
}

const AddUpdateStockPricesForDay =(props: Props) => {
    var stockPriceData: null|IStockPrice=null;
    //alert(" AddUpdateStockPricesForDay  =" + props.stockPriceToEditIndex.toString())
    if(props.stockPriceToEditIndex>=0){
        stockPriceData = props.stockPriceList[props.stockPriceToEditIndex];
    }
    
    let disabled = false;
    if(props.stockPriceToEditIndex>=0){
        disabled=true;
    }

    const [ticker, setTicker] = useState(stockPriceData===null ? "" : stockPriceData?.ticker);
    const [priceDate, setPriceDate] = useState(stockPriceData===null ? "" : stockPriceData?.priceDate);
    const [open, setOpen] = useState(stockPriceData===null ? "" : stockPriceData?.open);
    const [high, setHigh] = useState(stockPriceData===null ? "" : stockPriceData?.high);
    const [low, setLow] = useState(stockPriceData===null ? "" : stockPriceData?.low);
    const [close, setClose] = useState(stockPriceData===null ? "" : stockPriceData?.close);
    const [adjClose, setAdjClose] = useState(stockPriceData===null ? "" : stockPriceData?.adjClose);
    const [volume, setVolume] = useState(stockPriceData===null ? "" : stockPriceData?.volume);
    
    const {onBackButtonClickHandler, onSubmitFormAddPricesHandler} = props;

    const onTickerChangeHandler =(e:any) => {
        setTicker(e.target.value);
    }

    const onPriceDateChangeHandler =(e:any) => {
        setPriceDate(e.target.value);
    }

    const onOpenChangeHandler =(e:any) => {
        setOpen(e.target.value);
    }

    const onHighChangeHandler =(e:any) => {
        setHigh(e.target.value);
    }

    const onLowChangeHandler =(e:any) => {
        setLow(e.target.value);
    }

    const onCloseChangeHandler =(e:any) => {
        setClose(e.target.value);
    }

    const onAdjCloseChangeHandler =(e:any) => {
        setAdjClose(e.target.value);
    }

    const onVolumeChangeHandler =(e:any) => {
        setVolume(e.target.value);
    }

    const onSubmitBtnHandler =(e: any) => {
        e.preventDefault();

        const data: IStockPrice = {
            //id: ticker+priceDate
            ticker: ticker,
            priceDate: priceDate,
            open: Number(open),
            high: Number(high),
            low: Number(low),
            close: Number(close),
            adjClose: Number(adjClose),
            volume: Number(volume)
        }   
        //alert(data);
        onSubmitFormAddPricesHandler(props.stockPriceToEditIndex, data);
        onBackButtonClickHandler();
    }
    

    return (
    <>  
        
        <div className=".form-container ">
            <div>   
                <h3>
                    {(props.stockPriceToEditIndex<0 ? "Add Stock Prices" : "Update Existing Stock Prices")}
                </h3>
            </div>
            <form onSubmit={onSubmitBtnHandler}>
                <table className="table_addprices"> 
                    <tbody>
                    <tr><td className="td_label"><label>Ticker:</label></td><td><input type="text" disabled={disabled} value={ticker}  onChange={onTickerChangeHandler}/></td></tr>
                    <tr><td className="td_label"><label>PriceDate:</label></td><td><input type="text" value={priceDate}  onChange={onPriceDateChangeHandler}/></td></tr>
                    <tr><td className="td_label"><label>Open:</label></td><td><input type="text"  value={open}  onChange={onOpenChangeHandler}/></td></tr>
                    <tr><td className="td_label"><label>High:</label></td><td><input type="text"  value={high}  onChange={onHighChangeHandler}/></td></tr>
                    <tr><td className="td_label"><label>Low:</label></td><td><input type="text"  value={low}  onChange={onLowChangeHandler}/></td></tr>
                    <tr><td className="td_label"><label>Close:</label></td><td><input type="text"  value={close}  onChange={onCloseChangeHandler}/></td></tr>
                    <tr><td className="td_label"><label>AdjClose:</label></td><td><input type="text"  value={adjClose}  onChange={onAdjCloseChangeHandler}/></td></tr>
                    <tr><td className="td_label"><label>Volume:</label></td><td><input type="text"  value={volume}  onChange={onVolumeChangeHandler}/></td></tr>
                    </tbody>   
                </table>
                <div>
                    &nbsp;
                </div>
                <div>
                    <input type="button" value ="Back" onClick={onBackButtonClickHandler}/>
                    <input type="submit" value ="Submit Prices For Date" onClick={onSubmitBtnHandler}/>
                </div>
            </form>
        </div>
    </>  
    );
}

export default AddUpdateStockPricesForDay;

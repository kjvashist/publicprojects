import httpService from "../service/HttpService";
import ApiConfig from "../service/ApiConfig";
import { IStockPrice } from "./StockPrice.type";
import axios from "axios";

const addOrUpdateTickerPricesForDate = async (stockPrice:IStockPrice) => {
    const urlUpdateTickerPricesForDate = ApiConfig.addupdatetickerprice+"/"+stockPrice.ticker+"/"+stockPrice.priceDate;
    //alert(urlUpdateTickerPricesForDate);
    let stockPriceJsonString = JSON.stringify(stockPrice);
    //alert(stockPriceJsonString);
    const response = await axios.post(urlUpdateTickerPricesForDate, stockPriceJsonString, {headers: {'Content-Type': 'application/text'}});
    return response.data;
};


export default addOrUpdateTickerPricesForDate;


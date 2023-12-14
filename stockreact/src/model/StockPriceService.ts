import httpService from "../service/HttpService";
import ApiConfig from "../service/ApiConfig";
import { IStockPrice } from "./StockPrice.type";

const getTickerPricesList = async (ticker:String) => {
    const urlTickerPrices = ApiConfig.tickerprices+"/"+ticker;
    const result = await httpService.get<IStockPrice[]>(urlTickerPrices);
    //alert(result);
    //console.log(result.data);
    return result.data;
};

export default getTickerPricesList;


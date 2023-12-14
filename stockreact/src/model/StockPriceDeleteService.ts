import httpService from "../service/HttpService";
import ApiConfig from "../service/ApiConfig";


const deleteTickerPricesForDate = async (ticker:String, priceDate:String) => {
    const urlDeleteTickerPricesForDate = ApiConfig.deletetickerprices+"/"+ticker+"/"+priceDate;
    //alert(urlDeleteTickerPricesForDate);
    const response = await httpService.delete<void>(urlDeleteTickerPricesForDate);
    return response.data;
};

export default deleteTickerPricesForDate;


import httpService from "../service/HttpService";
import ApiConfig from "../service/ApiConfig";

const getTickersList = async () => {
    const urlTickers = ApiConfig.tickers;
    const result = await httpService.get<string[]>(urlTickers);
    //alert(result);
    //console.log(result.data);
    return result.data;
};

export default getTickersList;
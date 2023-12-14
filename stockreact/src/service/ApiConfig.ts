const baseUrl = "http://localhost:8081";
//const baseUrl = "https://www.kjvashist.com/stockweb";

const ApiConfig = {
    //tickerprices: 'http://localhost:8081/tickerprices/'
    tickerprices: baseUrl+'/tickerprices',
    tickers: baseUrl+'/tickers',
    addupdatetickerprice: baseUrl+'/addupdatetickerprice',
    deletetickerprices: baseUrl+'/deletetickerprice'
}

export default ApiConfig;

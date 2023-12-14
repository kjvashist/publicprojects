namespace stock_asp_api.Model
{
    public class StockPrice 
    {
        public String Ticker  { get; set; }
        //raw Data
        public String PriceDate  { get; set; }
        public double Open  { get; set; }
        public double High  { get; set; }
        public double Low  { get; set; }

        public double Close  { get; set; }
        public double AdjClose  { get; set; }
        public double Volume  { get; set; }
    }
}
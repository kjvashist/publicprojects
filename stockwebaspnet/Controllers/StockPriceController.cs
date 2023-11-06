using Microsoft.AspNetCore.Mvc;
using Microsoft.Data.Sqlite;
using stockwebaspnet.Model;

namespace stockwebaspnet.Controllers;

[ApiController]
[Route("[controller]")]
public class StockPriceController : ControllerBase
{
    private readonly ILogger<StockPriceController> _logger;

    private readonly IConfiguration _configuration;
    private int _monthsOfData=3;
    public StockPriceController(ILogger<StockPriceController> logger, IConfiguration configuration)
    {
        _logger = logger;
        _configuration=configuration;
        string? monthsOfDataString = _configuration.GetValue<string>("MONTHS_DATA");
        if(Int32.TryParse(monthsOfDataString, out _monthsOfData)==false)
        {
            _monthsOfData=3;
        }
    }



    // private static readonly string[] Summaries = new[]
    // {
    //     "Freezing", "Bracing", "Chilly", "Cool", "Mild", "Warm", "Balmy", "Hot", "Sweltering", "Scorching"
    // };

    // [HttpGet(Name = "GetStockPrice")]
    // public IEnumerable<StockPrice> Get()
    // {
    //     return Enumerable.Range(1, 5).Select(index => new StockPrice
    //     {
    //         Ticker = "AAPL",
    //         PriceDate = DateOnly.FromDateTime(DateTime.Now.AddDays(index)),
    //         Open = 0.01
    //     })
    //     .ToArray();
    // }

    [HttpGet("/StockPrice/Tickers")]
    public IEnumerable<string> Tickers()
    {
        SqliteConnection sqliteConnection = DbWrapper.OpenConnection(_configuration);
        List<string>  listTickers =  StockPriceService.GetTickers(sqliteConnection);
        sqliteConnection.Close();
        return listTickers;
    }

    [HttpGet("/StockPrice/TickerPrices")]
    public IEnumerable<StockPrice> TickerPrices([FromQuery] string ticker)
    {
        SqliteConnection sqliteConnection = DbWrapper.OpenConnection(_configuration);
        List<StockPrice>  listStockPrices =  StockPriceService.GetTickerPrices(sqliteConnection, ticker, _monthsOfData);
        sqliteConnection.Close();
        return listStockPrices;
    }
}

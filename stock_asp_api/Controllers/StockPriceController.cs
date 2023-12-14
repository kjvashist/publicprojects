using Microsoft.AspNetCore.Mvc;
using Microsoft.Data.Sqlite;
using stock_asp_api.Model;
using System.Data;

namespace stock_asp_api.Controllers;

[ApiController]
[Route("[/api/[controller]")]
public class StockPriceController : ControllerBase
{
    private readonly ILogger<StockPriceController> _logger;

    private readonly IConfiguration _configuration;
    private int _monthsOfData = 3;
    public StockPriceController(ILogger<StockPriceController> logger, IConfiguration configuration)
    {
        _logger = logger;
        _configuration = configuration;
        string? monthsOfDataString = _configuration.GetValue<string>("MONTHS_DATA");
        if (Int32.TryParse(monthsOfDataString, out _monthsOfData) == false)
        {
            _monthsOfData = 3;
        }
    }

    //[HttpGet("/StockPrice/Tickers")]
    //public IEnumerable<string> Tickers()
    //{
    //    SqliteConnection sqliteConnection = DbWrapper.OpenConnection(_configuration);
    //    List<string> listTickers = StockPriceService.GetTickers(sqliteConnection);
    //    sqliteConnection.Close();
    //    return listTickers;
    //}


    [HttpGet("/StockPrice/Tickers")]
    public async Task<IActionResult> Tickers()
    {
        SqliteConnection sqliteConnection = DbWrapper.OpenConnection(_configuration);
        var myTask = Task.Run(() => StockPriceService.GetTickers(sqliteConnection));
        List<string> listTickers = await myTask;
        sqliteConnection.Close();
        return Ok(listTickers);
    }

    [HttpGet("/StockPrice/TickerPrices")]
    public async Task<List<StockPrice>> TickerPrices([FromQuery] string ticker)
    {
        SqliteConnection sqliteConnection = DbWrapper.OpenConnection(_configuration);
        var myTask = Task.Run(() => StockPriceService.GetTickerPrices(sqliteConnection, ticker, "", _monthsOfData));
        List<StockPrice> listStockPrices = await myTask;
        sqliteConnection.Close();
        return listStockPrices;
    }

    [HttpGet("/StockPrice/TickerPricesForDate/{ticker}/{priceDate}")]
    public async Task<IActionResult> TickerPricesForDate(string ticker, string priceDate)
    {
        SqliteConnection sqliteConnection = DbWrapper.OpenConnection(_configuration);
        var myTask = Task.Run(() => StockPriceService.GetTickerPricesForDate(sqliteConnection, ticker, priceDate));
        StockPrice? stockPrice = await myTask;
        sqliteConnection.Close();
        if (stockPrice == null)
            return NotFound();
        return Ok(stockPrice);
    }

    [HttpPut("/StockPrice/Update")]
    public async Task<IActionResult> UpdateTickerPrices([FromBody] StockPrice stockPrice)
    {
        SqliteConnection sqliteConnection = DbWrapper.OpenConnection(_configuration);
        var myTask = Task.Run(() => StockPriceService.UpdateTickerPrices(sqliteConnection, stockPrice));
        int updateStatus= await myTask;
        sqliteConnection.Close();
        return Ok(updateStatus);
    }


    [HttpDelete("/StockPrice/DeleteStockPrices/{ticker}/{priceDate}")]
    public async Task<IActionResult> DeleteStockPrices([FromRoute] string ticker, [FromRoute] string priceDate)
    {
        SqliteConnection sqliteConnection = DbWrapper.OpenConnection(_configuration);
        var myTask = Task.Run(() => StockPriceService.DeleteStockPrices(sqliteConnection, ticker, priceDate));
        int deleteStatus = await myTask;
        sqliteConnection.Close();
        return Ok(deleteStatus);
    }
}

import { IStockPrice } from "../model/StockPrice.type";
import "./StockPriceList.style.css"
//import 'bootstrap/dist/css/bootstrap.css';

type Props = {
    list: IStockPrice[];
    onEditRecordHandler: (data: IStockPrice) => void;
    onDeleteRecordHandler: (ticker:string,  priceDate: string) => void;
};


const StockPriceList = (props: Props) => {
    const { list, onEditRecordHandler,onDeleteRecordHandler} = props;


    return (
        <div>
            <article>
                <h3 className="list-header">StockPrices for Ticker</h3>
            </article>
            <table>
                <tbody>
                    <tr>
                        <th>Ticker</th>
                        <th>PriceDate</th>
                        <th>Open</th>
                        <th>High</th>
                        <th>Low</th>
                        <th>Close</th>
                        <th>AdjClose</th>
                        <th>Volume</th>
                        <th>Actions</th>
                    </tr>
                    {list.map((stockPrice) => {
                        // console.log(stockPrice);
                        return (
                            <tr key={stockPrice.ticker + stockPrice.priceDate.toString()}>
                                <td>{stockPrice.ticker}</td>
                                <td>{stockPrice.priceDate}</td>
                                <td>{stockPrice.open.toFixed(2)}</td>
                                <td>{stockPrice.high.toFixed(2)}</td>
                                <td>{stockPrice.low.toFixed(2)}</td>
                                <td>{stockPrice.close.toFixed(2)}</td>
                                <td>{stockPrice.adjClose.toFixed(2)}</td>
                                <td>{stockPrice.volume.toLocaleString()}</td>
                                <td>
                                    <div>
                                        <input type="button" value="Edit" onClick={() => onEditRecordHandler(stockPrice)} />
                                        <input type="button" value="Delete" className="button-danger" onClick={() => onDeleteRecordHandler(stockPrice.ticker,stockPrice.priceDate)} />
                                    </div>
                                </td>
                            </tr>
                        );
                    })}

                </tbody>
            </table>
        </div>);
}

export default StockPriceList;

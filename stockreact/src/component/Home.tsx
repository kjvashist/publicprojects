import { useEffect, useRef, useState } from "react";
import "./Home.style.css"
import { IStockPrice, PageTypeEnum, emptyStockPriceList,dummyTickersList} from "../model/StockPrice.type";
import StockPriceList from "./StockPriceList";
import AddUpdateStockPricesForDay from "./AddUpdateStockPriceRecord";

import getTickerPricesList from "../model/StockPriceService";
import getTickersList from "../model/TickersService";
import addOrUpdateTickerPricesForDate from "../model/StockPriceUpdateService";
import deleteTickerPricesForDate from "../model/StockPriceDeleteService";



const Home = () => {
//   return <div>This Home Page for Stock Prices</div>  
    const [stockPriceList, setStockPriceList] = useState(emptyStockPriceList as IStockPrice[]);
    const [tickersList, setTickersList] = useState(dummyTickersList as string[]);
    const [shownPage, setShownPage] = useState(PageTypeEnum.list) 
    const [stockPriceToEditIndex, setStockPriceToEditIndex] = useState(-1) 
    const selectedTicker = useRef("AAPL");
    
    
    const fetchTickers = async () => {
        const listTickersData = await getTickersList();
        console.log(listTickersData);
        setTickersList(listTickersData)
    }  


    const fetchTickerPricesData = async (ticker: string) => {
        const listStockPricesData = await getTickerPricesList(ticker);
        setStockPriceList(listStockPricesData)
        setShownPage(PageTypeEnum.list);
    }  
    

    useEffect(  ()=> {
        //const listInuptString = window.localStorage.getItem("StockPriceList");
        //if(listInuptString){
        //    setStockPriceList(JSON.parse(listInuptString));
        //}
        fetchTickers();
        if(tickersList.length>0)
        {
            fetchTickerPricesData(selectedTicker.current);
        }
    },[]);
    
    const persistStockPriceList = (list:IStockPrice[]) => {
        setStockPriceList(list); 
        window.localStorage.setItem("StockPriceList", JSON.stringify(list));
    }

    const onAddNewStockPriceRecordHandler =() => {
        setStockPriceToEditIndex(-1);
        setShownPage(PageTypeEnum.add);
    }

    const showListPageHandler =() => {
        setShownPage(PageTypeEnum.list);
    }

    const deleteStockPriceRecord = async (ticker:string, priceDate:string) => {
        //const indexToDelete = stockPriceList.indexOf(data);
        //const tempList = [...stockPriceList];
        //tempList.splice(indexToDelete, 1);
        //persistStockPriceList(tempList)
        let retval = await deleteTickerPricesForDate(ticker,priceDate);
        setShownPage(PageTypeEnum.list);
        fetchTickerPricesData(selectedTicker.current);
    }

    const addOrUpdateStockPricesToList = async (indexDataToUpdate:number, stockPriceData:IStockPrice) => {
        if(indexDataToUpdate<0)
        {
            //persistStockPriceList( [...stockPriceList, stockPriceData]);
            let retval = await addOrUpdateTickerPricesForDate(stockPriceData);
            setShownPage(PageTypeEnum.list);
            fetchTickerPricesData(selectedTicker.current);
        }
        else
        {
            //alert('updateStockPricesToList indexDataToUpdate = ' +indexDataToUpdate.toString() + " dataRecordToUpdate.open = " +" data.open = " + data.open.toString() ) ;
            if(indexDataToUpdate>=0){
                //const tempList = [...stockPriceList];
                //tempList[indexDataToUpdate] = stockPriceData;
                //persistStockPriceList(tempList)
                let retval = await addOrUpdateTickerPricesForDate(stockPriceData);
                setShownPage(PageTypeEnum.list);
                fetchTickerPricesData(selectedTicker.current);
            }
        }
    }
    
    const editStockPriceRecord = (data: IStockPrice) => {
        //alert('editStockPriceRecord' + data.ticker + " data.priceDate = " + data.priceDate.toString() + " data.open = " + data.open.toString());
        const indexToEdit = stockPriceList.indexOf(data);
        //alert('indexToEdit' + indexToEdit.toString());
        setStockPriceToEditIndex(indexToEdit);
        setShownPage(PageTypeEnum.edit);
    }

    type HtmlEvent = React.ChangeEvent<HTMLSelectElement>

    const onChangeHandler: React.EventHandler<HtmlEvent> = 
    (event: HtmlEvent) => { 
        console.log(event.target.value) 
        selectedTicker.current=event.target.value;
        fetchTickerPricesData(selectedTicker.current);
        // alert(selectedTicker.current );
    }

    const populateTickersDropDown = () => {
        return (
            <select name='combotickers' value={selectedTicker.current} onChange={onChangeHandler}>
            {tickersList.map((string) => (
                <option key={string} value={string}>{string}</option>
            ))}
            </select>
        );
        }


    return  (
        <>
            <article className="article-header">
                <header>
                    <h1>Simple React Typescript App for Stock Prices wih Java Spring Boot REST API</h1>
                </header>
            </article>
            <section className="section-content">
                {
                    shownPage === PageTypeEnum.list && (
                        <>
                        <label>Tickers:</label>
                        {populateTickersDropDown()}
                        <input 
                        type="button" 
                        value="Add New Record"
                         onClick={onAddNewStockPriceRecordHandler}
                         className ="add-stockprice-btn"
                         />
                        <StockPriceList list={stockPriceList} onEditRecordHandler={editStockPriceRecord} onDeleteRecordHandler={deleteStockPriceRecord} />
                        </>
                    )
                }

                {shownPage === PageTypeEnum.add && <AddUpdateStockPricesForDay  stockPriceToEditIndex={stockPriceToEditIndex} stockPriceList={stockPriceList} onBackButtonClickHandler={showListPageHandler} onSubmitFormAddPricesHandler={addOrUpdateStockPricesToList} /> }
                {shownPage === PageTypeEnum.edit && <AddUpdateStockPricesForDay stockPriceToEditIndex={stockPriceToEditIndex} stockPriceList={stockPriceList} onBackButtonClickHandler={showListPageHandler} onSubmitFormAddPricesHandler={addOrUpdateStockPricesToList}/> }

            </section>
        </>
    );
};

export default  Home;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Data.Sqlite;

namespace stock_asp_api.Model
{
    
    public class DbWrapper
    {
        public static string _connString = "";
        
        public static SqliteConnection OpenConnection(IConfiguration _configuration)
        {
            SqliteConnection conn=null;
            if(_connString.Length==0)
            {
                string dbPaths = _configuration.GetSection("ConnectionStrings").GetValue<string>("DBPaths");
                List<string> listDbPaths=dbPaths.Split(',').Select(x => x.Trim()).ToList();
                foreach(string strPath in listDbPaths)
                {
                    try{
                        string connectionString = $"Data Source={strPath}";
                        conn = new SqliteConnection(connectionString);
                        conn.Open();
                        _connString = connectionString;
                        break;
                    }
                    catch(Exception exp)
                    {

                    }
                }
            }
            else
            {
                conn = new SqliteConnection(_connString);
                conn.Open();
            }
            return conn;
        }

    }
}
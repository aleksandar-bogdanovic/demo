package com.flight.demo;



import com.amadeus.exceptions.ResponseException;
import org.json.JSONException;


import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.*;

public class getDataSql {

    public static void getDataSql(String origin, String dest, String depDate, String retDate, Integer passengerNo, String currency) throws JSONException, ResponseException {
        Connection conn = null;
        Statement stmt=null;
        String PolazniA;
        String OdredisniA;
        String PolazniD;
        String DolazniD;
        Integer BrPutnika;
        String Valuta;
        String Cijena;
        int id2=0;
        int idS=0;
        boolean provjera=false;
        try{
            //STEP 2: Register JDBC driver
            DbManager db = new DbManager();
            conn = db.getConection();

            stmt = conn.createStatement();
            String sql;

            sql = "SELECT * FROM letovi.pretraga";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                PolazniA= rs.getString("PolazniAerodrom");
                OdredisniA= rs.getString("OdredisniAerodrom");
                PolazniD= rs.getString("DatumPolaska");
                DolazniD= rs.getString("DatumPovratka");
                BrPutnika= rs.getInt("BrojPutnika");
                Valuta= rs.getString("Valuta");
                if(PolazniA.equals(origin)&&OdredisniA.equals(dest)&&PolazniD.equals(depDate)&&DolazniD.equals(retDate)&&BrPutnika.equals(passengerNo)&&Valuta.equals(currency)){
                    provjera=true;
                    id2=rs.getInt("PretragaID");
                }
                idS=rs.getInt("PretragaID");
            }
            stmt.close();
            rs.close();
            conn.close();

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }//Provjera unosa korisnika dali postoji u bazi

        if(provjera==true) {
            System.out.println("Test"+id2);
            try{
                //STEP 2: Register JDBC driver
                DbManager db = new DbManager();
                conn = db.getConection();

                stmt = conn.createStatement();
                String sql;

                sql = "SELECT * FROM letovi.letovi WHERE PretragaID='"+id2 +"'";
                ResultSet rs = stmt.executeQuery(sql);
                String prov="1";
                while(rs.next()){
                    PolazniA= rs.getString("PolazniAerodrom");
                    OdredisniA= rs.getString("OdredisniAerodrom");
                    PolazniD= rs.getString("DatumPolaska");
                    DolazniD= rs.getString("DatumPovratka");
                    BrPutnika= rs.getInt("BrojSjedista");
                    Cijena= rs.getString("UkupnaCijena");
                    Valuta=rs.getString("Valuta");
                    System.out.println("from "+PolazniA+" at "+PolazniD+" to "+OdredisniA+" at "+DolazniD+" | "+BrPutnika+" seats available.");
                    if(!Cijena.equals(prov)){
                        System.out.println("-----Trip cost: "+Cijena+" "+Valuta+"-----");
                        System.out.println("-----------------------------");
                    }
                }
                stmt.close();
                rs.close();
                conn.close();

            }catch(SQLException se){
                //Handle errors for JDBC
                se.printStackTrace();
            }catch(Exception e){
                //Handle errors for Class.forName
                e.printStackTrace();
            }finally{

                try{
                    if(stmt!=null)
                        stmt.close();
                }catch(SQLException se2){
                }
                try{
                    if(conn!=null)
                        conn.close();
                }catch(SQLException se){
                    se.printStackTrace();
                }
            }//Provjera unosa korisnika dali postoji u bazi
        }
        else{
            conn = null;
            stmt=null;
            try {

                // Open a connection
                DbManager db = new DbManager();
                conn = db.getConection();

                int abc=1;
                String sql;
                sql = "INSERT INTO pretraga ( PolazniAerodrom, OdredisniAerodrom, DatumPolaska, DatumPovratka, BrojPutnika, Valuta) VALUES (?,?,?,?,?,?)";
                stmt = conn.prepareStatement(sql);
                ((PreparedStatement) stmt).setString(1, origin);
                ((PreparedStatement) stmt).setString(2, dest);
                ((PreparedStatement) stmt).setString(3, depDate);
                ((PreparedStatement) stmt).setString(4, retDate);
                ((PreparedStatement) stmt).setInt(5, passengerNo);
                ((PreparedStatement) stmt).setString(6, currency);
                ((PreparedStatement) stmt).executeUpdate();
                // STEP 6: Clean-up environment

                stmt.close();
                conn.close();

            } catch (SQLException se) {
                // Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                // Handle errors for Class.forName
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            } //Ubacivanje u bazu pretrage korisnika

            getData.getData(origin,  dest, depDate,  retDate,  passengerNo,  currency,idS+1);


        }

    }

}

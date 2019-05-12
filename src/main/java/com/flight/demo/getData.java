package com.flight.demo;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.*;

import java.util.ArrayList;

public class getData {

public static void getData(String origin, String dest, String depDate, String retDate, Integer passengerNo, String currency,int ID) throws JSONException, ResponseException {

    Amadeus amadeus = Amadeus
            .builder("XGgFtAosGX8R5OAJ4sZcXEbP2rLZRz7b", "zPy9EsDxUFkc6p4C")
            .build();

    FlightOffer[] flightOffersOdlazak  = amadeus.shopping.flightOffers.get(Params
            .with("origin", origin)
            .and("destination", dest)
            .and("departureDate", depDate)
            //.and ("returnDate",retDate)
            .and ("currency",currency));

    FlightOffer[] flightOffersPovratak  = amadeus.shopping.flightOffers.get(Params
            .with("origin", dest)
            .and("destination", origin)
            .and("departureDate", retDate)
            //.and ("returnDate",retDate)
            .and ("currency",currency));

//		Location[] locations = amadeus.referenceData.locations.get(Params
//				.with("keyword", "LON")
//				.and("subType", Locations.ANY));
//
//		for (int i = 0; i < locations.length; i++) {
//			System.out.println(locations[i].getResponse().getBody());
//		}



    String dataOdlazak=flightOffersOdlazak[0].getResponse().getBody();
    System.out.println(dataOdlazak);

    String dataPovratak=flightOffersPovratak[0].getResponse().getBody();
    System.out.println(dataPovratak);

//		JSONObject res=new JSONObject(data);
//		JSONObject dataObject=res.getJSONObject("data");
//
//		//JSONArray arr=new JSONArray(dataObject);
//		JSONArray arr=(JSONArray) dataObject.get("services");
//
//		ArrayList<String> list = new ArrayList<String>();
//
//
//		for (int i = 0; i < arr.length(); i++) {
//			list.add(arr.getJSONObject(i).getString("iataCode"));
//		}
//
//		System.out.println(list);

    JSONObject obj = new JSONObject(dataOdlazak);
    JSONArray dataArr=obj.getJSONArray("data");

    JSONObject obj2 = new JSONObject(dataPovratak);
    JSONArray dataArr2=obj2.getJSONArray("data");

    boolean brPutnikaOdlazak=false;


    System.out.println("============================================= PONUDA ODLAZAK =============================================");
    for (int m = 0; m < dataArr.length(); m++) {

        JSONArray offerItemsArr=dataArr.getJSONObject(m).getJSONArray("offerItems");

        outerloop:
        for (int k = 0; k < offerItemsArr.length(); k++) {

            JSONArray servicesArr=offerItemsArr.getJSONObject(k).getJSONArray("services");
            JSONObject priceObj=offerItemsArr.getJSONObject(k).getJSONObject("price");
            Double price=priceObj.getDouble("total");

            for (int j = 0; j < servicesArr.length(); j++) {

                JSONArray segmentsArr=servicesArr.getJSONObject(j).getJSONArray("segments");


                for (int l = 0; l <segmentsArr.length() ; l++) {
                    JSONObject availObj=segmentsArr.getJSONObject(l).getJSONObject("pricingDetailPerAdult");
                    Integer availability=availObj.getInt("availability");
                    if (availability<passengerNo){
                        continue outerloop;
                    }
                }


                for (int i = 0; i < segmentsArr.length(); i++) {

                    JSONObject flightSegmentsObj=segmentsArr.getJSONObject(i).getJSONObject("flightSegment");
                    JSONObject availObj=segmentsArr.getJSONObject(i).getJSONObject("pricingDetailPerAdult");


                    JSONObject departureObj=flightSegmentsObj.getJSONObject("departure");
                    JSONObject arrivalObj=flightSegmentsObj.getJSONObject("arrival");

                    String depIATA=departureObj.getString("iataCode");
                    String arrIATA=arrivalObj.getString("iataCode");
                    Integer availability=availObj.getInt("availability");

                    String depTime=departureObj.getString("at");
                    String arrTime=arrivalObj.getString("at");

                    brPutnikaOdlazak=true;
                    System.out.println("from "+depIATA+" at "+depTime+" to "+arrIATA+" at "+arrTime+" | "+availability+" seats available.");
                    Connection conn = null;
                    try {

                        // Open a connection
                        DbManager db = new DbManager();
                        conn = db.getConection();

                        int abc=1;
                        String sql;
                        sql = "INSERT INTO letovi ( PolazniAjrodrom, OdredisniAjrodrom, DatumPolaska, DatumPovratka, BrojSjedista, UkupnaCijena,Valuta,PretragaID) VALUES (?,?,?,?,?,?,?,?)";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setInt(8,ID);
                        stmt.setString(1, depIATA);
                        stmt.setString(2, arrIATA);
                        stmt.setString(3, depTime);
                        stmt.setString(4, arrTime);
                        stmt.setInt(5, availability);
                        if(segmentsArr.length()-1== i){
                        stmt.setDouble(6, price);}
                        else{
                            stmt.setDouble(6, abc);
                        }
                        stmt.setString(7, currency);
                        stmt.executeUpdate();
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
                    } //Ubacivanje u bazu ponude odlazka
                }
                if (brPutnikaOdlazak){
                System.out.println("-----Trip cost: "+price+"-----");
                System.out.println("-----------------------------");
                }
                brPutnikaOdlazak=false;
            }
        }
    }

    System.out.println("============================================= PONUDA POVRATAK =============================================");

    boolean brPutnikaPovratak=false;

    for (int m = 0; m < dataArr2.length(); m++) {

        JSONArray offerItemsArr2=dataArr2.getJSONObject(m).getJSONArray("offerItems");

        outerloop:
        for (int k = 0; k < offerItemsArr2.length(); k++) {

            JSONArray servicesArr2=offerItemsArr2.getJSONObject(k).getJSONArray("services");
            JSONObject priceObj2=offerItemsArr2.getJSONObject(k).getJSONObject("price");
            Double price2=priceObj2.getDouble("total");

            for (int j = 0; j < servicesArr2.length(); j++) {

                JSONArray segmentsArr2=servicesArr2.getJSONObject(j).getJSONArray("segments");
                for (int l = 0; l <segmentsArr2.length() ; l++) {
                    JSONObject availObj=segmentsArr2.getJSONObject(l).getJSONObject("pricingDetailPerAdult");
                    Integer availability=availObj.getInt("availability");
                    if (availability<passengerNo){
                        continue outerloop;
                    }
                }

                for (int i = 0; i < segmentsArr2.length(); i++) {

                    JSONObject flightSegmentsObj2=segmentsArr2.getJSONObject(i).getJSONObject("flightSegment");

                    JSONObject availObj2=segmentsArr2.getJSONObject(i).getJSONObject("pricingDetailPerAdult");
                    Integer availability2=availObj2.getInt("availability");

                    JSONObject departureObj2=flightSegmentsObj2.getJSONObject("departure");
                    JSONObject arrivalObj2=flightSegmentsObj2.getJSONObject("arrival");

                    String depIATA2=departureObj2.getString("iataCode");
                    String arrIATA2=arrivalObj2.getString("iataCode");

                    String depTime2=departureObj2.getString("at");
                    String arrTime2=arrivalObj2.getString("at");

                    System.out.println("from "+depIATA2+" at "+depTime2+" to "+arrIATA2+" at "+arrTime2+" | "+availability2+" seats available.");
                    brPutnikaPovratak=true;
                    Connection conn = null;
                    try {

                        // Open a connection
                        DbManager db = new DbManager();
                        conn = db.getConection();
                        int abc=1;
                        String sql;
                        sql = "INSERT INTO letovi ( PolazniAjrodrom, OdredisniAjrodrom, DatumPolaska, DatumPovratka, BrojSjedista, UkupnaCijena,Valuta,PretragaID) VALUES (?,?,?,?,?,?,?,?)";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setInt(8,ID);
                        stmt.setString(1, depIATA2);
                        stmt.setString(2, arrIATA2);
                        stmt.setString(3, depTime2);
                        stmt.setString(4, arrTime2);
                        stmt.setInt(5, availability2);
                        if(segmentsArr2.length()-1== i){
                            stmt.setDouble(6, price2);}
                        else{
                            stmt.setDouble(6, abc);
                        }
                        stmt.setString(7, currency);
                        stmt.executeUpdate();
                        // STEP 6: Clean-up environment

                        stmt.close();
                        conn.close();

                    } catch (SQLException se) {
                        // Handle errors for JDBC
                        se.printStackTrace();
                    } catch (Exception e) {
                        // Handle errors for Class.forName
                        e.printStackTrace();
                    } finally { //Ubacivanje u bazu ponude odlazka
                        try {
                            if (conn != null)
                                conn.close();
                        } catch (SQLException se) {
                            se.printStackTrace();
                        }
                    } //Ubacivanje u bazu ponude odlazka
                }
                if (brPutnikaPovratak){
                System.out.println("-----Trip cost: "+price2+"-----");
                System.out.println("-----------------------------");
                }
                brPutnikaPovratak=false;

            }
        }
        }
    }
}

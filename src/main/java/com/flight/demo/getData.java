package com.flight.demo;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class getData {

public static ArrayList<String> getData(String origin, String dest, String depDate, String retDate, Integer passengerNo, String currency) throws JSONException, ResponseException {

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

    ArrayList<String> depList=new ArrayList<String>();
    ArrayList<String> arrList=new ArrayList<String>();

    boolean brPutnikaOdlazak=false;


    System.out.println("============================================= PONUDA ODLAZAK =============================================");
    for (int m = 0; m < dataArr.length(); m++) {

        JSONArray offerItemsArr=dataArr.getJSONObject(m).getJSONArray("offerItems");


        for (int k = 0; k < offerItemsArr.length(); k++) {

            JSONArray servicesArr=offerItemsArr.getJSONObject(k).getJSONArray("services");
            JSONObject priceObj=offerItemsArr.getJSONObject(k).getJSONObject("price");
            Double price=priceObj.getDouble("total");

            for (int j = 0; j < servicesArr.length(); j++) {

                JSONArray segmentsArr=servicesArr.getJSONObject(j).getJSONArray("segments");

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

                    depList.add(depIATA);
                    arrList.add(arrIATA);

                    if (availability>passengerNo){
                        brPutnikaOdlazak=true;
                    System.out.println("from "+depIATA+" at "+depTime+" to "+arrIATA+" at "+arrTime+" | "+availability+" seats available.");
                    }
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


        for (int k = 0; k < offerItemsArr2.length(); k++) {

            JSONArray servicesArr2=offerItemsArr2.getJSONObject(k).getJSONArray("services");
            JSONObject priceObj2=offerItemsArr2.getJSONObject(k).getJSONObject("price");
            Double price2=priceObj2.getDouble("total");

            for (int j = 0; j < servicesArr2.length(); j++) {

                JSONArray segmentsArr2=servicesArr2.getJSONObject(j).getJSONArray("segments");

                for (int i = 0; i < segmentsArr2.length(); i++) {

                    JSONObject flightSegmentsObj2=segmentsArr2.getJSONObject(i).getJSONObject("flightSegment");
                    JSONObject availObj2=segmentsArr2.getJSONObject(i).getJSONObject("pricingDetailPerAdult");


                    JSONObject departureObj2=flightSegmentsObj2.getJSONObject("departure");
                    JSONObject arrivalObj2=flightSegmentsObj2.getJSONObject("arrival");

                    String depIATA2=departureObj2.getString("iataCode");
                    String arrIATA2=arrivalObj2.getString("iataCode");
                    Integer availability2=availObj2.getInt("availability");

                    String depTime2=departureObj2.getString("at");
                    String arrTime2=arrivalObj2.getString("at");

                    depList.add(depIATA2);
                    arrList.add(arrIATA2);

                    if (availability2>passengerNo){
                        brPutnikaPovratak=true;
                    System.out.println("from "+depIATA2+" at "+depTime2+" to "+arrIATA2+" at "+arrTime2+" | "+availability2+" seats available.");
                    }
                }
                if (brPutnikaPovratak){
                System.out.println("-----Trip cost: "+price2+"-----");
                System.out.println("-----------------------------");
                }
                brPutnikaPovratak=false;

            }
        }
    }
return depList;
    }
}

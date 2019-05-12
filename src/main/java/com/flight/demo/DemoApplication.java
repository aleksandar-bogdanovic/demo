package com.flight.demo;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.ArrayList;
import java.util.Scanner;


@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws ResponseException, JSONException {
		SpringApplication.run(DemoApplication.class, args);

		Scanner Polazisni = new Scanner(System.in);
		System.out.println("Aerodrom polaska");


		String PolazisniI = Polazisni.nextLine();
		System.out.println("Aerodrom polaska: " + PolazisniI);

		Scanner Odredisni = new Scanner(System.in);
		System.out.println("Aerodrom povratka");


		String OdredisniI = Odredisni.nextLine();
		System.out.println("Aerodrom povratka	: " + OdredisniI);

		Scanner DatumPolaska = new Scanner(System.in);
		System.out.println("Datum Polaska");


		String DatumPolaskaI = DatumPolaska.nextLine();
		System.out.println("Datum Polaska: " + DatumPolaskaI);

		Scanner DatumDolaska = new Scanner(System.in);
		System.out.println("Datum Dolaska");


		String DatumDolaskaI = DatumDolaska.nextLine();
		System.out.println("Datum Dolaska : " + DatumDolaskaI);

		Scanner BrPutnika = new Scanner(System.in);
		System.out.println("Broj putnika");


		Integer BrPutnikaI = BrPutnika.nextInt();
		System.out.println("Broj putnika: " + BrPutnikaI);

		Scanner Valuta = new Scanner(System.in);
		System.out.println("Valuta:");


		String ValutaI = Valuta.nextLine();
		System.out.println("Valuta: " + ValutaI);


		getData.getData(PolazisniI,OdredisniI, DatumPolaskaI, DatumDolaskaI, BrPutnikaI, ValutaI);


//		Amadeus amadeus = Amadeus
//				.builder("XGgFtAosGX8R5OAJ4sZcXEbP2rLZRz7b", "zPy9EsDxUFkc6p4C")
//				.build();
//
//		FlightOffer[] flightOffers  = amadeus.shopping.flightOffers.get(Params
//				.with("origin", "BUD")
//				.and("destination", "JFK")
//				.and("departureDate", "2019-08-06")
//				.and ("currency","HRK"));
//
////		Location[] locations = getData.referenceData.locations.get(Params
////				.with("keyword", "LON")
////				.and("subType", Locations.ANY));
////
////		for (int i = 0; i < locations.length; i++) {
////			System.out.println(locations[i].getResponse().getBody());
////		}
//
//
//		String data=flightOffers[0].getResponse().getBody();
//		System.out.println(data);
//
////		JSONObject res=new JSONObject(data);
////		JSONObject dataObject=res.getJSONObject("data");
////
////		//JSONArray arr=new JSONArray(dataObject);
////		JSONArray arr=(JSONArray) dataObject.get("services");
////
////		ArrayList<String> list = new ArrayList<String>();
////
////
////		for (int i = 0; i < arr.length(); i++) {
////			list.add(arr.getJSONObject(i).getString("iataCode"));
////		}
////
////		System.out.println(list);
//
//		JSONObject obj = new JSONObject(data);
//		JSONArray dataArr=obj.getJSONArray("data");
//
//		ArrayList<String> depList=new ArrayList<String>();
//
//
//		for (int m = 0; m < dataArr.length(); m++) {
//
//			JSONArray offerItemsArr=dataArr.getJSONObject(m).getJSONArray("offerItems");
//
//
//			for (int k = 0; k < offerItemsArr.length(); k++) {
//
//				JSONArray servicesArr=offerItemsArr.getJSONObject(k).getJSONArray("services");
//				JSONObject priceObj=offerItemsArr.getJSONObject(k).getJSONObject("price");
//				Double price=priceObj.getDouble("total");
//
//				for (int j = 0; j < servicesArr.length(); j++) {
//
//					JSONArray segmentsArr=servicesArr.getJSONObject(j).getJSONArray("segments");
//
//					for (int i = 0; i < segmentsArr.length(); i++) {
//
//						JSONObject flightSegmentsObj=segmentsArr.getJSONObject(i).getJSONObject("flightSegment");
//						JSONObject availObj=segmentsArr.getJSONObject(i).getJSONObject("pricingDetailPerAdult");
//
//
//						JSONObject departureObj=flightSegmentsObj.getJSONObject("departure");
//						JSONObject arrivalObj=flightSegmentsObj.getJSONObject("arrival");
//
//						String depIATA=departureObj.getString("iataCode");
//						String arrIATA=arrivalObj.getString("iataCode");
//						Integer availability=availObj.getInt("availability");
//
//						String depTime=departureObj.getString("at");
//						String arrTime=arrivalObj.getString("at");
//
//						depList.add(depIATA);
//
//						System.out.println("from "+depIATA+" at "+depTime+" to "+arrIATA+" at "+arrTime+" | "+availability+" seats available.");
//
//					}
//					System.out.println("-----Trip cost: "+price+"-----");
//					System.out.println("-----------------------------");
//				}
//			}
//		}
	}


	@Bean
	public ViewResolver viewResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setTemplateMode("XHTML");
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".html");

		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver);

		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(engine);
		return viewResolver;
	}
}
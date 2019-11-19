package com.example.javaapp;

import android.text.format.DateUtils;

import com.example.javaapp.models.Event;
import com.example.javaapp.models.Location;
import com.example.javaapp.models.ScrapingResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KreuzerScraper {

    public static ScrapingResult fetchEvents() throws Exception {

        List<Event> localResultEvents = new ArrayList<>();
        List<Location> localResultLocations;

        Map<String, Location> locations = new HashMap<>();

        Pattern patternDate = Pattern.compile("(\\d{2})\\.(\\d{2})\\.");
        Pattern patternTime = Pattern.compile("(\\d{2})\\:(\\d{2})");

        Date today = Calendar.getInstance().getTime();

        Calendar calendarNextMonth = Calendar.getInstance();
        calendarNextMonth.add(Calendar.MONTH, 1);
        Date nextMonth = calendarNextMonth.getTime();

        String thisYear = (new SimpleDateFormat("yyyy")).format(today);

        SimpleDateFormat searchDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        try {
            String url = "https://kreuzer-leipzig.de/termine/"
                    + "?datumVon="
                    + searchDateFormat.format(today)
                    + "&datumBis="
                    + searchDateFormat.format(nextMonth);

            System.out.println(url);

            Document doc = Jsoup.connect(url).get();

            for (Element event : doc.select("article.termin")) {
                Elements titleElement = event.select("h2");
                Elements tagElements = titleElement.select("strong");


                String title;
                List<String> tags = new LinkedList<>();

                if (tagElements.size() > 0) {
                    title = titleElement.first().textNodes().get(tagElements.size()).text().trim();
                    for (Element forElement : tagElements) {
                        tags.add(forElement.text());
                    }
                }
                else {
                    title = event.select("h2").text();
                }

                String subtitle = event.select("p.details").text();
                Elements moreText = event.select(".moreText p");
                String details = moreText.text();

                Element whenAndWhere = event.select(".whenAndWhere").first();

                if (whenAndWhere == null) continue;
                Elements locationTimes = whenAndWhere.children();

                String imageURL = event.select("img").attr("src");
                imageURL = !imageURL.isEmpty() ? "https://kreuzer-leipzig.de" + imageURL : null;

                String category = event.previousElementSiblings().select("aside h3").first().text();

                for (int i = 0; i < locationTimes.size(); i+=2) {

                    Element locationElement = locationTimes.get(i);
                    String locationName = locationElement.text();
                    String locationURL = locationElement.attr("href");

                    Location location = new Location(locationName, locationURL);
                    locations.put(locationName, location);

                    Element timesElement = locationTimes.get(i + 1);

                    for (Element time : timesElement.select("li")) {
                        String timeLabel = time.text();
                        String dateAsString = "";
                        Matcher m = patternDate.matcher(timeLabel);

                        while (m.find()) {
                            dateAsString = m.group() + thisYear;
                        }

                        m = patternTime.matcher(timeLabel);

                        while (m.find()) {
                            Date eventDate = (new SimpleDateFormat("dd.MM.yyyy HH:mm")).parse(dateAsString + " " + m.group());
                            Event newEvent = new Event(title, subtitle, details, eventDate, locationName, tags, imageURL, category);
                            localResultEvents.add(newEvent);
                        }
                    }
                }
            }

            localResultLocations = new ArrayList<>(locations.values());
        }
        catch(Exception e) {
            e.printStackTrace();
            throw(e);
        }

        return new ScrapingResult(localResultEvents, localResultLocations);
    }
}

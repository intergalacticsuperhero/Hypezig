package com.example.javaapp.net;

import android.util.Log;

import com.example.javaapp.models.Event;
import com.example.javaapp.models.ScrapingResult;

import org.jsoup.Connection;
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

import static com.example.javaapp.BaseApplication.LOG_NET;

public class KreuzerScraper {

    private static final String PROVIDER_NAME = "kreuzer-leipzig.de";
    private static final SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat(
            "dd.MM.yyyy HH:mm");
    private static final SimpleDateFormat ID_DATE_FORMAT = new SimpleDateFormat(
            "yyyyMMddHHmm");


    public static ScrapingResult fetchEvents() throws Exception {
        Log.d(LOG_NET, KreuzerScraper.class.getName() + ".fetchEvents() called");

        List<Event> localResultEvents = new ArrayList<>();

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

            Log.i(LOG_NET, "fetchEvents: URL to be read = " + url);

            Connection connection = Jsoup.connect(url);
            connection.maxBodySize(0);
            Document doc = connection.timeout(2 * 60 * 1000).get();

            Elements events = doc.select("article.termin");
            Log.i(LOG_NET, "fetchEvents: " + events.size() + " events found.");

            int eventCounter = 1;

            for (Element event : events) {
                Log.d(LOG_NET, "fetchEvents: Processing event " + eventCounter + "...");

                eventCounter++;
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

                if (whenAndWhere == null) {
                    Exception e = new Exception("No .whenAndWhere found");

                    Log.e(LOG_NET, "fetchEvents: No .whenAndWhere found!", e);
                    Log.i(LOG_NET, "fetchEvents: current event: " + event);

                    throw (e);
                }
                Elements locationTimes = whenAndWhere.children();

                String imageURL = event.select("img").attr("src");
                imageURL = !imageURL.isEmpty() ? "https://kreuzer-leipzig.de" + imageURL : null;

                String category = event.previousElementSiblings().select("aside h3").first().text();

                for (int i = 0; i < locationTimes.size(); i+=2) {

                    Element locationElement = locationTimes.get(i);
                    String locationName = locationElement.text();
                    String locationURL = locationElement.attr("href");

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
                            Date eventDate = INPUT_DATE_FORMAT.parse(dateAsString + " "
                                    + m.group());

                            String providerId = event.attr("id") + "_"
                                    + ID_DATE_FORMAT.format(eventDate);

                            Event newEvent = new Event(title, subtitle, details, eventDate,
                                    locationName, tags, imageURL, category, PROVIDER_NAME,
                                    providerId, category);

                            newEvent.locationURL = locationURL.isEmpty() ? null :
                                    "https://kreuzer-leipzig.de" + locationURL;
                            newEvent.eventURL = "https://kreuzer-leipzig.de/termine";

                            Log.d(LOG_NET, "fetchEvents: new event created: " + newEvent);

                            localResultEvents.add(newEvent);
                        }
                    }
                }
            }

            Log.i(LOG_NET, "fetchEvents: Import complete");
        }
        catch(Exception e) {
            Log.e(LOG_NET, "fetchEvents: ", e);
            throw(e);
        }

        return new ScrapingResult(localResultEvents);
    }
}

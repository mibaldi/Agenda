package mycalendar;

import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Asynchronously insert a new calendar.
 *
 * @author Yaniv Inbar
 */
public class events extends CalendarAsyncTask {

    private final String calendarId;

    public events(CalendarSampleActivity calendarSample, CalendarInfo calendarInfo) {
        super(calendarSample);
        calendarId = calendarInfo.id;
    }

    @Override
    protected void doInBackground() throws IOException {
        //Ver lista de eventos de un calendario
        /*
        String pageToken = null;
        do {
            Events events = client.events().list(calendarId).setPageToken(pageToken).execute();
            List<Event> items = events.getItems();
            for (Event event : items) {
                String email=event.getSummary();

                System.out.println(event.getSummary());

            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
        */
        Event event= new Event();
        event.setSummary("prueba");
        event.setLocation("Donostia");
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime()+3600000);
        DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC+1"));
        event.setStart(new EventDateTime().setDateTime(start));
        DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC+1"));
        event.setEnd(new EventDateTime().setDateTime(end));
        Event createdEvent = client.events().insert(calendarId, event).execute();
    }
}
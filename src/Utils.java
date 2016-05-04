import java.util.Calendar;

/**
 * Created by Tomasz Pisarek on 04/05/2016.
 *
 * Utils
 *
 */

public class Utils {

    /**
     * Return date of Next Day of Week
     */
    static Calendar quessDateOfNextDayOfWeek(int dayOfWeek){
        Calendar date = Calendar.getInstance();
        int diff = dayOfWeek - date.get(Calendar.DAY_OF_WEEK);
        if (!(diff > 0)) {
            diff += 7;
        }
        date.add(Calendar.DAY_OF_MONTH, diff);
        return date;
    }

    /**
     * Create Ad
     */
    static Ad createAd(String aReference, String aContent, String aContact){
        Ad ad = new Ad(aReference,aContent,aContact);
        return ad;
    }
}

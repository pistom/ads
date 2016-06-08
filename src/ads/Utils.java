package ads;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

/**
 * Created by Tomasz Pisarek on 04/05/2016.
 *
 * ads.Utils
 *
 */

public class Utils {

    /**
     * Return date of Next Day of Week
     */
    static Calendar guessDateOfNextDayOfWeek(int dayOfWeek){
        Calendar date = Calendar.getInstance();
        int diff = dayOfWeek - date.get(Calendar.DAY_OF_WEEK);
        if (!(diff > 0)) {
            diff += 7;
        }
        date.add(Calendar.DAY_OF_MONTH, diff);
        return date;
    }


    /**
     * Create ads.Ad
     */
    static Ad createAd(Ad.Category aCategory, String aReference, String aContent){
        Ad ad = new Ad(aCategory,aReference,aContent);
        return ad;
    }
}

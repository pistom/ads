package ads;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;
import java.util.ArrayList;

/**
 * Created by Tomasz Pisarek on 04/05/2016.
 *
 * ads.Edition of magazine
 *
 */
public class Edition implements Serializable {
    private int editionNumber;                      // ads.Edition number
    private Calendar editionDate;                   // Date of edition issue
    private ArrayList<Ad> ads = new ArrayList<Ad>();// ads.Edition Ads

    /**
     * Initialize new ads.Edition
     */

    Edition(){
        this.editionDate = Utils.guessDateOfNextDayOfWeek(Calendar.SUNDAY);
    }

    Edition(int edNumber){
        this.editionNumber = edNumber;
        this.editionDate = Utils.guessDateOfNextDayOfWeek(Calendar.SUNDAY);
    }

    Edition(int edNumber, String edDate) throws ParseException {
        this.editionNumber = edNumber;
        setEditionDate(edDate);
    }

    public void setEditionNumber(int edNumber){
        this.editionNumber = edNumber;
    }

    public int getEditionNumber(){
        return editionNumber;
    }

    /**
     * Set the date of edition issue
     */
    public void setEditionDate(String edDate) throws ParseException {
        this.editionDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
        this.editionDate.setTime(sdf.parse(edDate));
    }

    /**
     * Set the date of edition issue
     */
    public String getEditionDateAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(editionDate.getTime());
    }
    public LocalDate getEditionDateAsLocalDate() {
        LocalDate ld = editionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ld;
    }

    /**
     * Return the number of ads
     */
    public int getAdsNumber(){
        return ads.size();
    }

    /**
     * Print Date of ads.Edition
     */
    public String printEditionDate(){
        Calendar edDate = editionDate;
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        return format.format(edDate.getTime());
    }

    /**
     * Adding Ads
     */
    public void addAd(Ad ad) {
        ads.add(ad);
    }

    public String printAdsList(){
        return ads.toString();
    }

    public String printAdsItem(int i){
        String aReference = ads.get(i).getReference();
        String aContent = ads.get(i).getContent();
        String aContact = ads.get(i).getContact();
        return aReference+": "+aContent+" - Kontakt: "+aContact;
    }
}
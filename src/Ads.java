import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Tomasz Pisarek on 04/05/2016.
 * Program to manage ads on the website www.glos-katolicki.eu
 *
 */
public class Ads {

    public static void main(String[] args) throws ParseException {


        Edition ed = new Edition(17);
        System.out.println(ed.printEditionDate()+" (numer "+ed.getEditionNumber()+"), Liczba ogłoszeń: "+ed.getAdsNumber());

        Ad ad1 = Utils.createAd(Ad.Category.SzukamPracy, "1602","Ogłoszenie szukam pracy","739 062 332");
        Ad ad2 = new Ad(Ad.Category.SzukamMieszkania, "2209","Treść innego ogłoszenia","tom.pis@hotmail.com");
        Ad ad3 = new Ad();
        ad3.setCategory(Ad.Category.values()[2]);
        ed.addAd(ad1);
        ed.addAd(ad2);
        ed.addAd(ad3);

        System.out.println(ed.printAdsList());

    }
}

import java.text.ParseException;

/**
 * Created by Tomasz Pisarek on 04/05/2016.
 * Program to manage ads on the website www.glos-katolicki.eu
 *
 */
public class Ads {
    public static void main(String[] args) throws ParseException {
        Edition ed = new Edition(17);
        System.out.println(ed.printEditionDate()+" (numer "+ed.getEditionNumber()+"), Liczba ogłoszeń: "+ed.getAdsNumber());
        ed.setEditionDate("2017-02-22");
        ed.setEditionNumber(30);
        Ad ad = Utils.createAd("referencja 1","treść ogłoszenia 1","namiary 1");
        ed.addAd(ad);
        Ad ad2 = new Ad("referencja 2","treść ogłoszenia 2","namiary 2");
        ed.addAd(ad2);
        for (int i=0; i<ed.getAdsNumber(); i++){
            System.out.println(ed.printAdsItem(i));
        }

        System.out.println(ed.printEditionDate()+" (numer "+ed.getEditionNumber()+"), Liczba ogłoszeń: "+ed.getAdsNumber());
    }
}

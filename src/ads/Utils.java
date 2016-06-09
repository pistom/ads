package ads;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
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

    /**
     * Generate XML from Edition
     */
    static boolean generateXML(Edition ed, String currentDirectory){

        boolean success = false;

        int categoryNumber = Ad.Category.values().length;
        ArrayList<ArrayList<Ad>> ads = new ArrayList<ArrayList<Ad>>();

        for (int i=0; i<categoryNumber; i++) {
            ArrayList<Ad> init = new ArrayList<>();
            ads.add(init);
        }

        for (int i=0; i<ed.ads.size(); i++) {
            ads.get(ed.ads.get(i).getCategory().ordinal()).add(ed.ads.get(i));
        }

        for (int i=0; i<ads.size(); i++) {
            for (int j=0; j<ads.get(i).size(); j++) {
                //System.out.println(ads.get(i).get(j).toString());
            }
        }


        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("wkladka");
            Attr dateAttr = doc.createAttribute("niedziela");
            dateAttr.setValue(ed.getEditionDateAsLocalDate().toString());
            rootElement.setAttributeNode(dateAttr);
            doc.appendChild(rootElement);

            for (int i=0; i<ads.size(); i++) {
                if (ads.get(i).size() > 0) {
                    Element category = doc.createElement("ogloszenia");
                    rootElement.appendChild(category);
                    category.setAttribute("dzial", ads.get(i).get(0).getCategoryName(false));
                    for (int j = 0; j < ads.get(i).size(); j++) {
                        Element item = doc.createElement("ogloszenie");
                        item.appendChild(doc.createTextNode(
                                ads.get(i).get(j).toString()
                        ));
                        category.appendChild(item);
                    }
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            String filePath = currentDirectory + File.separator + ed.getEditionDateAsLocalDate().toString() + ".xml";

            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
            success = true;

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

        return success;
    }

    static boolean uploadToFTP(String filename){
        boolean success = false;

        return success;
    }
}

package ads;

import java.io.Serializable;

/**
 * Created by Tomasz Pisarek on 04/05/2016.
 * ads.Ad in the magazin
 */
public class Ad implements Serializable {
    public enum Category {
        Szukam_Pracy, Oferty_Pracy, Szukam_Mieszkania, Wynajem_Mieszkania, Usługi, Inne
    }

    private int id;
    private String reference;
    private String content;
    private Category category;
    Ad(int aId){
        setId(aId);
        setCategory(Category.Inne);
        setReference("");
        setContent("");
    }
    Ad(int aId, Category aCategory, String aReference, String aContent){
        setId(aId);
        setCategory(aCategory);
        setReference(aReference);
        setContent(aContent);
    }

    public String toString(){
        return reference+" "+content;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category cat) {
        this.category = cat;
    }


    public String getCategoryName(boolean abbreviation) {
        String c = "";
        switch (category){
            case Szukam_Pracy:
                c = abbreviation ? "SP" : "Szukam Pracy";
                break;
            case Oferty_Pracy:
                c = abbreviation ? "OP" : "Oferty Pracy";
                break;
            case Szukam_Mieszkania:
                c = abbreviation ? "SM" : "Szukam Mieszkania";
                break;
            case Wynajem_Mieszkania:
                c = abbreviation ? "WM" : "Wynajem Mieszkania";
                break;
            case Usługi:
                c = abbreviation ? "US" : "Usługi";
                break;
            case Inne:
                c = abbreviation ? "IN" : "Inne";
                break;
        }
        return c;
    }

    public int getId(){
        return id;
    }
    public void setId(int aId){
        this.id = aId;
    }

}

package ads;

import java.io.Serializable;

/**
 * Created by Tomasz Pisarek on 04/05/2016.
 * ads.Ad in the magazin
 */
public class Ad implements Serializable {
    private String reference;
    private String content;
    public enum Category {
        Szukam_Pracy, Oferty_Pracy, Szukam_Mieszkania, Wynajem_Mieszkania, Usługi, Inne
    }

    private Category category;
    Ad(){
        setCategory(Category.Inne);
        setReference("");
        setContent("");
    }
    Ad(Category aCategory, String aReference, String aContent){
        setCategory(aCategory);
        setReference(aReference);
        setContent(aContent);
    }

    public String toString(){
        return printCategory()+": (ref. "+reference+") "+content;
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

    public String printCategory() {
        return category.toString();
    }

}

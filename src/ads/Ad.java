package ads;

/**
 * Created by Tomasz Pisarek on 04/05/2016.
 * ads.Ad in the magazin
 */
public class Ad {
    private String reference;
    private String content;
    private String contact;
    public enum Category {
        SzukamPracy, OfertyPracy, SzukamMieszkania, WynajemMieszkania, Uslugi, Inne
    }

    private Category category;
    Ad(){
        setCategory(Category.Inne);
        setReference("");
        setContent("");
        setContact("");
    }
    Ad(Category aCategory, String aReference, String aContent, String aContact){
        setCategory(aCategory);
        setReference(aReference);
        setContent(aContent);
        setContact(aContact);
    }

    public String toString(){
        return printCategory()+": (ref. "+reference+") "+content+" - Kontakt: "+contact;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

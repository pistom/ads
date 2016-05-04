/**
 * Created by Tomasz Pisarek on 04/05/2016.
 * Ad in the magazin
 */
public class Ad {
    private String reference;
    private String content;
    private String contact;
    private String category;

    Ad(){
        setReference("");
        setContent("");
        setContact("");
    }
    Ad(String aReference, String aContent, String aContact){
        setReference(aReference);
        setContent(aContent);
        setContact(aContact);
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

}

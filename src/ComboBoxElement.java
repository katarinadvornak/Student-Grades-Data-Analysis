package src;

public class ComboBoxElement {
    private String displayName;
    private String id;

    public ComboBoxElement(String _displayname, String _id){
        displayName=_displayname;
        id=_id;
    }
    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }
}

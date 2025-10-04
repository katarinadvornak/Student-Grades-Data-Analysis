package src;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ListCheckboxElement {
    private String label;
    private BooleanProperty selected;

    public ListCheckboxElement(String label) {
        this.label = label;
        this.selected = new SimpleBooleanProperty(false);
    }

    public String getName() {
        return label;
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
    public void setSelected(boolean selected) { this.selected.set(selected); }
    public boolean isSelected() { return selected.get(); }



}

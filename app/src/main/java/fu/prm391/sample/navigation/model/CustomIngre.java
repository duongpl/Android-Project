package fu.prm391.sample.navigation.model;

public class CustomIngre {
    private String id;
    private String ingreid;
    private String amount;

    public CustomIngre(String id, String ingreid, String amount) {
        this.id = id;
        this.ingreid = ingreid;
        this.amount = amount;
    }
    public CustomIngre(){

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIngreid() {
        return ingreid;
    }

    public void setIngreid(String ingreid) {
        this.ingreid = ingreid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

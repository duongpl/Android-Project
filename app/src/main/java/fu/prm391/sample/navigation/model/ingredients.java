package fu.prm391.sample.navigation.model;

import java.io.Serializable;

public class ingredients implements Serializable {
    private String id;
    private String name;
    private String img;
    private String amount;

    public ingredients(String id, String name, String img,String amount) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.amount = amount;
    }

    public ingredients(){

    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

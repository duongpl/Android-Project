package fu.prm391.sample.navigation.model;

import java.io.Serializable;

public class Food implements Serializable {
    private String id;
    private String name;
    private String img;
    private String category_type;

    public Food(){}

    public Food(String id, String name, String img, String category_type) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.category_type = category_type;
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

    public String getCategory_type() {
        return category_type;
    }

    public void setCategory_type(String category_type) {
        this.category_type = category_type;
    }
}

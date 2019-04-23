package objects;

public class BasketItem {
    String name, addedBy;
    double price;
    int id;
    int quan;


    public BasketItem()
    {}

    public BasketItem(int id, String name, String addedBy, double price, int quan)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quan= quan;
        this.addedBy = addedBy;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }
       public double getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public int getQ() {
        return quan;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setQ(int quan) {
        this.quan = quan;
    }


}
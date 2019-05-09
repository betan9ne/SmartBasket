package objects;

public class BasketItem {
    String name, addedBy, list_id;
    double price;
    int id, status;
    int quan;

    public BasketItem(){}

    public BasketItem(int id, String name, String list_id,  String addedBy, double price, int quan, int status)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quan= quan;
        this.addedBy = addedBy;
        this.list_id = list_id;
        this.status = status;
    }

    public int getQuan() {
        return quan;
    }

    public int getStatus() {
        return status;
    }

    public void setQuan(int quan) {
        this.quan = quan;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public String getList_id() {
        return list_id;
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
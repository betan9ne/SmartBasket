package objects;

public class PartnersItem {
    int id;
    int status;
   String f_name;
   String name;
   String photo;

    public PartnersItem(){}
    public PartnersItem(int id, String name, String f_name, String photo,  int status)
    {
        this.id = id;
        this.name = name;
        this.f_name = f_name;
        this.status= status;
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public String getF_name() {
        return f_name;
    }

    public String getName() {
        return name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

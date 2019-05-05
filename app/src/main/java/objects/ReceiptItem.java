package objects;

public class ReceiptItem {
    private int  id, u_id, list_id;
    private String receipt, created, descr, title;

    public ReceiptItem()
    {}
    public ReceiptItem(int id, int u_id, int list_id, String receipt, String created, String descr, String title) {
        super();
        this.id = id;
        this.u_id = u_id;
        this.list_id = list_id;
        this.receipt = receipt;
        this.created = created;
        this.descr = descr;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public int getList_id() {
        return list_id;
    }

    public int getU_id() {
        return u_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceipt() {
        return receipt;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDescr() {
        return descr;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }


}

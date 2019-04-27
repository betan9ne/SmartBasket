package objects;

public class ProductItem {
	private int  id, user_id, item_count;
	private String name, f_name;

	public ProductItem()
	{}
	public ProductItem(int id, int user_id, int item_count, String name, String f_name) {
		super();
		this.id = id;
		this.user_id = user_id;
		 this.name = name;
		 this.item_count = item_count;
		 this.f_name = f_name;
	}

	public void setF_name(String f_name) {
		this.f_name = f_name;
	}

	public String getF_name() {
		return f_name;
	}

	public int getItem_count() {
		return item_count;
	}

	public void setItem_count(int item_count) {
		this.item_count = item_count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}


	public void setName(String name) {
		this.name = name;
	}


}

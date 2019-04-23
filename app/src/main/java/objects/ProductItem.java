package objects;

public class ProductItem {
	private int  id, user_id, item_count;
	private String name;

	public ProductItem()
	{}
	public ProductItem(int id, int user_id, int item_count, String name) {
		super();
		this.id = id;
		this.user_id = user_id;
		 this.name = name;
		 this.item_count = item_count;
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

package model;

public class CreateUserResponse {
	private int code;
	private String meta;
	private User data;
	public CreateUserResponse(int code, String meta, User data) {
		super();
		this.code = code;
		this.meta = meta;
		this.data = data;
	}
	public CreateUserResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMeta() {
		return meta;
	}
	public void setMeta(String meta) {
		this.meta = meta;
	}
	public User getData() {
		return data;
	}
	public void setData(User data) {
		this.data = data;
	}
	
	
}

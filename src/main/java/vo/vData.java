package vo;

public class vData {
	private Integer id;
	private String dId;
	private String url;
	private String typeName;
	private Integer downloadTotalTime;
	private String Provider;
	private Integer price;
	private Integer searchedTime;
	private Integer visitedTime;

	public Integer getVisitedTime() {
		return visitedTime;
	}

	public void setVisitedTime(Integer visitedTime) {
		this.visitedTime = visitedTime;
	}

	public Integer getSearchedTime() {
		return searchedTime;
	}

	public void setSearchedTime(Integer searchedTime) {
		this.searchedTime = searchedTime;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getProvider() {
		return Provider;
	}
	public void setProvider(String provider) {
		Provider = provider;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getdId() {
		return dId;
	}
	public void setdId(String dId) {
		this.dId = dId;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getDownloadTotalTime() {
		return downloadTotalTime;
	}
	public void setDownloadTotalTime(Integer downloadTotalTime) {
		this.downloadTotalTime = downloadTotalTime;
	}
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	private String registerDate;
	private String updateDate;
	
}

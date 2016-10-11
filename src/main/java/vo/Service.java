package vo;
public class Service {
	
	private String surl;
	private String registerDate;
	private String updateDate;
	private String serviceType;
	private Integer Id;
	private String sId;
	private Integer visiteTotalTime;
	private String isNew;
	private String provider;
	private Integer searchedTime;
	private String sname;
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public Integer getSearchedTime() {
		return searchedTime;
	}
	public void setSearchedTime(Integer searchedTime) {
		this.searchedTime = searchedTime;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getsId() {
		return sId;
	}
	public void setsId(String sId) {
		this.sId = sId;
	}
	
	public String getSurl() {
		return surl;
	}
	public void setSurl(String surl) {
		this.surl = surl;
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
	
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public Integer getVisiteTotalTime() {
		return visiteTotalTime;
	}
	public void setVisiteTotalTime(Integer visiteTotalTime) {
		this.visiteTotalTime = visiteTotalTime;
	}
}

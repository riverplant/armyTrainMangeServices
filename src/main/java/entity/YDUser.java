package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "YDUser")
public class YDUser implements Serializable{
	private static final long serialVersionUID = 293121694505183671L;
	private Integer id;
	private String uid;
	//private UserType userType;
	private Integer visitServiceTimes;
	private Integer visitAtlasTimes;
	private Integer downloadAtlasTime;
	private Integer searchTime;
	private Integer visitDataTimes;
	private Integer visitAppTimes;
	private Integer visitProductionTimes;
	private Integer visitPageTime;
	private String uname;
	private UserType userType;
	
	@Column(name = "VISITESERVICETIME")
	public Integer getVisitServiceTimes() {
		return visitServiceTimes;
	}

	public void setVisitServiceTimes(Integer visitServiceTimes) {
		this.visitServiceTimes = visitServiceTimes;
	}
	
	@Column(name = "VISITEATLASTIME")
	public Integer getVisitAtlasTimes() {
		return visitAtlasTimes;
	}

	public void setVisitAtlasTimes(Integer visitAtlasTimes) {
		this.visitAtlasTimes = visitAtlasTimes;
	}
	@Column(name = "DOWNLOADATLASTIME")
	public Integer getDownloadAtlasTime() {
		return downloadAtlasTime;
	}

	public void setDownloadAtlasTime(Integer downloadAtlasTime) {
		this.downloadAtlasTime = downloadAtlasTime;
	}
	
	@Column(name = "VISITDATATIME")
	public Integer getVisitDataTimes() {
		return visitDataTimes;
	}

	public void setVisitDataTimes(Integer visitDataTimes) {
		this.visitDataTimes = visitDataTimes;
	}
	
	@Column(name = "VISITAPPTIME")
	public Integer getVisitAppTimes() {
		return visitAppTimes;
	}

	public void setVisitAppTimes(Integer visitAppTimes) {
		this.visitAppTimes = visitAppTimes;
	}
	
	@Column(name = "VISITPRODUCTIONTIME")
	public Integer getVisitProductionTimes() {
		return visitProductionTimes;
	}

	public void setVisitProductionTimes(Integer visitProductionTimes) {
		this.visitProductionTimes = visitProductionTimes;
	}
	
	@Column(name = "SEARCHTIME")
	public Integer getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(Integer searchTime) {
		this.searchTime = searchTime;
	}

	@ManyToOne(cascade={CascadeType.ALL}) 
	@JoinColumn(name = "UT_ID", updatable = true)
public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}	
	
	private Date registerDate;
	private Date updateDate;
	

	private String UserIp;
	@Column(name = "USERIP")
	public String getUserIp() {
		return UserIp;
	}

	public void setUserIp(String userIp) {
		UserIp = userIp;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateDate", length = 32)
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	private Date loginDate;
	private String password;
	private Set<YDData> ydDatas = new HashSet<YDData>(0);
	private Set<YDService> ydService = new HashSet<YDService>(0);
	@OneToMany(targetEntity = YDService.class,fetch = FetchType.LAZY, orphanRemoval = true, cascade={CascadeType.ALL})
	@JoinColumn(name = "ServiceProvider_ID")
	public Set<YDService> getYdService() {
		return ydService;
	}
	public void setYdService(Set<YDService> ydService) {
		this.ydService = ydService;
	}
	@OneToMany(targetEntity = YDData.class,fetch = FetchType.LAZY, orphanRemoval = true, cascade={CascadeType.ALL})
	@JoinColumn(name = "Provider_ID")
	public Set<YDData> getYdDatas() {
		return ydDatas;
	}
	public void setYdDatas(Set<YDData> ydDatas) {
		this.ydDatas = ydDatas;
	}
	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "registerDate", length = 32)
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "loginDate", length = 32)
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	@Column(name = "UNAME")
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

	@Column(name = "VISITEPAGETIME")
	public Integer getVisitPageTime() {
		return visitPageTime;
	}
	public void setVisitPageTime(Integer visitPageTime) {
		this.visitPageTime = visitPageTime;
	}
	

	@Id
	@GenericGenerator(name = "generator", strategy = "increment")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", length = 11)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "UID", length = 516)
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
}

package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
//一般使用javax.persisitence
@Table(name = "YDService")
public class YDService  implements Serializable{
private static final long serialVersionUID = 2846809701154825293L;
private Integer id;
private String sid;
private String surl;
private Date registerDate;
private Date updateDate;
private YDUser provider;
private String sName;
private Integer searchedTime;
@Column(name = "SEARCHEDTIME" )
public Integer getSearchedTime() {
	return searchedTime;
}

public void setSearchedTime(Integer searchedTime) {
	this.searchedTime = searchedTime;
}
@Column(name = "SERVICENAME", length = 128)
public String getsName() {
	return sName;
}

public void setsName(String sName) {
	this.sName = sName;
}

//private ServiceType serviceType;
private String isNew;
@Column(name = "ISNEW", length = 11)
public String getIsNew() {
	return isNew;
}

public void setIsNew(String isNew) {
	this.isNew = isNew;
}


@ManyToOne(cascade={CascadeType.ALL}) 
@JoinColumn(name = "Provider_ID", updatable = true)
public YDUser getProvider() {
	return provider;
}

public void setProvider(YDUser provider) {
	this.provider = provider;
}

private ServiceType serviceType;


@ManyToOne(cascade={CascadeType.ALL}) 
@JoinColumn(name = "ST_ID" , updatable = true)
public ServiceType getServiceType() {
	return serviceType;
}
public void setServiceType(ServiceType serviceType) {
	this.serviceType = serviceType;
}
public static long getSerialversionuid() {
	return serialVersionUID;
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
@Column(name = "updateDate", length = 32)
public Date getUpdateDate() {
	return updateDate;
}
public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
}

private Integer visitedTime;
@Column(name = "VISITEDTIME", length = 11)
public Integer getVisitedTime() {
	return visitedTime;
}
public void setVisitedTime(Integer visitedTime) {
	this.visitedTime = visitedTime;
}
@Column(name = "SURL", length = 512)
public String getSurl() {
	return surl;
}
public void setSurl(String surl) {
	this.surl = surl;
}
@Column(name = "SID", length = 256)
public String getSid() {
	return sid;
}
public void setSid(String sid) {
	this.sid = sid;
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
}

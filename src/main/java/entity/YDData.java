package entity;

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
@Table(name = "YDData")
public class YDData {
private Integer id;
private String did;
private Integer price;
private Integer downloadTimes;
private Integer visitedTimes;
private String dUrl;
private Date registerDate;
private Date updateDate;
private DataType dataType;
private YDUser provider;
private String name;
private String isNew;
private Integer searchedTime;
@Column(name = "SEARCHEDTIME" )
public Integer getSearchedTime() {
	return searchedTime;
}

public void setSearchedTime(Integer searchedTime) {
	this.searchedTime = searchedTime;
}

@Column(name = "ISNEW", length = 11)
public String getIsNew() {
	return isNew;
}
public void setIsNew(String isNew) {
	this.isNew = isNew;
}
@Column(name = "VISITEDTIMES")
public Integer getVisitedTimes() {
	return visitedTimes;
}
public void setVisitedTimes(Integer visitedTimes) {
	this.visitedTimes = visitedTimes;
}
@Column(name = "NAME", length =128)
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
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
@ManyToOne(cascade={CascadeType.ALL}) 
@JoinColumn(name = "DT_ID" , updatable = true)
public DataType getDataType() {
	return dataType;
}
public void setDataType(DataType dataType) {
	this.dataType = dataType;
}
@Column(name = "DURL", length =512)
public String getdUrl() {
	return dUrl;
}
public void setdUrl(String dUrl) {
	this.dUrl = dUrl;
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
@Column(name = "DID", length = 128)
public String getDid() {
	return did;
}
public void setDid(String did) {
	this.did = did;
}
@Column(name = "PRICE", length =512)
public Integer getPrice() {
	return price;
}
public void setPrice(Integer price) {
	this.price = price;
}
@Column(name = "DOWNLOADTIMES")
public Integer getDownloadTimes() {
	return downloadTimes;
}
@ManyToOne(cascade={CascadeType.ALL}) 
@JoinColumn(name = "Provider_ID", updatable = true)
public YDUser getProvider() {
	return provider;
}
public void setProvider(YDUser provider) {
	this.provider = provider;
}
public void setDownloadTimes(Integer downloadTimes) {
	this.downloadTimes = downloadTimes;
}

}

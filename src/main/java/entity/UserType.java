package entity;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
/**
 * 服务级别
 * @author riverplant
 *
 */
@Entity
@Table(name = "UserType")
public class UserType {
	private Integer id;
	private String name;
	private Set<YDUser> ydVisiters = new  HashSet<YDUser>(0);
	private Date registerDate;
	private Date updateDate;
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
	@Column(name = "NAME", length = 32)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@OneToMany(targetEntity = YDUser.class,fetch = FetchType.LAZY, orphanRemoval = true,cascade={CascadeType.ALL})
	@JoinColumn(name = "UT_ID")
	@JsonIgnore
	public Set<YDUser> getYdVisiters() {
		return ydVisiters;
	}
	public void setYdVisiters(Set<YDUser> ydVisiters) {
		this.ydVisiters = ydVisiters;
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
}

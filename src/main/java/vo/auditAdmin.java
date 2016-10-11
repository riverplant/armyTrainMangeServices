package vo;

public class auditAdmin {
private String username;
private String passwd;
private String login_ip;
private String login_time;
public String getLogin_ip() {
	return login_ip;
}
public void setLogin_ip(String login_ip) {
	this.login_ip = login_ip;
}
public String getLogin_time() {
	return login_time;
}
public void setLogin_time(String login_time) {
	this.login_time = login_time;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getPasswd() {
	return passwd;
}
public void setPasswd(String passwd) {
	this.passwd = passwd;
}
}

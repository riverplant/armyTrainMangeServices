package util;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

public class YDCronTrigger extends CronTriggerFactoryBean {

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		super.setName(name);
	}

	@Override
	public void setGroup(String group) {
		// TODO Auto-generated method stub
		super.setGroup(group);
	}

	@Override
	public void setJobDetail(JobDetail jobDetail) {
		// TODO Auto-generated method stub
		super.setJobDetail(jobDetail);
	}

	@Override
	public void setJobDataMap(JobDataMap jobDataMap) {
		// TODO Auto-generated method stub
		super.setJobDataMap(jobDataMap);
	}

	@Override
	public JobDataMap getJobDataMap() {
		// TODO Auto-generated method stub
		return super.getJobDataMap();
	}

	@Override
	public void setJobDataAsMap(Map<String, ?> jobDataAsMap) {
		// TODO Auto-generated method stub
		super.setJobDataAsMap(jobDataAsMap);
	}

	@Override
	public void setStartTime(Date startTime) {
		// TODO Auto-generated method stub
		super.setStartTime(startTime);
	}

	@Override
	public void setStartDelay(long startDelay) {
		// TODO Auto-generated method stub
		super.setStartDelay(startDelay);
	}

	@Override
	public void setCronExpression(String cronExpression) {
		// TODO Auto-generated method stub
		super.setCronExpression(cronExpression);
	}

	@Override
	public void setTimeZone(TimeZone timeZone) {
		// TODO Auto-generated method stub
		super.setTimeZone(timeZone);
	}

	@Override
	public void setCalendarName(String calendarName) {
		// TODO Auto-generated method stub
		super.setCalendarName(calendarName);
	}

	@Override
	public void setPriority(int priority) {
		// TODO Auto-generated method stub
		super.setPriority(priority);
	}

	@Override
	public void setMisfireInstruction(int misfireInstruction) {
		// TODO Auto-generated method stub
		super.setMisfireInstruction(misfireInstruction);
	}

	@Override
	public void setMisfireInstructionName(String constantName) {
		// TODO Auto-generated method stub
		super.setMisfireInstructionName(constantName);
	}

	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		super.setDescription(description);
	}

	@Override
	public void setBeanName(String beanName) {
		// TODO Auto-generated method stub
		super.setBeanName(beanName);
	}

	@Override
	public void afterPropertiesSet() throws ParseException {
		// TODO Auto-generated method stub
		super.afterPropertiesSet();
	}

	@Override
	public CronTrigger getObject() {
		// TODO Auto-generated method stub
		return super.getObject();
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return super.getObjectType();
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return super.isSingleton();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

}

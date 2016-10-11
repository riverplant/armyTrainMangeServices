package util;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.quartz.Calendar;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.quartz.spi.JobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

public class quartzTest extends SchedulerFactoryBean {

	
	@Override
	public void start() throws SchedulingException {
		// TODO Auto-generated method stub
		super.start();
	}

	@Override
	public void setSchedulerFactoryClass(
			Class<? extends SchedulerFactory> schedulerFactoryClass) {
		// TODO Auto-generated method stub
		super.setSchedulerFactoryClass(schedulerFactoryClass);
	}

	@Override
	public void setConfigLocation(Resource configLocation) {
		// TODO Auto-generated method stub
		super.setConfigLocation(configLocation);
	}

	
	@Override
	public void setDataSource(DataSource dataSource) {
		// TODO Auto-generated method stub
		super.setDataSource(dataSource);
	}

	
	@Override
	public void setNonTransactionalDataSource(
			DataSource nonTransactionalDataSource) {
		// TODO Auto-generated method stub
		super.setNonTransactionalDataSource(nonTransactionalDataSource);
	}

	
	@Override
	public void setSchedulerContextAsMap(Map<String, ?> schedulerContextAsMap) {
		// TODO Auto-generated method stub
		super.setSchedulerContextAsMap(schedulerContextAsMap);
	}

	@Override
	public void setApplicationContextSchedulerContextKey(
			String applicationContextSchedulerContextKey) {
		// TODO Auto-generated method stub
		super.setApplicationContextSchedulerContextKey(applicationContextSchedulerContextKey);
	}

	
	@Override
	public void setJobFactory(JobFactory jobFactory) {
		// TODO Auto-generated method stub
		super.setJobFactory(jobFactory);
	}

	@Override
	public void setAutoStartup(boolean autoStartup) {
		// TODO Auto-generated method stub
		super.setAutoStartup(autoStartup);
	}

	@Override
	public boolean isAutoStartup() {
		// TODO Auto-generated method stub
		return super.isAutoStartup();
	}

	@Override
	public void setPhase(int phase) {
		// TODO Auto-generated method stub
		super.setPhase(phase);
	}

	@Override
	public int getPhase() {
		// TODO Auto-generated method stub
		return super.getPhase();
	}

	@Override
	public void setExposeSchedulerInRepository(
			boolean exposeSchedulerInRepository) {
		// TODO Auto-generated method stub
		super.setExposeSchedulerInRepository(exposeSchedulerInRepository);
	}

	@Override
	public void setBeanName(String name) {
		// TODO Auto-generated method stub
		super.setBeanName(name);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		// TODO Auto-generated method stub
		super.setApplicationContext(applicationContext);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		super.afterPropertiesSet();
	}

	@Override
	protected Scheduler createScheduler(SchedulerFactory schedulerFactory,
			String schedulerName) throws SchedulerException {
		// TODO Auto-generated method stub
		return super.createScheduler(schedulerFactory, schedulerName);
	}

	@Override
	public Scheduler getScheduler() {
		// TODO Auto-generated method stub
		return super.getScheduler();
	}

	@Override
	public Scheduler getObject() {
		// TODO Auto-generated method stub
		return super.getObject();
	}

	@Override
	public Class<? extends Scheduler> getObjectType() {
		// TODO Auto-generated method stub
		return super.getObjectType();
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return super.isSingleton();
	}

	@Override
	public boolean isRunning() throws SchedulingException {
		// TODO Auto-generated method stub
		return super.isRunning();
	}

	@Override
	public void destroy() throws SchedulerException {
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	public void setOverwriteExistingJobs(boolean overwriteExistingJobs) {
		// TODO Auto-generated method stub
		super.setOverwriteExistingJobs(overwriteExistingJobs);
	}

	@Override
	public void setJobSchedulingDataLocation(String jobSchedulingDataLocation) {
		// TODO Auto-generated method stub
		super.setJobSchedulingDataLocation(jobSchedulingDataLocation);
	}

	@Override
	public void setJobSchedulingDataLocations(
			String... jobSchedulingDataLocations) {
		// TODO Auto-generated method stub
		super.setJobSchedulingDataLocations(jobSchedulingDataLocations);
	}

	@Override
	public void setJobDetails(JobDetail... jobDetails) {
		// TODO Auto-generated method stub
		super.setJobDetails(jobDetails);
	}

	@Override
	public void setCalendars(Map<String, Calendar> calendars) {
		// TODO Auto-generated method stub
		super.setCalendars(calendars);
	}

	@Override
	public void setTriggers(Trigger... triggers) {
		// TODO Auto-generated method stub
		super.setTriggers(triggers);
	}

	@Override
	public void setSchedulerListeners(SchedulerListener... schedulerListeners) {
		// TODO Auto-generated method stub
		super.setSchedulerListeners(schedulerListeners);
	}

	@Override
	public void setGlobalJobListeners(JobListener... globalJobListeners) {
		// TODO Auto-generated method stub
		super.setGlobalJobListeners(globalJobListeners);
	}

	@Override
	public void setGlobalTriggerListeners(
			TriggerListener... globalTriggerListeners) {
		// TODO Auto-generated method stub
		super.setGlobalTriggerListeners(globalTriggerListeners);
	}

	@Override
	public void setTransactionManager(
			PlatformTransactionManager transactionManager) {
		// TODO Auto-generated method stub
		super.setTransactionManager(transactionManager);
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		// TODO Auto-generated method stub
		super.setResourceLoader(resourceLoader);
	}

	@Override
	protected void registerJobsAndTriggers() throws SchedulerException {
		// TODO Auto-generated method stub
		super.registerJobsAndTriggers();
	}

	@Override
	protected void registerListeners() throws SchedulerException {
		// TODO Auto-generated method stub
		super.registerListeners();
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

	@Override
	public void setSchedulerName(String schedulerName) {
		// TODO Auto-generated method stub
		super.setSchedulerName(schedulerName);
	}

	@Override
	public void setQuartzProperties(Properties quartzProperties) {
		// TODO Auto-generated method stub
		super.setQuartzProperties(quartzProperties);
	}

	@Override
	public void setTaskExecutor(Executor taskExecutor) {
		// TODO Auto-generated method stub
		super.setTaskExecutor(taskExecutor);
	}

	@Override
	public void setStartupDelay(int startupDelay) {
		// TODO Auto-generated method stub
		super.setStartupDelay(startupDelay);
	}

	@Override
	public void setWaitForJobsToCompleteOnShutdown(
			boolean waitForJobsToCompleteOnShutdown) {
		// TODO Auto-generated method stub
		super.setWaitForJobsToCompleteOnShutdown(waitForJobsToCompleteOnShutdown);
	}

	@Override
	protected void startScheduler(Scheduler scheduler, int startupDelay)
			throws SchedulerException {
		// TODO Auto-generated method stub
		
		super.startScheduler(scheduler, startupDelay);
	}

	@Override
	public void stop() throws SchedulingException {
		System.out.println("定时器停止");
		super.stop();
	}

	@Override
	public void stop(Runnable callback) throws SchedulingException {
		// TODO Auto-generated method stub
		super.stop(callback);
	}
	

}

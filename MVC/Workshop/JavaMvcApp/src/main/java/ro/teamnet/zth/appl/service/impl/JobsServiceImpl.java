package ro.teamnet.zth.appl.service.impl;

import ro.teamnet.zth.appl.dao.JobDao;
import ro.teamnet.zth.appl.domain.Job;
import ro.teamnet.zth.appl.service.JobsService;

import java.util.List;

/**
 * Created by user on 7/18/2016.
 */
public class JobsServiceImpl implements JobsService{
    JobDao jobDao = new JobDao();

    @Override
    public List<Job> findAllJobs() {
        return jobDao.getAllJobs();
    }

    @Override
    public Job findOneJob(String id) {
        return jobDao.getJobById(id);
    }

    @Override
    public void deleteOneJob(String id){
        jobDao.deleteJob(findOneJob(id));
    }
}

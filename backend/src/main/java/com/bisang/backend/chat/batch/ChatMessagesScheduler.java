package com.bisang.backend.chat.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ChatMessagesScheduler {

    private final JobLauncher jobLauncher;
    private final Job chatMessageJob;

    @Autowired
    public ChatMessagesScheduler(JobLauncher jobLauncher, Job chatMessageJob) {
        this.jobLauncher = jobLauncher;
        this.chatMessageJob = chatMessageJob;
    }

    // 매일 새벽 4시에 실행
    @Scheduled(cron = "0 0 4 * * *")
    public void runJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis()) // 새로운 파라미터 추가
                .toJobParameters();
        jobLauncher.run(chatMessageJob, jobParameters);
    }
}

package com.bisang.backend.chat.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.bisang.backend.chat.domain.ChatMessage;
import com.bisang.backend.chat.domain.redis.RedisChatMessage;

@Configuration
public class ChatMessageBatchConfig extends DefaultBatchConfiguration {

    private final ItemReader<RedisChatMessage> chatItemReader;
    private final ItemProcessor<RedisChatMessage, ChatMessage> chatItemProcessor;
    private final ItemWriter<ChatMessage> chatItemWriter;

    public ChatMessageBatchConfig(
            ItemReader<RedisChatMessage> chatItemReader,
            ItemProcessor<RedisChatMessage, ChatMessage> chatItemProcessor,
            ItemWriter<ChatMessage> chatItemWriter
    ) {
        this.chatItemReader = chatItemReader;
        this.chatItemProcessor = chatItemProcessor;
        this.chatItemWriter = chatItemWriter;
    }

    @Bean
    public Job chatMessageJob(JobRepository jobRepository, Step chatMessageStep) {
        return new JobBuilder("chatMessageJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chatMessageStep)
                .build();
    }

    @Bean
    public Step chatMessageStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("chatMessageStep", jobRepository)
                .<RedisChatMessage, ChatMessage>chunk(100, transactionManager)
                .reader(chatItemReader)
                .processor(chatItemProcessor)
                .writer(chatItemWriter)
                .build();
    }
}

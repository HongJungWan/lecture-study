package com.example.batchpractice.ch05.ch04;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;

//@Configuration
@RequiredArgsConstructor
public class LimitAndAllowConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob() {
        return jobBuilderFactory.get("batchJob")
                .start(step1())
                .next(step2())
//                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step1 was executed");
                    System.out.println("contribution = " + contribution);
                    System.out.println("chunkContext = " + chunkContext);
                    return RepeatStatus.FINISHED;
                })
                .allowStartIfComplete(false)
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 was executed");
                    System.out.println("contribution = " + contribution);
                    System.out.println("chunkContext = " + chunkContext);
                    throw new RuntimeException("step2 was failed");
//                    return RepeatStatus.FINISHED;
                })
                .startLimit(3)
                .build();
    }

}

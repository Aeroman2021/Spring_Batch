package com.example.spring_batch.config;

import com.example.spring_batch.model.Account;
import com.example.spring_batch.model.Customer;
import com.example.spring_batch.repository.AccountRepository;
import com.example.spring_batch.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private CustomerRepository customerRepository;
    private AccountRepository accountRepository;


    @Bean
    public FlatFileItemReader<Customer> customerReader(){
        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/cust.csv"));
        itemReader.setName("csv-customer-reader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(customerLineMapper());
        return itemReader;
    }

    @Bean
    public FlatFileItemReader<Account> accountReader(){
        FlatFileItemReader<Account> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/account.csv"));
        itemReader.setName("csv-account-reader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(accountLineMapper());
        return itemReader;
    }

    private LineMapper<Customer> customerLineMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("customerId","name","surName","address","zipCode","nationalId","dob");

        BeanWrapperFieldSetMapper<Customer> customerFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        customerFieldSetMapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(customerFieldSetMapper);
        return lineMapper;
    }

    private LineMapper<Account> accountLineMapper() {
        DefaultLineMapper<Account> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("accountId","number","surName","accountType","customerId","limit","openDate","balance");

        BeanWrapperFieldSetMapper<Account> accountFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        accountFieldSetMapper.setTargetType(Account.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(accountFieldSetMapper);
        return lineMapper;
    }

    @Bean
    public CustomerProcessor customerProcessor(){
        return new CustomerProcessor();
    }

    @Bean
    public AccountProcessor accountProcessor(){
        return new AccountProcessor();
    }

    @Bean
    public RepositoryItemWriter<Customer> customerWriter(){
         RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
         writer.setRepository(customerRepository);
         writer.setMethodName("save");
         return writer;
    }

    @Bean
    public RepositoryItemWriter<Account> accountWriter(){
        RepositoryItemWriter<Account> writer = new RepositoryItemWriter<>();
        writer.setRepository(accountRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("customer-csv-step").<Customer,Customer>chunk(10)
                .reader(customerReader())
                .processor(customerProcessor())
                .writer(customerWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step step2(){
        return stepBuilderFactory.get("account-csv-step").<Account,Account>chunk(10)
                .reader(accountReader())
                .processor(accountProcessor())
                .writer(accountWriter())
                .taskExecutor(taskExecutor())
                .build();
    }


    @Bean
    public Job runJob(){
        return jobBuilderFactory.get("import-customers-and-accounts")
                .flow(step1())
                .next(step2())
                .end()
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(10);
        return simpleAsyncTaskExecutor;
    }
}

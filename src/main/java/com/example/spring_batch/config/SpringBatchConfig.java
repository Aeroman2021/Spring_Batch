package com.example.spring_batch.config;

import com.example.spring_batch.model.*;
import com.example.spring_batch.repository.AccountRepository;
import com.example.spring_batch.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.sql.DataSource;
import java.awt.print.Book;
import java.io.File;
import java.io.IOException;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private CustomerRepository customerRepository;
    private AccountRepository accountRepository;

    private BankAccountFieldSetMapper accountFieldSetMapper;

    private BankCustomerFieldSetMapper bankCustomerFieldSetMapper;



    @Bean
    public FlatFileItemReader<BankCustomer> customerReader(){
        FlatFileItemReader<BankCustomer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/cust.csv"));
        itemReader.setName("csv-customer-reader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(customerLineMapper());
        return itemReader;
    }

    @Bean
    public FlatFileItemReader<BankAccount> accountReader(){
        FlatFileItemReader<BankAccount> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/account.csv"));
        itemReader.setName("csv-account-reader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(accountLineMapper());
        return itemReader;
    }

    private LineMapper<BankCustomer> customerLineMapper() {
        DefaultLineMapper<BankCustomer> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("customerId","customerName","customerSurname","customerAddress",
                "customerZipCode","customerNationalId","customerDob");
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(bankCustomerFieldSetMapper);
        return lineMapper;
    }

    private LineMapper<BankAccount> accountLineMapper() {
        DefaultLineMapper<BankAccount> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("accountId","accountNumber","accountType","customerId",
                "accountLimit","accountOpenDate","accountCurrentBalance");

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
    public RepositoryItemWriter<BankCustomer> customerWriter(){
         RepositoryItemWriter<BankCustomer> writer = new RepositoryItemWriter<>();
         writer.setRepository(customerRepository);
         writer.setMethodName("save");
         return writer;
    }

    @Bean
    public JsonFileItemWriter<BankAccount> jsonFileItemWriter() {
        return new JsonFileItemWriterBuilder<BankAccount>()
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(new ClassPathResource("account.json"))
                .name("accountJsonFileItemWriter")
                .build();
    }

    @Bean
    public RepositoryItemWriter<BankAccount> accountWriter(){
        RepositoryItemWriter<BankAccount> writer = new RepositoryItemWriter<>();
        writer.setRepository(accountRepository);
        writer.setMethodName("save");
        return writer;
    }


//    @Bean
//    public JdbcCursorItemReader<BankAccount> dbReader(){
//        JdbcCursorItemReader<BankAccount> cursorItemReader = new JdbcCursorItemReader<>();
//        cursorItemReader.setDataSource(dataSource);
//        cursorItemReader.setSql("SELECT ACCOUNT_ID,ACCOUNT_NUMBER,ACCOUNT_TYPE,CUSTOMER_ID,ACCOUNT_LIMIT,ACCOUNT_OPEN_DATE," +
//                "ACCOUNT_BALANCE FROM BANK_ACCOUNT");
//        cursorItemReader.setRowMapper(new BankAccountRowMapper());
//        return cursorItemReader;
//    }

//    @Bean
//    public JsonFileItemWriter<BankAccount> jsonFileItemWriter() {
//        return new JsonFileItemWriterBuilder<BankAccount>()
//                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
//                .resource(new ClassPathResource("account.json"))
//                .name("accountsJsonFileItemWriter")
//                .build();
//    }



    @Bean
    public Step step1(){
        return stepBuilderFactory.get("customer-csv-step").<BankCustomer, BankCustomer>chunk(5)
                .reader(customerReader())
                .processor(customerProcessor())
                .writer(customerWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step step2(){
        return stepBuilderFactory.get("account-csv-step").<BankAccount, BankAccount>chunk(5)
                .reader(accountReader())
                .processor(accountProcessor())
                .writer(accountWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step step3(){
        return stepBuilderFactory.get("account-json").<BankAccount, BankAccount>chunk(2)
                .reader(accountReader())
//                .processor(accountProcessor())
                .writer(jsonFileItemWriter())
                .taskExecutor(taskExecutor())
                .build();
    }


    @Bean
    public Job runJob(){
        return jobBuilderFactory.get("import-customers-and-accounts")
                .flow(step1())
                .next(step2())
//                .next(step3())
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

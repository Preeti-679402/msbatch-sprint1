package com.superleague.inventory.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.superleague.inventory.demo.model.Inventory;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("classPath:/input/inventory.csv")
    private Resource inputResource;

    @Bean
    public Job readCSVFileJob() {
        return jobBuilderFactory
                .get("readCSVFileJob")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .build();
    }
    

    @Bean
    public Step step() {
        return stepBuilderFactory
                .get("step")
                .<Inventory, Inventory>chunk(5)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
    

    @Bean
    public ItemProcessor<Inventory, Inventory> processor() {
        return new DBLogProcessor();
    }


    @Bean
    public FlatFileItemReader<Inventory> reader() {
        FlatFileItemReader<Inventory> itemReader = new FlatFileItemReader<Inventory>();
        itemReader.setLineMapper(lineMapper());
        itemReader.setLinesToSkip(1);
        itemReader.setResource(inputResource);
        return itemReader;
    }
    



    @Bean
    public DataSource dataSource() {

        EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();

        return embeddedDatabaseBuilder.addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .addScript("classpath:inventory.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Inventory> writer() {

        JdbcBatchItemWriter<Inventory> itemWriter = new JdbcBatchItemWriter<Inventory>();

        itemWriter.setDataSource(dataSource());
        itemWriter.setSql("INSERT INTO INVENTORY ( ID,NAME,QUANTITY,PRICE,InDate) VALUES ( :id, :name, :quantity, :price, :inDate )");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Inventory>());
        return itemWriter;
    }


    @Bean
    public LineMapper<Inventory> lineMapper() {
        DefaultLineMapper<Inventory> lineMapper = new DefaultLineMapper<Inventory>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        BeanWrapperFieldSetMapper<Inventory> fieldSetMapper = new BeanWrapperFieldSetMapper<Inventory>();

        lineTokenizer.setNames(new String[]{"id", "name", "quantity", "price", "inDate" });
        lineTokenizer.setIncludedFields(new int[]{0, 1, 2, 3, 4});
        fieldSetMapper.setTargetType(Inventory.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }


}

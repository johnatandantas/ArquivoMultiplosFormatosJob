package com.springbatch.arquivomultiplosformatos.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.springbatch.arquivomultiplosformatos.dominio.Remessa;
import com.springbatch.arquivomultiplosformatos.writer.RemessaFileWriterConfig;

@EnableBatchProcessing
@Configuration
public class BatchConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Value("${arquivo.remessa}")
	private Resource[] inputResources;

	@Bean
	public Job readCSVFilesJob() {
		return jobBuilderFactory.get("readCSVFilesJob").incrementer(new RunIdIncrementer()).start(step1()).build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<Remessa, Remessa>chunk(5).reader(multiResourceItemReader())
				.writer(writer()).build();
	}

	@Bean
	public RemessaFileWriterConfig<Remessa> writer() {
		return new RemessaFileWriterConfig<Remessa>();
	}

	@Bean
	public MultiResourceItemReader<Remessa> multiResourceItemReader() {
		MultiResourceItemReader<Remessa> resourceItemReader = new MultiResourceItemReader<Remessa>();
		resourceItemReader.setResources(inputResources);
		resourceItemReader.setDelegate(reader());
		return resourceItemReader;
	}

	@Bean
	public FlatFileItemReader<Remessa> reader() {
		// Create reader instance
		FlatFileItemReader<Remessa> reader = new FlatFileItemReader<Remessa>();

		// Configure how each line will be parsed and mapped to different values
		reader.setLineMapper(new DefaultLineMapper<Remessa>() {
			{
				// 3 columns in each row
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "id", "nome", "sobrenome", "idade", "email" });
					}
				});
				// Set values in Cliente class
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Remessa>() {
					{
						setTargetType(Remessa.class);
					}
				});
			}
		});
		return reader;
	}

}

package com.springbatch.simplepartitionerlocal.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.springbatch.simplepartitionerlocal.dominio.Pessoa;

@Configuration
public class MigrarPessoaStepConfig {
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("transactionManagerApp")
	private PlatformTransactionManager transactionManagerApp;

    @Bean
    public Step migrarPessoaManager(
            @Qualifier("pessoaPartitioner") Partitioner pessoaPartitioner,
            @Qualifier("arquivoPessoaPartitionerReader")ItemStreamReader<Pessoa> arquivoPessoaReader,
            JdbcBatchItemWriter<Pessoa> pessoaWriter,
            TaskExecutor taskExecutor){
        return stepBuilderFactory
                .get("migrarPessoaManager")
                .partitioner("migrarPessoa.manager", pessoaPartitioner)
                .step(migrarPessoaStep(arquivoPessoaReader, pessoaWriter))
                .gridSize(10)
                .taskExecutor(taskExecutor)
                .build();
    }

	@Bean
	public Step migrarPessoaStep(
			ItemReader<Pessoa> arquivoPessoaReader,
            JdbcBatchItemWriter<Pessoa> pessoaWriter) {
		return stepBuilderFactory
				.get("migrarPessoaStep")
				.<Pessoa, Pessoa>chunk(2000)
				.reader(arquivoPessoaReader)
				.writer(pessoaWriter)
				.transactionManager(transactionManagerApp)
				.build();
	}

}

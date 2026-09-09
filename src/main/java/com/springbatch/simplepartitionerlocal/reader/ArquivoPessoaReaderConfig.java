package com.springbatch.simplepartitionerlocal.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

import com.springbatch.simplepartitionerlocal.dominio.Pessoa;

@Configuration
public class ArquivoPessoaReaderConfig {
	@Bean
	public FlatFileItemReader<Pessoa> arquivoPessoaReader() {
		return new FlatFileItemReaderBuilder<Pessoa>()
				.name("arquivoPessoaReader")
				.resource(new FileSystemResource("files/pessoas.csv"))
				.delimited()
				.names("nome", "email", "dataNascimentoStr", "idade", "id")
				.addComment("--")
                .targetType(Pessoa.class)
				.build();
	}
}

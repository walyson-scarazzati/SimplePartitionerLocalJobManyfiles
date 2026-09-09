package com.springbatch.simplepartitionerlocal.reader;

import com.springbatch.simplepartitionerlocal.dominio.Pessoa;

import java.util.Date;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
public class ArquivoPessoaPartitionReaderConfig {
    @StepScope
    @Bean
    public FlatFileItemReader<Pessoa> arquivoPessoaPartitionReader(@Value("#{stepExecutionContext['file']}") Resource resource) {
        return new FlatFileItemReaderBuilder<Pessoa>()
                .name("arquivoPessoaReader")
                .resource(resource)
                .delimited()
                .names("nome", "email", "dataNascimento", "idade", "id")
                .addComment("--")
                .fieldSetMapper(fieldSetMapper())
                .targetType(Pessoa.class)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Pessoa> arquivoPessoaPartitionerReader(
            @Value("#{stepExecutionContext['file']}") Resource resource) {
        return new FlatFileItemReaderBuilder<Pessoa>()
                .name("arquivoPessoaPartitionerReader")
                .resource(resource)
                .delimited()
                .names("nome", "email", "dataNascimentoStr", "idade", "id")
                .addComment("--")
                .targetType(Pessoa.class)
                .build();
    }

	private FieldSetMapper<Pessoa> fieldSetMapper() {
		return new FieldSetMapper<Pessoa>() {

			@Override
			public Pessoa mapFieldSet(FieldSet fieldSet) {
				Pessoa pessoa = new Pessoa();
				pessoa.setNome(fieldSet.readString("nome"));
				pessoa.setEmail(fieldSet.readString("email"));
				pessoa.setDataNascimento(new Date(fieldSet.readDate("dataNascimento", "yyyy-MM-dd HH:mm:ss").getTime()));
				pessoa.setIdade(fieldSet.readInt("idade"));
				pessoa.setId(fieldSet.readInt("id"));
				return pessoa;
			}
		};
	}
}

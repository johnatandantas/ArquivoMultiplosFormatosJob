package com.springbatch.arquivomultiplosformatos.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;

import com.springbatch.arquivomultiplosformatos.dominio.Remessa;

@Configuration
public class RemessaFileWriterConfig<T> implements ItemWriter<Remessa> {

	@Override
	public void write(List<? extends Remessa> items) throws Exception {
		for (Remessa item : items) { 
            System.out.println(item.getEmail()); 
        } 
		
	} 
    
}

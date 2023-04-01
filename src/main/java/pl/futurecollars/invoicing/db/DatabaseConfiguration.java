package pl.futurecollars.invoicing.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.file.FileBasedDatabase;
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.utils.FileService;

@Configuration
public class DatabaseConfiguration {

  @Bean
  @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
  public Database inMemoryDatabase(){
    return new InMemoryDatabase();
  }

  @Bean
  @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
  public Database fileBasedDatabase(){
    return new FileBasedDatabase();
  }

  @Bean
  @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
  public Database idService(FileService fileService, @Value("${invoicing-system.database.directory}") String dataBaseDirectory, String idFile){
    return new IdService();
  }

}

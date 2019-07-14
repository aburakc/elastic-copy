package aburakc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ElasticCopyApplication implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticCopyApplication.class);

    @Autowired
    private ElasticCoppierService elasticCoppierService;

    public static void main(String[] args) {
        LOGGER.info("STARTING THE APPLICATION");
        SpringApplication.run(ElasticCopyApplication.class, args);
        LOGGER.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) throws Exception {
        elasticCoppierService.beginCopy();
    }
}

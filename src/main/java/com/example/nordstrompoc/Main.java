package com.example.nordstrompoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoOperations;

@SpringBootApplication
public class Main implements CommandLineRunner {
//NordstromPocApplication


        @Autowired
        // TEMPLATE IS REQUIRED FOR BULK OPERATIONS.  REPOSITORY DOES NOT SUPPORT IT.
        private MongoOperations mongoTemplate;

        public static void main(String[] args) {
                SpringApplication.run(Main.class, args);
        }

        @Override
        public void run(String... args) throws Exception {
                BulkLoad bl = new BulkLoad(this.mongoTemplate);
                // bl.load();

                ProcessTest pt = new ProcessTest(this.mongoTemplate);
                pt.run();
        }
}

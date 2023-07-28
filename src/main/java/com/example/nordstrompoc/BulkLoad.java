package com.example.nordstrompoc;

import com.mongodb.bulk.BulkWriteResult;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoOperations;


public class BulkLoad {
    public MongoOperations mongoTemplate;

    public BulkLoad(MongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void load() {
        for (Integer y = 0; y < 10000; y++) {
            BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, TestPOJO.class);

            for (Integer i = 0; i < 100000; i++)
            {
                TestPOJO testPOJO = new TestPOJO();
                testPOJO.bool = Helpers.getRandomBoolean(.5);
                testPOJO.date = Helpers.getRandomDate(100000000);
                testPOJO.dubble = Helpers.getRandomDouble(1000, 100000);
                testPOJO.integer = Helpers.getRandomInt(10, 10000000);
                testPOJO.stringField1 = Helpers.getRandomString(12, false);
                testPOJO.stringField2 = Helpers.getRandomString(10, false);
                testPOJO.stringField3 = Helpers.getRandomString(36, true);

                for (Integer x = 0; x < Helpers.getRandomInt(3, 15); x++) {
                    TestPOJONested testPOJONested = new TestPOJONested();
                    testPOJONested.name = Helpers.getRandomString(10, false);

                    for (Integer z = 0; z < Helpers.getRandomInt(3, 8); z++) {
                        testPOJONested.strings.add(Helpers.getRandomString(10));
                    }

                    testPOJO.nestedStrings.add(testPOJONested);
                }

                //System.out.println(String.format("i: %s", i));
                bulkOps.insert(testPOJO);
            }

            System.out.println(String.format("y: %s", y));
            BulkWriteResult results = bulkOps.execute();
        }
    }
}

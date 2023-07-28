package com.example.nordstrompoc;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.client.result.UpdateResult;

public class ProcessTest {
    public MongoOperations mongoTemplate;

    public ProcessTest(MongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void run() {
        // GET SOME RANDOM DATA
        AggregationResults<TestPOJO> sampleSet = this.getSampleSet();
        List<TestPOJO> sampleSetList = sampleSet.getMappedResults();

        for(TestPOJO sampleRecord: sampleSetList) {
            try {
                // GENERATE A RANDOM VALUE TO BE USED AS THE UPDATE PREDICATE.
                String updateValue = Helpers.getRandomString(10, false);

                // PREPARE AN UPDATE PREDICATE TO USE WITH updateFirst BELOW
                Update update = new Update();
                update.set("testField", updateValue);
                //update.pull("nestedStrings", Query.query(Criteria.where("name").is(sampleRecord.nestedStrings.get(0))));

                // PREPARE A FILTER PREDICATE TO USE WITH updateFirst BELOW
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(sampleRecord.id));

                System.out.println(String.format("Update Record.  Filter Predicate: %s, Mutation Predicate: %s", query.toString(), update.toString()));

                try {
                    UpdateResult updateResult = this.mongoTemplate.updateFirst(query, update, TestPOJO.class);

                    if (updateResult.getMatchedCount() != 1 && updateResult.getModifiedCount() != 1) {
                        System.out.println(String.format("Bad - update failed.   MatchedCount: %s, UpdatedCount: %s", updateResult.getMatchedCount(), updateResult.getModifiedCount()));
                    }
                }
                catch (Exception ex) {
                    // EXPECT A COMMAND TIMEOUT.  EFFECTIVELY SWALLOW THE ERROR, AND CONTINUE.
                    System.out.println(ex.getMessage());
                }

                // FIND THE RECORD AND VERIFY THE UPDATE IS PERSISTED AND VISIBLE DURING READ OPERATIONS.
                TestPOJO findRecord = mongoTemplate.findById(sampleRecord.id, TestPOJO.class);

                // AVOID A NULL REFERENCE
                String testField = findRecord.testField;

                if (testField == null) {
                    testField = "";
                }

                if(testField.equals(updateValue)) {
                    System.out.println("good");
                }
                else {
                    System.out.println(String.format("Status: %s, testField actual: '%s', testField expected: '%s'", "bad", testField, updateValue));
                }
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private AggregationResults<TestPOJO> getSampleSet () {
        // THIS METHOD BENEFITS FROM THE FOLLOWING INDEX - db.testPOJO.createIndex({integer: 1})

        // THE FIELD "integer" IS ASSIGNED A RANDOM NUMBER BETWEEN 10 AND 10M UPON BulkLoad.load() - this.getRandomInt(10, 10,000,000);
        // FOR SAMPLING, GET A LIST OF DATA ELEMENTS BETWEEN TWO RANDOM VALUES IN THIS RANGE
        int sampleSetStartRange = Helpers.getRandomInt(10, 9000000);
        MatchOperation stage1 = Aggregation.match(new Criteria("integer").gte(sampleSetStartRange));

        // JUST GRAB A FEW.  DOESN'T NEED TO BE AN EXHAUSTIVE CHECK ACROSS ALL RECORDS.
        LimitOperation stage2 = Aggregation.limit(100);

        Aggregation agg = Aggregation.newAggregation(stage1, stage2);

        AggregationResults<TestPOJO> response = this.mongoTemplate.aggregate(agg, TestPOJO.class, TestPOJO.class);

        // OUTPUT THE JSON VERSION OF THE AGGREGATION PIPELINE
        System.out.println(String.format("Aggregation: %s", agg.toString()));

        // GET A COUNT OF SAMPLE SIZE - SHOULD MATCH THE LIMIT AGG STAGE VALUE.
        System.out.println(String.format("Sample size: %s", response.getMappedResults().size()));

        return response;
    }
}

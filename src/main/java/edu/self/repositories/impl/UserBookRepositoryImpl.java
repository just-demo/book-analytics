package edu.self.repositories.impl;

import edu.self.model.User;
import edu.self.model.UserBook;
import edu.self.repositories.UserBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

import static com.mongodb.DBCollection.ID_FIELD_NAME;
import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class UserBookRepositoryImpl implements UserBookRepository {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public Optional<String> findBookId(String username, String bookName) {
        AggregationResults<UserBook> results = mongoOperations.aggregate(newAggregation(
                match(where(ID_FIELD_NAME).is(username)),
                unwind("books"),
                match(where("books.name").is(bookName)),
                // Don't understand why it should be "_id" instead fo "id" single UserBook has no annotated id field at all
                project().and("books.id").as(ID_FIELD_NAME)), User.class, UserBook.class);
        return ofNullable(results.getUniqueMappedResult()).map(UserBook::getId);
    }

    @Override
    public Iterable<String> findBookIds(String username) {
        Query query = new Query();
        query.addCriteria(where(ID_FIELD_NAME).is(username));
        query.fields().include("books.id");
        return ofNullable(mongoOperations.findOne(query, User.class))
                .map(User::getBooks)
                .orElse(emptySet())
                .stream()
                .map(UserBook::getId).collect(toList());
    }
}

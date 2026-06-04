package com.tutorias.infrastructure.repository.mongo;

import com.tutorias.infrastructure.repository.mongo.document.MateriaDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoMateriaRepository extends MongoRepository<MateriaDocument, String> {
}

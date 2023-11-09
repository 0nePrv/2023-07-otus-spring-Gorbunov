package ru.otus.homework.service.validation;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {

  @Override
  public ValidationResult validate(Ids ids) {
    return validateIds(ids.values());
  }

  @Override
  public ValidationResult validate(StringFields stringFields) {
    return validateTextFields(stringFields.values());
  }

  @Override
  public ValidationResult validate(Ids ids, StringFields stringFields) {
    ValidationResult idsValidation = validateIds(ids.values());
    ValidationResult fieldsValidation = validateTextFields(stringFields.values());
    return idsValidation.append(fieldsValidation);
  }

  private ValidationResult validateIds(String... ids) {
    ValidationResult validationResult = new ValidationResult();
    for (String id : ids) {
      if (id == null || !ObjectId.isValid(id)) {
        return validationResult.reject("Invalid id: " + id);
      }
    }
    return validationResult;
  }

  private ValidationResult validateTextFields(String... fields) {
    ValidationResult validationResult = new ValidationResult();
    for (String field : fields) {
      if (field == null || field.isBlank()) {
        return validationResult.reject("Fields can not be blank");
      }
    }
    return validationResult;
  }
}
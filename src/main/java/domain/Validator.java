package domain;

import exceptions.ValidationException;

@FunctionalInterface
public interface Validator<T> {

    /**
     * Valideaza o entitate
     * @param entity entitatea de validat
     * @throws ValidationException daca datele sunt invalide
     */
    void validate(T entity) throws ValidationException;

    default Validator<T> andThen(Validator<T> other) {
        return (entity) -> {
            this.validate(entity);
            other.validate(entity);
        };
    }
}

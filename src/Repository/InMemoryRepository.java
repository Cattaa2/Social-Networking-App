package Repository;

import domain.Entity;
import domain.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected Validator<E> validator;
    protected Map<ID, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        this.entities = new HashMap<>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        if(id == null){
            throw new IllegalArgumentException("ID-ul nu poate fi null");
        }
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if(entity == null){
            throw new IllegalArgumentException("Entitatea nu poate fi null");
        }
        validator.validate(entity);
        if(entities.containsKey(entity.getId())){
            return entity;
        }
        entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public Optional<E> delete(ID id) {
        if(id == null){
            throw new IllegalArgumentException("ID-ul nu poate fi null");
        }
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity) {
        if(entity == null){
            throw new IllegalArgumentException("Entitatea nu poate fi null");
        }
        validator.validate(entity);
        if(entities.containsKey(entity.getId())){
            entities.put(entity.getId(), entity);
            return Optional.empty();
        }
        return Optional.of(entity);
    }
}

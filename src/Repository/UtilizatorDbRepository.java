package Repository;

import domain.Utilizator;
import domain.Validator;

import java.util.Optional;

public class UtilizatorDbRepository implements Repository<Integer, Utilizator> {
    private Validator<Utilizator> validator;
    private String url;
    private String user;
    private String password;

    public UtilizatorDbRepository(String url, String user, String password, Validator<Utilizator> validator) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Utilizator> findOne(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterable<Utilizator> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Utilizator save(Utilizator entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Utilizator> delete(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

package Repository;

import domain.Prietenie;
import domain.Tuple;
import domain.Validator;

import java.util.Optional;

public class PrietenieDbRepository implements Repository<Tuple<Integer,Integer>, Prietenie> {
    private Validator<Prietenie> validator;
    private String url;
    private String user;
    private String password;

    public PrietenieDbRepository(String url, String user, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Prietenie> findOne(Tuple<Integer, Integer> id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterable<Prietenie> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Prietenie save(Prietenie entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Prietenie> delete(Tuple<Integer, Integer> id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

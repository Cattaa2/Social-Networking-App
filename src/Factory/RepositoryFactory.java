package Factory;

import Repository.PrietenieFileRepository;
import Repository.UtilizatorDbRepository;
import Repository.PrietenieDbRepository;
import Repository.UtilizatorFileRepository;
import Repository.InMemoryRepository;
import Repository.Repository;
import domain.Prietenie;
import domain.Tuple;
import domain.Utilizator;
import domain.Validator;
import exceptions.RepoException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

public class RepositoryFactory {
    private Properties properties;

    public RepositoryFactory() {
        properties = new Properties();
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if(is == null) {
                throw new RepoException("Properties file not found.");
            }
            properties.load(is);
        } catch(IOException e) {
            throw new RepoException("Eroare la citirea fisierului config.properties", e);
        }
    }

    public Repository<Integer, Utilizator> createUtilizatorRepository(Validator<Utilizator> validator) {
        String typeStr = properties.getProperty("repository.type");
        if(typeStr == null) { throw new RepoException("Repository type not found."); }
        RepositoryType type =  RepositoryType.valueOf(typeStr.toUpperCase());
        Map<RepositoryType, Function<Validator<Utilizator>, Repository<Integer, Utilizator>>> strategii = new HashMap<>();

        strategii.put(RepositoryType.MEMORY, InMemoryRepository::new);
        strategii.put(RepositoryType.FILE, val -> {
            String usersFile = properties.getProperty("users.file");
            if (usersFile == null) throw new RepoException("Users file nu e in config");
            return new UtilizatorFileRepository(usersFile, val);
        });
        strategii.put(RepositoryType.DATABASE, val->{
            String url =  properties.getProperty("db.url");
            String user =  properties.getProperty("db.user");
            String password =  properties.getProperty("db.password");
            if(url == null || user == null || password == null) throw new RepoException("Lipseste user/url/password pt DB.");
            return new UtilizatorDbRepository(url, user, password, val);
        });
        Function<Validator<Utilizator>, Repository<Integer,Utilizator>> strategieAleasa = strategii.get(type);
        if(strategieAleasa == null) throw new  RepoException("Repository type invalid");
        return strategieAleasa.apply(validator);
    }

    public Repository<Tuple<Integer,Integer>, Prietenie> createPrietenieRepository(Validator<Prietenie> validator) {
        String typeStr = properties.getProperty("repository.type");
        if(typeStr == null) { throw new RepoException("Repository type not found."); }
        RepositoryType type =  RepositoryType.valueOf(typeStr.toUpperCase());
        Map<RepositoryType, Function<Validator<Prietenie>, Repository<Tuple<Integer,Integer>, Prietenie>>> strategii = new HashMap<>();

        strategii.put(RepositoryType.MEMORY, InMemoryRepository::new);
        strategii.put(RepositoryType.FILE, val -> {
            String friendshipsFile = properties.getProperty("friendships.file");
            if (friendshipsFile == null) throw new RepoException("Friendships file nu e in config");
            return new PrietenieFileRepository(friendshipsFile, val);
        });
        strategii.put(RepositoryType.DATABASE, val->{
            String url = properties.getProperty("db.url");
            String user =  properties.getProperty("db.user");
            String password =  properties.getProperty("db.password");
            if(url == null || user == null || password == null) throw new RepoException("Lipseste user/url/password DB.");
            return new PrietenieDbRepository(url, user, password, val);
        });

        Function<Validator<Prietenie>, Repository<Tuple<Integer, Integer>, Prietenie>> strategieAleasa = strategii.get(type);
        if(strategieAleasa == null) throw new  RepoException("Repository type invalid");
        return strategieAleasa.apply(validator);
    }
}

package Repository;

import domain.Prietenie;
import domain.Tuple;
import domain.Validator;
import exceptions.RepoException;

import java.net.ConnectException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrietenieDbRepository implements Repository<Tuple<Integer,Integer>, Prietenie> {
    private Validator<Prietenie> validator;
    private String url;
    private String user;
    private String password;

    /**
     * Constructs a new PrietenieDbRepository.
     *
     * @param url the database connection URL
     * @param user the database username
     * @param password the database password
     * @param validator the validator for Prietenie entity
     */
    public PrietenieDbRepository(String url, String user, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.validator = validator;
    }

    /**
     * Makes the smaller ID be always on the left (CHECK id1 < id2).
     *
     * @param id the original tuple ID
     * @return a tuple with min ID first and max ID second
     */
    private Tuple<Integer, Integer> normalizeId(Tuple<Integer, Integer> id) {
        int min = Math.min(id.getLeft(), id.getRight());
        int max = Math.max(id.getLeft(), id.getRight());
        return new Tuple<>(min, max);
    }

    /**
     * Get a friendship by its tuple ID.
     *
     * @param id the tuple ID (id1, id2)
     * @return an Optional with the found friendship, or empty if not found
     * @throws IllegalArgumentException if the id is null
     * @throws RepoException if there is a database error
     */
    @Override
    public Optional<Prietenie> findOne(Tuple<Integer, Integer> id) {
        if (id == null || id.getLeft() == null || id.getRight() == null) {
            throw new IllegalArgumentException("ID-ul prieteniei nu poate fi null!");
        }
        Tuple<Integer, Integer> normalId = normalizeId(id);
        String sql = "SELECT id1, id2, friends_from FROM friendships WHERE id1 = ? AND id2 = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setInt(1,normalId.getLeft());
            statement.setInt(2,normalId.getRight());
            try (ResultSet rs = statement.executeQuery()){
                if (rs.next()) {
                    LocalDateTime date = rs.getTimestamp("friends_from").toLocalDateTime();
                    return Optional.of(new Prietenie(normalId.getLeft(), normalId.getRight(), date));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepoException("Eroare la DB (findOne friendship):" + e.getMessage());
        }
    }

    /**
     * Gets all friendships from the database.
     *
     * @return an Iterable of all friendships
     * @throws RepoException if there is a database error
     */
    @Override
    public Iterable<Prietenie> findAll() {
        List<Prietenie> friendships = new ArrayList<>();
        String sql = "SELECT id1, id2, friends_from FROM friendships";
        try (Connection connection = DriverManager.getConnection(url,user,password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()){

            while (rs.next()) {
                int id1 = rs.getInt("id1");
                int id2 = rs.getInt("id2");
                LocalDateTime date = rs.getTimestamp("friends_from").toLocalDateTime();
                friendships.add(new Prietenie(id1, id2, date));
            }
            return friendships;
        } catch (SQLException e) {
            throw new RepoException("Eroare la DB (findAll friendships):" + e.getMessage());
        }
    }

    /**
     * Save a new friendship to the database.
     *
     * @param entity the friendship entity to save
     * @return null if saved successfully or the entity itself if the friendship already exists
     * @throws IllegalArgumentException if the entity is null
     * @throws RepoException if a database error or constraint violation occurs
     */
    @Override
    public Prietenie save(Prietenie entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entitatea nu poate fi null!");
        }
        validator.validate(entity);
        if(findOne(entity.getId()).isPresent()) {
            return entity;
        }

        String sql = "INSERT INTO friendships (id1, id2, friends_from) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url,user,password);
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,entity.getIdUser1());
            statement.setInt(2,entity.getIdUser2());
            statement.setTimestamp(3,Timestamp.valueOf(entity.getData()));
            statement.executeUpdate();
            return null;
        } catch (SQLException e) {
            throw new RepoException("Eroare la DB (save friendship):" + e.getMessage());
        }
    }

    /**
     * Deletes a friendship from the database by the ID.
     *
     * @param id the ID of the friendship to delete
     * @return an Optional with the deleted friendship, or empty if not found
     * @throws IllegalArgumentException if the id is null
     * @throws RepoException if there is a database error
     */
    @Override
    public Optional<Prietenie> delete(Tuple<Integer, Integer> id) {
        if (id == null || id.getLeft() == null || id.getRight() == null) {
            throw new IllegalArgumentException("ID-ul prieteniei nu poate fi null!");
        }
        Optional<Prietenie> toDelete = findOne(id);
        if (toDelete.isEmpty()) {
            return Optional.empty();
        }

        Tuple<Integer, Integer> normalId = normalizeId(id);
        String sql = "DELETE FROM friendships WHERE id1 = ? AND id2 = ?";
        try (Connection connection = DriverManager.getConnection(url,user,password);
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, normalId.getLeft());
            statement.setInt(2, normalId.getRight());
            statement.executeUpdate();
            return toDelete;
        } catch (SQLException e) {
            throw new RepoException("Eroare la DB (delete friendship):" + e.getMessage());
        }
    }

    /**
     * Updates an existing friendship in the database.
     *
     * @param entity the friendship entity with updated fields
     * @return an empty Optional if updated, or an Optional with the entity if not found
     * @throws IllegalArgumentException if the entity is null
     * @throws RepoException if there is a database error
     */
    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entitatea nu poate fi null!");
        }
        validator.validate(entity);
        String sql = "UPDATE friendships SET friends_from = ? WHERE id1 = ? AND id2 = ?";
        try (Connection connection = DriverManager.getConnection(url,user,password);
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setTimestamp(1, Timestamp.valueOf(entity.getData()));
            statement.setInt(2, entity.getIdUser1());
            statement.setInt(3, entity.getIdUser2());
            int  affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                return Optional.empty();
            }
            return  Optional.of(entity);
        } catch (SQLException e) {
            throw new RepoException("Eroare la DB (update friendships):" + e.getMessage());
        }
    }
}

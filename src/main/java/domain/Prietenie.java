package domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Prietenie extends Entity<Tuple<Integer, Integer>> {
    private LocalDateTime data;

    public Prietenie(int idUser1, int idUser2, LocalDateTime data) {
        int minID = Math.min(idUser1, idUser2);
        int maxID = Math.max(idUser1, idUser2);
        this.setId(new  Tuple<>(minID, maxID));
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Prietenie prietenie = (Prietenie) o;
        return Objects.equals(getId(), prietenie.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public int getIdUser1() {
        return getId().getLeft();
    }

    public int getIdUser2() {
        return getId().getRight();
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "id=" + getId() +
                ", idUser1=" + getIdUser1() +
                ", idUser2=" + getIdUser2() +
                ", data=" + data +
                '}';
    }

}

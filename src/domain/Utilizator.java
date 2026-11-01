package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilizator extends Entity<Integer> {
    private String nume;
    private String prenume;
    private List<Utilizator> prieteni = new ArrayList<>();

    public List<Utilizator> getPrieteni() {
        return prieteni;
    }

    public void addPrieten(Utilizator u){
        this.prieteni.add(u);
    }

    public void removePrieten(Utilizator u){
        this.prieteni.remove(u);
    }

    public Utilizator(Integer id, String prenume, String nume) {
        this.setId(id);
        this.nume = nume;
        this.prenume = prenume;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Utilizator that = (Utilizator) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "id=" + getId() + '\'' +
                ", prenume='" + prenume + '\'' +
                ", nume='" + nume + '\'' +
                '}';
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }
}

package domain;

import exceptions.ValidationException;

public class PrietenieValidator implements Validator<Prietenie>{

    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if(entity.getId() == null || entity.getId().getLeft() == null || entity.getId().getRight() == null){
            throw new ValidationException("ID-ul prieteniei nu poate fi null");
        }

        int id1 = entity.getId().getLeft();
        int id2 = entity.getId().getRight();

        if(id1 <= 0 || id2 <= 0){
            throw new ValidationException("ID-urile utilizatorilor trebuie sa fie mai mari ca 0");
        }

        if(id1 == id2){
            throw new ValidationException("Un utilizator nu poate fi prieten cu el insusi");
        }
        if(entity.getData() == null){
            throw new ValidationException("Data prieteniei nu poate fi null");
        }
    }
}

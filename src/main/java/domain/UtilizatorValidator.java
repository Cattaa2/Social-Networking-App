package domain;

import exceptions.ValidationException;

public class UtilizatorValidator implements Validator<Utilizator> {

    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if(entity.getId() != null && entity.getId() <=0){
            throw new ValidationException("Id invalid");
        }
        if(entity.getNume() == null || entity.getNume().isEmpty()){
            throw new ValidationException("Nume invalid");
        }
        if(entity.getPrenume() == null || entity.getPrenume().isEmpty()){
            throw new ValidationException("Prenume invalid");
        }
    }
}

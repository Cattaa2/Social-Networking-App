import Factory.RepositoryFactory;
import Repository.Repository;
import Service.ComunitateService;
import Service.PrietenieService;
import Service.UtilizatorService;
import domain.*;
import exceptions.RepoException;
import exceptions.ServiceException;
import exceptions.ValidationException;
import ui.Console;

public class Main{
    public static void main(String[] args) {
        try {
            Validator<Utilizator> utilizatorValidator = new UtilizatorValidator();
            Validator<Prietenie> prietenieValidator = new PrietenieValidator();
            Validator<Utilizator> validatorNou = u->{
                if (u.getNume().length() < 3){
                    throw new ValidationException("Nume prea scurt");
                }
            };
            Validator<Utilizator> finalValidator = utilizatorValidator.andThen(validatorNou);
            RepositoryFactory factory = new RepositoryFactory();

            Repository<Integer, Utilizator> repoUtilizatori = factory.createUtilizatorRepository(finalValidator);
            Repository<Tuple<Integer, Integer>, Prietenie> repoPrietenii = factory.createPrietenieRepository(prietenieValidator);

            UtilizatorService serviceUtilizator = new UtilizatorService(repoUtilizatori, repoPrietenii);
            PrietenieService servicePrietenii = new PrietenieService(repoPrietenii, repoUtilizatori);
            ComunitateService comunitateService = new ComunitateService(serviceUtilizator);

            Console console = new Console(serviceUtilizator, servicePrietenii, comunitateService);

            console.run();
        }catch(RepoException | ValidationException | ServiceException | IllegalArgumentException | UnsupportedOperationException e) {
            System.out.println("Eroare la pornirea aplicatiei: "  + e.getMessage());
        }
    }
}

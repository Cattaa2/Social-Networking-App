package ui;

import Service.ComunitateService;
import Service.PrietenieService;
import Service.UtilizatorService;
import domain.Prietenie;
import domain.Utilizator;
import exceptions.RepoException;
import exceptions.ServiceException;
import exceptions.ValidationException;

import java.util.Scanner;

public class Console {
    private UtilizatorService utilizatorService;
    private PrietenieService prietenieService;
    private ComunitateService comunitateService;
    private Scanner scanner;

    public Console(UtilizatorService utilizatorService, PrietenieService prietenieService, ComunitateService comunitateService) {
        this.utilizatorService = utilizatorService;
        this.prietenieService = prietenieService;
        this.comunitateService = comunitateService;
        this.scanner = new Scanner(System.in);
    }

    private void printMenu(){
        System.out.println("\n---MENIU---");
        System.out.println("1. Adauga utilizator");
        System.out.println("2. Sterge utilizator");
        System.out.println("3. Afiseaza toti utilizatorii");
        System.out.println("4. Adauga prietenie");
        System.out.println("5. Sterge prietenie");
        System.out.println("6. Afiseaza toate prieteniile");
        System.out.println("7. Afisare numar de comunitati");
        System.out.println("8. Afisare cea mai sociabila comunitate");
        System.out.println("0. Exit");
        System.out.print("Alege o optiune: ");
    }

    public void run(){
        while(true){
            printMenu();
            String optiune =  scanner.nextLine();
            try {
                switch (optiune) {
                    case "1":
                        adaugaUtilizator();
                        break;
                    case "2":
                        removeUtilizator();
                        break;
                    case "3":
                        showAllUtilizatori();
                        break;
                    case "4":
                        adaugaPrietenie();
                        break;
                    case "5":
                        removePrietenie();
                        break;
                    case "6":
                        showAllPrietenii();
                        break;
                    case "7":
                        System.out.println("Numar comunitati: " + comunitateService.numarComunitati());
                        break;
                    case "8":
                        System.out.println("Cea mai sociabila comunitate: ");
                        for (Utilizator u : comunitateService.ceaMaiSociabilaComunitate()) {
                            System.out.println(u.toString());
                        }
                        break;
                    case "0":
                        System.out.println("bye!");
                        return;
                    default:
                        System.out.println("Optiune invalida!");
                        break;
                }
            }catch(NumberFormatException e){
                System.out.println("Numar invalid!");
            } catch(ValidationException | ServiceException | RepoException | IllegalArgumentException | UnsupportedOperationException e){
                System.out.println("Eroare " + e.getMessage());
            }
        }
    }

    private void adaugaUtilizator(){
        System.out.println("Nume: ");
        String nume = scanner.nextLine();
        System.out.println("Prenume: ");
        String prenume = scanner.nextLine();
        utilizatorService.addUtilizator(nume, prenume);
        System.out.println("Utilizator adaugat cu succes!");
    }

    private void removeUtilizator(){
        System.out.println("ID de sters: ");
        int id = Integer.parseInt(scanner.nextLine());
        utilizatorService.removeUtilizator(id);
        System.out.println("Utilizator sters cu succes!");
    }

    private void showAllUtilizatori(){
        for(Utilizator u :  utilizatorService.getAllUtilizatori()){
            System.out.println(u.toString());
        }
    }

    private void adaugaPrietenie(){
        System.out.println("ID user 1:");
        int idUser1 = Integer.parseInt(scanner.nextLine());
        System.out.println("ID user 2:");
        int idUser2 = Integer.parseInt(scanner.nextLine());
        prietenieService.addPrietenie(idUser1, idUser2);
        System.out.println("Prietenie adaugata cu succes!");
    }

    private void removePrietenie(){
        System.out.println("ID user 1:");
        int idUser1 = Integer.parseInt(scanner.nextLine());
        System.out.println("ID user 2:");
        int idUser2 = Integer.parseInt(scanner.nextLine());
        prietenieService.removePrietenie(idUser1, idUser2);
        System.out.println("Prietenie stearsa cu succes");
    }

    private void showAllPrietenii(){
        for(Prietenie p :  prietenieService.getAllPrietenii()){
            System.out.println(p.toString());
        }
    }
}

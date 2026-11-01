Bucle inlocuite cu stream-uri:
1. In UtilizatorService.addUtilizator: for-ul clasic pentru gasirea ID-ului maxim inlocuit cu stream().mapToInt(Utilizator::getId).max().orElse(0).

2. In UtilizatorService.removeUtilizator: for-ul pentru stergerea utilizatorului din listele prietenilor inlocuit cu toDelete.getPrieteni().forEach().

3. In PrietenieService.sincronizeazaPrietenii: for-ul care cauta si stergea prieteniile invalide a fost inlocuit cu stream().filter(...).map(...).collect(Collectors.toList()).forEach(...).

4. In ComunitateService.numarComunitati: for-ul pentru parcurgerea nodurilor nevizitate a fost inlocuit cu stream().filter(...).map(...).count().

5. In ComunitateService.ceaMaiSociabilaComunitate: for-ul de cautare a maximului global a fost inlocuit cu stream().filter(...).map(...).max(Comparator.comparingInt(...)).orElse(...).

Interfete functionale folosite:
- Predicate<E>: folosit in interfata Repository la metoda default findAll(Predicate<E> filter) ca sa pot trimite conditii de filtrare direct ca lambda (ex: x -> x.getNume() != null).

- Function<T, R>: folosit in RepositoryFactory in loc de switch. Am un Map care primeste un RepositoryType si are ca valori Function<Validator<E>, Repository<ID, E>>. Metoda primeste validatorul si returneaza repository-ul corect pe baza fisierului de configurare.

Alegerea pentru Optional:
Am modificat interfata Repository (si implementarile ei) ca metodele findOne, delete si update sa returneze Optional<E> in loc de null. Am facut asta ca sa nu mai am erori de tip NullPointerException. Acum in Service folosesc direct .orElseThrow() ca sa arunc ServiceException daca nu gasesc utilizatorul, sau .ifPresent() la stergerea prietenilor.

Alegerea pentru file repository:
Am pastrat Template Method (clasa AbstractFileRepository) in loc sa trec la compozitie cu Function in constructor. Codul era deja bine structurat din laboratorul trecut, logica de parsare e izolata bine in clasele copil (UtilizatorFileRepository).
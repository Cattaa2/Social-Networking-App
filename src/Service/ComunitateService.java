package Service;

import domain.Utilizator;

import java.util.*;
import java.util.stream.StreamSupport;


public class ComunitateService {
    private UtilizatorService utilizatorService;

    public ComunitateService(UtilizatorService utilizatorService) {
        this.utilizatorService = utilizatorService;
    }

    private void extrageComunitate(Utilizator u, Set<Integer> vizitate, List<Utilizator> comunitate){
        vizitate.add(u.getId());
        comunitate.add(u);
        u.getPrieteni().stream()
                .filter(prieten -> !vizitate.contains(prieten.getId()))
                .forEach(prieten -> extrageComunitate(prieten, vizitate, comunitate));
    }

    private int dfsLongestPath(Utilizator curent, Set<Integer> pathVisited) {
        pathVisited.add(curent.getId());
        int maxDepth = 0;
        for (Utilizator prieten : curent.getPrieteni()) {
            if (!pathVisited.contains(prieten.getId())) {
                int depth = dfsLongestPath(prieten, pathVisited);
                if (depth > maxDepth) maxDepth = depth;
            }
        }
        pathVisited.remove(curent.getId());
        return maxDepth + 1;
    }

    /**
     * @return numarul componentelor conexe
     */
    public int numarComunitati() {
        Set<Integer> vizitate = new HashSet<>();
        return (int) StreamSupport.stream(utilizatorService.getAllUtilizatori().spliterator(), false)
                .filter(u -> !vizitate.contains(u.getId()))
                .map(u -> {
                    List<Utilizator> comunitate = new ArrayList<>();
                    extrageComunitate(u, vizitate, comunitate);
                    return comunitate;
                })
                .count();
    }

    public List<Utilizator> ceaMaiSociabilaComunitate() {
        Set<Integer> vizitate = new HashSet<>();
        return StreamSupport.stream(utilizatorService.getAllUtilizatori().spliterator(),false)
                .filter(u -> !vizitate.contains(u.getId()))
                .map(u -> {
                    List<Utilizator> comunitate = new ArrayList<>();
                    extrageComunitate(u, vizitate, comunitate);
                    return comunitate;
                }).max(Comparator.comparingInt(comunitate ->
                        comunitate.stream()
                                .mapToInt(u -> dfsLongestPath(u,new HashSet<>()))
                                .max()
                                .orElse(0)))
                .orElse(new ArrayList<>());
    }
}

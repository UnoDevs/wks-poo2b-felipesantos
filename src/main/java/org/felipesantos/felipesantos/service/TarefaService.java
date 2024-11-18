package org.felipesantos.felipesantos.service;

import org.felipesantos.felipesantos.model.Prioridade;
import org.felipesantos.felipesantos.model.Status;
import org.felipesantos.felipesantos.model.Tarefa;
import org.felipesantos.felipesantos.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public Tarefa createTarefa(Tarefa tarefa){
        tarefa.setStatus(Status.A_FAZER);
        return tarefaRepository.save(tarefa);
    }

    public Map<Status,List<Tarefa>> listarTodasTarefas(){
        Map<Status,List<Tarefa>> mapDeTarefas = new HashMap<>();

        // Separando as colunas por tarefas
        mapDeTarefas.put(Status.A_FAZER,ordenarPorPrioridade(listarTodasTarefasPorStatus(Status.A_FAZER)));
        mapDeTarefas.put(Status.EM_PROGRESSO,ordenarPorPrioridade(listarTodasTarefasPorStatus(Status.EM_PROGRESSO)));
        mapDeTarefas.put(Status.CONCLUIDO,ordenarPorPrioridade(listarTodasTarefasPorStatus(Status.CONCLUIDO)));

        return mapDeTarefas;
    }

    public List<Tarefa> listarTodasTarefasPorStatus(Status status){
        return tarefaRepository.findAll().stream().filter(
                tarefa -> tarefa.getStatus() == status).toList();
    }

    public Map<Status,List<Tarefa>> listarTodasTarefasFiltrada(Prioridade priority, LocalDate dueDate){
        Map<Status,List<Tarefa>> mapList = listarTodasTarefas();
        if (priority != null) {
            mapList.replace(Status.A_FAZER,mapList.get(Status.A_FAZER).stream().filter(tarefa -> tarefa.getPriority() == priority).toList());
            mapList.replace(Status.EM_PROGRESSO,mapList.get(Status.EM_PROGRESSO).stream().filter(tarefa -> tarefa.getPriority() == priority).toList());
            mapList.replace(Status.CONCLUIDO,mapList.get(Status.CONCLUIDO).stream().filter(tarefa -> tarefa.getPriority() == priority).toList());
        }


        if (dueDate != null) {
            mapList.replace(Status.A_FAZER,mapList.get(Status.A_FAZER).stream().filter(tarefa -> tarefa.getDueDate().isEqual(dueDate)).toList());
            mapList.replace(Status.EM_PROGRESSO,mapList.get(Status.EM_PROGRESSO).stream().filter(tarefa -> tarefa.getDueDate().isEqual(dueDate)).toList());
            mapList.replace(Status.CONCLUIDO,mapList.get(Status.CONCLUIDO).stream().filter(tarefa -> tarefa.getDueDate().isEqual(dueDate)).toList());
        }

        return mapList;
    }

    public Map<Status, Map<String,List<Tarefa>>> listarTarefasComAtrasadas(){
        Map<Status,List<Tarefa>> mapList = listarTodasTarefas();
        Map<Status, Map<String,List<Tarefa>>> mapListFiltrada = new HashMap<>();

        mapListFiltrada.put(Status.A_FAZER,separarAtrasados(mapList.get(Status.A_FAZER)));
        mapListFiltrada.put(Status.EM_PROGRESSO,separarAtrasados(mapList.get(Status.EM_PROGRESSO)));
        mapListFiltrada.put(Status.CONCLUIDO,separarAtrasados(mapList.get(Status.CONCLUIDO)));

        return mapListFiltrada;
    }

    public Tarefa listarTarefaPorId(int id){
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
        if(tarefaOptional.isPresent()){
            return tarefaOptional.get();
        }
        throw new RuntimeException("Tarefa "+ id +" não encontrada!");
    }

    public Tarefa moverTarefa(int id){
        Tarefa tarefa = listarTarefaPorId(id);
        tarefa.setStatus(proximoStatus(tarefa.getStatus()));
        return tarefaRepository.save(tarefa);
    }

    public Tarefa editarTarefa(int id, Tarefa tarefa){
        Tarefa tarefaEditada = listarTarefaPorId(id);

        tarefaEditada.setTitle(tarefa.getTitle());
        tarefaEditada.setDescription(tarefa.getDescription());
        tarefaEditada.setPriority(tarefa.getPriority());
        tarefaEditada.setDueDate(tarefa.getDueDate());

        return tarefaRepository.save(tarefaEditada);
    }

    public void excluirTarefa(int id){
        tarefaRepository.deleteById(id);
    }

    private Status proximoStatus(Status status){
        if(status.equals(Status.A_FAZER)){
            return Status.EM_PROGRESSO;
        }
        return Status.CONCLUIDO;
    }

    private List<Tarefa> ordenarPorPrioridade(List<Tarefa> tarefas){
        return tarefas.stream().sorted(Comparator.comparingInt(t -> {
            if(t.getPriority() == null){
                return 0;
            }
            return t.getPriority().getValor();
        }))
                .collect(Collectors.toList());
    }

    private Map<String, List<Tarefa>> separarAtrasados(List<Tarefa> tempList){
        List<Tarefa> atrasados = tempList.stream().filter(tarefa -> {
            if(tarefa.getDueDate() == null){
                return false;
            }
            return tarefa.getDueDate().isBefore(LocalDate.now()) && !tarefa.getStatus().equals(Status.CONCLUIDO);
        }).toList();

        List<Tarefa> naoAtrasados = tempList.stream().filter(tarefa -> {
            if(tarefa.getDueDate() == null){
                return true;
            }
            return tarefa.getDueDate().isAfter(LocalDate.now()) || tarefa.getStatus().equals(Status.CONCLUIDO);
        }).toList();

        Map<String, List<Tarefa>> mapList = new HashMap<>();
        mapList.put("atrasados",atrasados);
        mapList.put("registrados",naoAtrasados);

        return mapList;
    }
}

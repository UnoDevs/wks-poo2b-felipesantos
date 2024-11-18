package org.felipesantos.felipesantos.controller;

import org.felipesantos.felipesantos.model.Prioridade;
import org.felipesantos.felipesantos.model.Status;
import org.felipesantos.felipesantos.model.Tarefa;
import org.felipesantos.felipesantos.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TarefaController {
    @Autowired
    private TarefaService tarefaService;

    @PostMapping
    public Tarefa criarTarefa(@RequestBody Tarefa tarefa){
        return tarefaService.createTarefa(tarefa);
    }

    @GetMapping
    public Map<Status, List<Tarefa>> listarTarefas(@RequestParam(required = false) Prioridade priority, @RequestParam(required = false) LocalDate dueDate){
        if (priority != null || dueDate != null) {
            return tarefaService.listarTodasTarefasFiltrada(priority,dueDate);
        }
        return tarefaService.listarTodasTarefas();
    }

    @GetMapping("/report")
    public Map<Status, Map<String,List<Tarefa>>> listarRelatorioDeTarefas(){
        return tarefaService.listarTarefasComAtrasadas();
    }

    @PutMapping("/{id}/move")
    public Tarefa moverTarefa(@PathVariable int id){
        return tarefaService.moverTarefa(id);
    }

    @PutMapping("/{id}")
    public Tarefa editarTarefa(@PathVariable int id, @RequestBody Tarefa tarefa){
        return tarefaService.editarTarefa(id,tarefa);
    }

    @DeleteMapping("/{id}")
    public void deletarTarefa(@PathVariable int id){
        tarefaService.excluirTarefa(id);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> execaoSobreIntegridadeDoBody(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                "Campos obrigatórios informados incorretamente ou ausentes!\nExemplo: Título"
        );
    }
}

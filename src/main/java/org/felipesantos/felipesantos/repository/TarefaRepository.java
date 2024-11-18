package org.felipesantos.felipesantos.repository;

import org.felipesantos.felipesantos.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa,Integer> {
}

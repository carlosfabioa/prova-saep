package com.senai.saep.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senai.saep.model.StatusTarefa;
import com.senai.saep.model.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long>{
	List<Tarefa> findByStatus(StatusTarefa status);

}

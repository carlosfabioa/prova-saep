package com.senai.saep.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senai.saep.model.StatusTarefa;
import com.senai.saep.model.Tarefa;
import com.senai.saep.repository.TarefaRepository;

@Service
public class TarefaService {
	
	@Autowired
	private TarefaRepository tarefaRepository;
	
	public Tarefa salvarTarefa(Tarefa tarefa) {
		return tarefaRepository.save(tarefa);
	}
	
	public List<Tarefa> listarTarefas(){
		return tarefaRepository.findAll();
	}	
	
	public List<Tarefa> listarTarefasPorStatus(StatusTarefa status){
		return tarefaRepository.findByStatus(status);
	}
	
	public Optional<Tarefa> buscarPorId(Long id){
		return tarefaRepository.findById(id);
	}
	
	public Tarefa atualizarTarefa(Tarefa tarefa) {
		return tarefaRepository.save(tarefa);
	}
	
	public void excluirTarefa(Long id) {
		tarefaRepository.deleteById(id);
	}

}

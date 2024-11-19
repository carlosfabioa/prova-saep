package com.senai.saep.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.senai.saep.model.StatusTarefa;
import com.senai.saep.model.Tarefa;
import com.senai.saep.model.Usuario;
import com.senai.saep.services.TarefaService;
import com.senai.saep.services.UsuarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/tarefas")
public class tarefaController {
    
	@Autowired
    private TarefaService tarefaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
    @GetMapping("/nova")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("tarefa", new Tarefa());
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "tarefa/cadastro";
    }
    @PostMapping("/salvar")
    public String cadastrarTarefa(@Valid Tarefa tarefa, BindingResult result, Model model,  RedirectAttributes redirectAttributes) {
    	if (result.hasErrors()) {
            model.addAttribute("tarefa", tarefa);
            model.addAttribute("usuarios", usuarioService.listarUsuarios());
            return "usuario/cadastro"; 
        }
        tarefaService.salvarTarefa(tarefa);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Cadastro concluído com sucesso.");

        return "redirect:/tarefas/gerenciar";
    }
    
    @GetMapping("/gerenciar")
    public String listarTarefas(Model model) {
        model.addAttribute("tarefasAFazer", tarefaService.listarTarefasPorStatus(StatusTarefa.A_FAZER));
        model.addAttribute("tarefasFazendo", tarefaService.listarTarefasPorStatus(StatusTarefa.FAZENDO));
        model.addAttribute("tarefasPronto", tarefaService.listarTarefasPorStatus(StatusTarefa.PRONTO));
        return "tarefa/gerenciar";
    }
    
    @GetMapping("/editar/{id}")
    public String editarTarefa(@PathVariable Long id, Model model) {
        Optional<Tarefa> tarefa = tarefaService.buscarPorId(id);
        if (tarefa.isPresent()) {
            model.addAttribute("tarefa", tarefa.get());
            model.addAttribute("usuarios", usuarioService.listarUsuarios());
            return "tarefa/cadastro";
        } else {
            return "redirect:/tarefas/gerenciar";
        }
    }
    
    @PostMapping("/atualizar")
    public String atualizarTarefa(@ModelAttribute Tarefa tarefa) {
        tarefaService.atualizarTarefa(tarefa);
        return "redirect:/tarefas/gerenciar";
    }

    @GetMapping("/excluir/{id}")
    public String excluirTarefa(@PathVariable("id") Long id) {
        tarefaService.excluirTarefa(id);
        return "redirect:/tarefas/gerenciar";
    }
    
    
    
    @PostMapping("/atualizar-status")
    public String atualizarStatus(@RequestParam Long id, @RequestParam String status) {
        Optional<Tarefa> tarefaOpt = tarefaService.buscarPorId(id);
        if (tarefaOpt.isPresent()) {
            Tarefa tarefa = tarefaOpt.get();
            try {
                StatusTarefa statusEnum = StatusTarefa.valueOf(status);
                tarefa.setStatus(statusEnum);
                tarefaService.atualizarTarefa(tarefa);
            } catch (IllegalArgumentException e) {
                System.out.println("Status inválido: " + status);
            }
        }
        return "redirect:/tarefas/gerenciar";
    }
}
	


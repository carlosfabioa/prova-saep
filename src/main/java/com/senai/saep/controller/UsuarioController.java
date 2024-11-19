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

import com.senai.saep.model.Usuario;
import com.senai.saep.services.UsuarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
    @GetMapping("/novo")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/cadastro";
    }
    
    @PostMapping("/salvar")
    public String cadastrarUsuario(@Valid Usuario usuario, BindingResult result, Model model) {
    	if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "usuario/cadastro"; 
        }
    	try {
            usuarioService.salvarUsuario(usuario);
        } catch (IllegalArgumentException e) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("emailErro", e.getMessage());
            return "usuario/cadastro";
        }
        return "redirect:/usuarios/listar";
    }	
	
    @GetMapping("/listar")
    public String listarUsuarios(Model model) {
		model.addAttribute("usuarios", usuarioService.listarUsuarios());
		return "usuario/lista";
	}

    
	//editar
	@GetMapping("editarUsuario/{id}")
	public String editarUsuario(@PathVariable("id")  Long id, Model modelo) {
		Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
		if(usuarioOpt.isPresent()) {
			modelo.addAttribute("usuario", usuarioOpt.get());
			return "usuario/cadastro";
		}else {
			return "redirect:/usuarios/listar";
		}
	}
	
	//remover
	@GetMapping("/removerUsuario/{id}")
	public String removerUsuario(@PathVariable("id") Long id) {
		usuarioService.excluirUsuario(id);
		return "redirect:/usuarios/listar";
	}
}

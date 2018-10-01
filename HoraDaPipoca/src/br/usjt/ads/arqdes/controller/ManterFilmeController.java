package br.usjt.ads.arqdes.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.usjt.ads.arqdes.model.entity.Filme;
import br.usjt.ads.arqdes.model.entity.Genero;
import br.usjt.ads.arqdes.model.service.FilmeService;
import br.usjt.ads.arqdes.model.service.GeneroService;

@Controller
public class ManterFilmeController {
	
	@Autowired
	private FilmeService filmeService;
	
	@Autowired
	private GeneroService generoService;
	
		
	@RequestMapping("/home")
	public String inicio() {
		return "index";
	}
	
	@RequestMapping("/visualizarFilme")
	public String visualizarFilme(Filme filme) {
			
		return"VisualizarFilme";
	}
	
	@RequestMapping("/listarFilmes")
	public String listarFilmes(HttpSession session) {
		session.setAttribute("lista", null);
		return "ListarFilmes";
	}
	
	@RequestMapping("/novoFilme")
	public String novoFilme(HttpSession session){
		try {
			ArrayList<Genero> generos = generoService.listarGeneros();
			session.setAttribute("generos", generos);
			 return "CriarFilme";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "index";
	}	
		
	@RequestMapping("/inserirFilme")
	public String inserirFilme(@Valid Filme filme, BindingResult result, Model model) {
		try {
			if(!result.hasFieldErrors()) {		
			Genero genero = generoService.buscarGenero(filme.getGenero().getId());
			filme.setGenero(genero);
			model.addAttribute("filme", filme);
		
			filmeService.inserirFilme(filme);
			
				return "VisualizarFilme";
			}else {
				return "redirect:novoFilme";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "index";
	}
	@RequestMapping("/buscarFilmes")
	public String buscarFilmes(HttpSession session, @RequestParam String chave) {
		
		try {
			ArrayList<Filme> lista;
			
			if (chave != null && chave.length() > 0) {
				lista = filmeService.listarFilmes(chave);
			} else {
				lista = filmeService.listarFilmes();
			}
			session.setAttribute("lista", lista);
			return "ListarFilmes";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Erro";
	}
	
	
}

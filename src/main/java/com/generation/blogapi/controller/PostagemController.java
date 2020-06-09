package com.generation.blogapi.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.generation.blogapi.model.Postagem;
import com.generation.blogapi.repository.PostagemRepository;

@RestController
@RequestMapping("/postagem")
@CrossOrigin("*")
public class PostagemController {
	
	@Autowired
	private PostagemRepository postagemRepository;
	
	@GetMapping
	public List<Postagem> listar () {
		return postagemRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> detalhar (@PathVariable Long id) {
		Optional<Postagem> postagem = postagemRepository.findById(id);
		if(postagem.isPresent()) {
		return ResponseEntity.ok(postagem.get());
		}
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> findByTitulo (@PathVariable String titulo) {
		System.out.println(titulo);
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
		
	}

	@PostMapping
	public ResponseEntity<Postagem> criarPostagem (@RequestBody @Valid Postagem postagem, UriComponentsBuilder uriBuilder) {
		Postagem postagemSalva = postagemRepository.save(postagem);
		URI uri = uriBuilder.path("/postagem/{id}").buildAndExpand(postagemSalva.getId()).toUri();
		return ResponseEntity.created(uri).body(postagemSalva);
		
	}
	
	@PutMapping
	public ResponseEntity<Postagem> atualizar (@RequestBody @Valid Postagem postagem) {
		return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));
		
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> delete (@PathVariable Long id) {
		postagemRepository.deleteById(id);
		
		return ResponseEntity.noContent().build();
		
	}
	

}

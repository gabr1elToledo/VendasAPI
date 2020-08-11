package com.qintess.curso.rest.controller;

import com.qintess.curso.domain.entity.Cliente;
import com.qintess.curso.domain.entity.Produto;
import com.qintess.curso.domain.repository.ProdutoRepo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private ProdutoRepo produtoRepo;

    public ProdutoController(ProdutoRepo produtoRepo) {
        this.produtoRepo = produtoRepo;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto save(@RequestBody @Valid Produto produto){
        return produtoRepo.save(produto);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable @Valid Integer id, @RequestBody Produto produto){
        produtoRepo.findById(id).map( p -> {
            produto.setId(p.getId());
            produtoRepo.save(produto);
            return produto;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        produtoRepo.findById(id).map( p -> {
            produtoRepo.delete(p);
            return Void.TYPE;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado"));
    }

    @GetMapping("{id}")
    public Produto getById(@PathVariable Integer id){
        return produtoRepo.findById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado"));
    }

    @GetMapping
    public List<Produto> find(Produto filtro){
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro, matcher);
        return produtoRepo.findAll(example);
    }
}
package com.qintess.curso.rest.controller;

import com.qintess.curso.domain.entity.Cliente;
import com.qintess.curso.domain.repository.ClienteRepo;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@Api("Api Clientes")
public class ClienteController {

    private ClienteRepo clienteRepo;

    public ClienteController(ClienteRepo clienteRepo) {
        this.clienteRepo = clienteRepo;
    }

    @GetMapping("{id}")
    public ResponseEntity getClienteById(@PathVariable Integer id){
        Optional<Cliente> cliente = clienteRepo.findById(id);
        if(cliente.isPresent()){
            return ResponseEntity.ok(cliente.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity save(@RequestBody @Valid Cliente cliente){
        Cliente clienteSalvo = clienteRepo.save(cliente);
        return ResponseEntity.ok(clienteSalvo);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Integer id){
        Optional<Cliente> cliente = clienteRepo.findById(id);

        if(cliente.isPresent()){
            clienteRepo.delete(cliente.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Integer id,@RequestBody @Valid Cliente cliente){
        return clienteRepo.findById(id).map( clienteExistente -> {
            cliente.setId(clienteExistente.getId());
            clienteRepo.save(cliente);
            return ResponseEntity.noContent().build();
        }).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @GetMapping
    public ResponseEntity find(Cliente filtro){
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro, matcher);
        List<Cliente> lista = clienteRepo.findAll(example);
        return ResponseEntity.ok(lista);
    }

}

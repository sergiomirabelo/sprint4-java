package br.com.motiva.controller;

import br.com.motiva.model.IntervencaoOperacional;
import br.com.motiva.service.IntervencaoOperacionalService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/intervencoes")
public class IntervencaoOperacionalController {

    private final IntervencaoOperacionalService service;

    public IntervencaoOperacionalController(IntervencaoOperacionalService service) {
        this.service = service;
    }

    @GetMapping
    public List<IntervencaoOperacional> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IntervencaoOperacional> buscar(@PathVariable Long id) {
        return service.buscar(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<IntervencaoOperacional> criar(@RequestBody IntervencaoOperacional intervencao) {
        return ResponseEntity.status(201).body(service.criar(intervencao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IntervencaoOperacional> atualizar(@PathVariable Long id,
                                                            @RequestBody IntervencaoOperacional intervencao) {
        return service.atualizar(id, intervencao).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        return service.remover(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{status}")
    public List<IntervencaoOperacional> porStatus(@PathVariable String status) {
        return service.buscarPorStatus(status);
    }
}

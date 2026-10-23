package br.com.motiva.controller;

import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.service.TrechoRodoviaService;
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
@RequestMapping("/api/trechos")
public class TrechoRodoviaController {

    private final TrechoRodoviaService service;

    public TrechoRodoviaController(TrechoRodoviaService service) {
        this.service = service;
    }

    @GetMapping
    public List<TrechoRodovia> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrechoRodovia> buscar(@PathVariable Long id) {
        return service.buscar(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TrechoRodovia> criar(@RequestBody TrechoRodovia trecho) {
        return ResponseEntity.status(201).body(service.criar(trecho));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrechoRodovia> atualizar(@PathVariable Long id, @RequestBody TrechoRodovia trecho) {
        return service.atualizar(id, trecho).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        return service.remover(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/densidade/{densidade}")
    public List<TrechoRodovia> porDensidade(@PathVariable String densidade) {
        return service.buscarPorDensidade(densidade);
    }

    @GetMapping("/altura-minima/{cm}")
    public List<TrechoRodovia> porAlturaMinima(@PathVariable Double cm) {
        return service.buscarPorAlturaMinima(cm);
    }
}

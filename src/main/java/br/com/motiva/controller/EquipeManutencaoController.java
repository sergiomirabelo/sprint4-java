package br.com.motiva.controller;

import br.com.motiva.model.EquipeManutencao;
import br.com.motiva.service.EquipeManutencaoService;
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
@RequestMapping("/api/equipes")
public class EquipeManutencaoController {

    private final EquipeManutencaoService service;

    public EquipeManutencaoController(EquipeManutencaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<EquipeManutencao> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipeManutencao> buscar(@PathVariable Long id) {
        return service.buscar(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EquipeManutencao> criar(@RequestBody EquipeManutencao equipe) {
        return ResponseEntity.status(201).body(service.criar(equipe));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipeManutencao> atualizar(@PathVariable Long id, @RequestBody EquipeManutencao equipe) {
        return service.atualizar(id, equipe).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        return service.remover(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{status}")
    public List<EquipeManutencao> porStatus(@PathVariable String status) {
        return service.buscarPorStatus(status);
    }
}

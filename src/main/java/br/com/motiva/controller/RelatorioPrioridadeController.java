package br.com.motiva.controller;

import br.com.motiva.model.RelatorioPrioridade;
import br.com.motiva.service.GeradorRelatorio;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioPrioridadeController {

    private final GeradorRelatorio gerador;

    public RelatorioPrioridadeController(GeradorRelatorio gerador) {
        this.gerador = gerador;
    }

    @PostMapping
    public ResponseEntity<RelatorioPrioridade> gerar() {
        return ResponseEntity.status(201).body(gerador.gerar());
    }

    @GetMapping
    public List<RelatorioPrioridade> historico() {
        return gerador.historico();
    }

    @GetMapping("/periodo")
    public List<RelatorioPrioridade> porPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return gerador.porPeriodo(inicio, fim);
    }
}

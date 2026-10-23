package br.com.motiva.service;

import br.com.motiva.model.RelatorioPrioridade;
import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.RelatorioPrioridadeRepository;
import br.com.motiva.repository.TrechoRodoviaRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/** Motor de prioridade (Sprint 2/3), agora como @Service. As regras sao as mesmas de antes. */
@Service
public class GeradorRelatorio {

    // Limite regulatorio/contratual: acima de 30 cm acarreta multa
    public static final double LIMITE_CONTRATUAL_GRAMA_CM = 30.0;

    private final TrechoRodoviaRepository trechoRepository;
    private final RelatorioPrioridadeRepository relatorioRepository;

    public GeradorRelatorio(TrechoRodoviaRepository trechoRepository,
                            RelatorioPrioridadeRepository relatorioRepository) {
        this.trechoRepository = trechoRepository;
        this.relatorioRepository = relatorioRepository;
    }

    public RelatorioPrioridade gerar() {
        int urgente = 0, critico = 0, atencao = 0, normal = 0, sobMulta = 0;
        StringBuilder detalhes = new StringBuilder(
                "=== RELATÓRIO DE MONITORAMENTO DE VEGETAÇÃO E ROÇAGEM - MOTIVA ===\n");
        detalhes.append("Regra Regulatória: Altura máxima permitida = 30 cm (acima deste limite gera MULTA)\n\n");

        for (TrechoRodovia t : trechoRepository.findAll()) {
            String prioridade = classificar(t);
            boolean emInfracao = t.getAlturaGramaCm() > LIMITE_CONTRATUAL_GRAMA_CM;
            if (emInfracao) sobMulta++;

            detalhes.append(String.format(
                    "[%s%s] %s (%s, km %.1f-%.1f | %s) -> Altura Grama: %.1f cm | Densidade: %s | Dias s/ corte: %d%n",
                    prioridade, emInfracao ? " [! SUJEITO A MULTA !]" : " [Conforme]",
                    t.getCodigo(), t.getRodovia(), t.getKmInicio(), t.getKmFim(), t.getMunicipio(),
                    t.getAlturaGramaCm(), t.getDensidadeVegetacao(), t.getDiasSemCorte()));

            switch (prioridade) {
                case "URGENTE" -> urgente++;
                case "CRITICO" -> critico++;
                case "ATENCAO" -> atencao++;
                default -> normal++;
            }
        }

        detalhes.append("\nResumo: Urgente: ").append(urgente)
                .append(" (Trechos autuáveis/multa: ").append(sobMulta).append(")")
                .append(" | Crítico: ").append(critico)
                .append(" | Atenção: ").append(atencao)
                .append(" | Normal: ").append(normal).append("\n");

        RelatorioPrioridade r = new RelatorioPrioridade();
        r.setDataGeracao(LocalDateTime.now());
        r.setQtUrgente(urgente);
        r.setQtCritico(critico);
        r.setQtAtencao(atencao);
        r.setQtNormal(normal);
        r.setResumo(detalhes.toString());
        return relatorioRepository.save(r);
    }

    public List<RelatorioPrioridade> historico() {
        return relatorioRepository.findAll();
    }

    public List<RelatorioPrioridade> porPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("inicio não pode ser depois de fim");
        }
        return relatorioRepository.findByDataGeracaoBetween(inicio, fim);
    }

    // Mesmos criterios da Sprint 3
    String classificar(TrechoRodovia t) {
        double altura = t.getAlturaGramaCm();
        String densidade = t.getDensidadeVegetacao();
        int dias = t.getDiasSemCorte();

        if (altura >= LIMITE_CONTRATUAL_GRAMA_CM || "CRITICA".equalsIgnoreCase(densidade) || dias >= 30) {
            return "URGENTE";
        }
        if (altura >= 25.0 || "ALTA".equalsIgnoreCase(densidade) || dias >= 20) {
            return "CRITICO";
        }
        if (altura >= 18.0 || "MEDIA".equalsIgnoreCase(densidade) || dias >= 10) {
            return "ATENCAO";
        }
        return "NORMAL";
    }
}

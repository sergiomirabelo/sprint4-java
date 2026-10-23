package br.com.motiva.service;

import br.com.motiva.model.IntervencaoOperacional;
import br.com.motiva.repository.EquipeManutencaoRepository;
import br.com.motiva.repository.IntervencaoOperacionalRepository;
import br.com.motiva.repository.TrechoRodoviaRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class IntervencaoOperacionalService {

    private static final Set<String> TIPOS =
            Set.of("ROCAGEM_MECANICA", "ROCAGEM_MANUAL", "CAPINA_MANUAL", "PODA_VEGETACAO");
    private static final Set<String> STATUS =
            Set.of("PLANEJADA", "EM_ANDAMENTO", "CONCLUIDA", "CANCELADA");

    private final IntervencaoOperacionalRepository repository;
    private final TrechoRodoviaRepository trechoRepository;
    private final EquipeManutencaoRepository equipeRepository;

    public IntervencaoOperacionalService(IntervencaoOperacionalRepository repository,
                                         TrechoRodoviaRepository trechoRepository,
                                         EquipeManutencaoRepository equipeRepository) {
        this.repository = repository;
        this.trechoRepository = trechoRepository;
        this.equipeRepository = equipeRepository;
    }

    public List<IntervencaoOperacional> listar() {
        return repository.findAll();
    }

    public Optional<IntervencaoOperacional> buscar(Long id) {
        return repository.findById(id);
    }

    public IntervencaoOperacional criar(IntervencaoOperacional i) {
        validar(i);
        i.setId(null);
        return repository.save(i);
    }

    public Optional<IntervencaoOperacional> atualizar(Long id, IntervencaoOperacional dados) {
        validar(dados);
        return repository.findById(id).map(i -> {
            i.setTrechoId(dados.getTrechoId());
            i.setEquipeId(dados.getEquipeId());
            i.setDescricao(dados.getDescricao());
            i.setTipo(dados.getTipo());
            i.setDataIntervencao(dados.getDataIntervencao());
            i.setStatus(dados.getStatus());
            i.setCusto(dados.getCusto());
            return repository.save(i);
        });
    }

    public boolean remover(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public List<IntervencaoOperacional> buscarPorStatus(String status) {
        return repository.findByStatus(status.toUpperCase());
    }

    private void validar(IntervencaoOperacional i) {
        if (i.getDescricao() == null || i.getDescricao().isBlank()) {
            throw new IllegalArgumentException("descricao é obrigatória");
        }
        if (i.getTipo() == null || !TIPOS.contains(i.getTipo().toUpperCase())) {
            throw new IllegalArgumentException(
                    "tipo deve ser ROCAGEM_MECANICA, ROCAGEM_MANUAL, CAPINA_MANUAL ou PODA_VEGETACAO");
        }
        i.setTipo(i.getTipo().toUpperCase());
        if (i.getStatus() == null || !STATUS.contains(i.getStatus().toUpperCase())) {
            throw new IllegalArgumentException(
                    "status deve ser PLANEJADA, EM_ANDAMENTO, CONCLUIDA ou CANCELADA");
        }
        i.setStatus(i.getStatus().toUpperCase());
        if (i.getDataIntervencao() == null) {
            throw new IllegalArgumentException("dataIntervencao é obrigatória (formato AAAA-MM-DD)");
        }
        if (i.getCusto() == null || i.getCusto().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("custo deve ser maior ou igual a 0");
        }
        if (i.getTrechoId() == null || !trechoRepository.existsById(i.getTrechoId())) {
            throw new IllegalArgumentException("trechoId não existe");
        }
        if (i.getEquipeId() == null || !equipeRepository.existsById(i.getEquipeId())) {
            throw new IllegalArgumentException("equipeId não existe");
        }
    }
}

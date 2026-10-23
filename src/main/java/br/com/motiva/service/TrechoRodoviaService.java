package br.com.motiva.service;

import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.TrechoRodoviaRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class TrechoRodoviaService {

    private static final Set<String> DENSIDADES = Set.of("BAIXA", "MEDIA", "ALTA", "CRITICA");

    private final TrechoRodoviaRepository repository;

    public TrechoRodoviaService(TrechoRodoviaRepository repository) {
        this.repository = repository;
    }

    public List<TrechoRodovia> listar() {
        return repository.findAll();
    }

    public Optional<TrechoRodovia> buscar(Long id) {
        return repository.findById(id);
    }

    public TrechoRodovia criar(TrechoRodovia trecho) {
        validar(trecho);
        trecho.setId(null);
        return repository.save(trecho);
    }

    public Optional<TrechoRodovia> atualizar(Long id, TrechoRodovia dados) {
        validar(dados);
        return repository.findById(id).map(t -> {
            t.setCodigo(dados.getCodigo());
            t.setRodovia(dados.getRodovia());
            t.setKmInicio(dados.getKmInicio());
            t.setKmFim(dados.getKmFim());
            t.setMunicipio(dados.getMunicipio());
            t.setAlturaGramaCm(dados.getAlturaGramaCm());
            t.setDensidadeVegetacao(dados.getDensidadeVegetacao());
            t.setDiasSemCorte(dados.getDiasSemCorte());
            t.setUltimaVistoria(dados.getUltimaVistoria());
            return repository.save(t);
        });
    }

    public boolean remover(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public List<TrechoRodovia> buscarPorDensidade(String densidade) {
        return repository.findByDensidadeVegetacao(densidade.toUpperCase());
    }

    public List<TrechoRodovia> buscarPorAlturaMinima(Double minimo) {
        return repository.findByAlturaGramaCmGreaterThanEqual(minimo);
    }

    // Validacoes da Sprint 1 (encapsulamento do dominio) ficam aqui, nao no Controller
    private void validar(TrechoRodovia t) {
        if (vazio(t.getCodigo())) throw new IllegalArgumentException("codigo é obrigatório");
        if (vazio(t.getRodovia())) throw new IllegalArgumentException("rodovia é obrigatória");
        if (vazio(t.getMunicipio())) throw new IllegalArgumentException("municipio é obrigatório");
        if (t.getKmInicio() == null || t.getKmFim() == null) {
            throw new IllegalArgumentException("kmInicio e kmFim são obrigatórios");
        }
        if (t.getKmFim() <= t.getKmInicio()) {
            throw new IllegalArgumentException("kmFim deve ser maior que kmInicio");
        }
        if (t.getAlturaGramaCm() == null || t.getAlturaGramaCm() < 0) {
            throw new IllegalArgumentException("alturaGramaCm deve ser maior ou igual a 0");
        }
        if (vazio(t.getDensidadeVegetacao()) || !DENSIDADES.contains(t.getDensidadeVegetacao().toUpperCase())) {
            throw new IllegalArgumentException("densidadeVegetacao deve ser BAIXA, MEDIA, ALTA ou CRITICA");
        }
        t.setDensidadeVegetacao(t.getDensidadeVegetacao().toUpperCase());
        if (t.getDiasSemCorte() == null || t.getDiasSemCorte() < 0) {
            throw new IllegalArgumentException("diasSemCorte deve ser maior ou igual a 0");
        }
        if (t.getUltimaVistoria() == null) {
            throw new IllegalArgumentException("ultimaVistoria é obrigatória (formato AAAA-MM-DD)");
        }
    }

    private boolean vazio(String s) {
        return s == null || s.isBlank();
    }
}

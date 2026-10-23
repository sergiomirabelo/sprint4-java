package br.com.motiva.service;

import br.com.motiva.model.EquipeManutencao;
import br.com.motiva.repository.EquipeManutencaoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EquipeManutencaoService {

    private final EquipeManutencaoRepository repository;

    public EquipeManutencaoService(EquipeManutencaoRepository repository) {
        this.repository = repository;
    }

    public List<EquipeManutencao> listar() {
        return repository.findAll();
    }

    public Optional<EquipeManutencao> buscar(Long id) {
        return repository.findById(id);
    }

    public EquipeManutencao criar(EquipeManutencao equipe) {
        validar(equipe);
        equipe.setId(null);
        return repository.save(equipe);
    }

    public Optional<EquipeManutencao> atualizar(Long id, EquipeManutencao dados) {
        validar(dados);
        return repository.findById(id).map(e -> {
            e.setNome(dados.getNome());
            e.setEspecialidade(dados.getEspecialidade());
            e.setQuantidadeMembros(dados.getQuantidadeMembros());
            e.setTelefone(dados.getTelefone());
            e.setStatus(dados.getStatus());
            return repository.save(e);
        });
    }

    public boolean remover(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public List<EquipeManutencao> buscarPorStatus(String status) {
        return repository.findByStatus(status.toUpperCase());
    }

    private void validar(EquipeManutencao e) {
        if (e.getNome() == null || e.getNome().isBlank()) {
            throw new IllegalArgumentException("nome é obrigatório");
        }
        if (e.getEspecialidade() == null || e.getEspecialidade().isBlank()) {
            throw new IllegalArgumentException("especialidade é obrigatória");
        }
        if (e.getQuantidadeMembros() == null || e.getQuantidadeMembros() <= 0) {
            throw new IllegalArgumentException("quantidadeMembros deve ser maior que 0");
        }
        if (e.getStatus() == null
                || !(e.getStatus().equalsIgnoreCase("ATIVA") || e.getStatus().equalsIgnoreCase("INATIVA"))) {
            throw new IllegalArgumentException("status deve ser ATIVA ou INATIVA");
        }
        e.setStatus(e.getStatus().toUpperCase());
    }
}

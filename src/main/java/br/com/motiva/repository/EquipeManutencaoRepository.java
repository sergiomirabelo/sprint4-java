package br.com.motiva.repository;

import br.com.motiva.model.EquipeManutencao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipeManutencaoRepository extends JpaRepository<EquipeManutencao, Long> {

    List<EquipeManutencao> findByStatus(String status);

    List<EquipeManutencao> findByEspecialidade(String especialidade);
}

package br.com.motiva.repository;

import br.com.motiva.model.IntervencaoOperacional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntervencaoOperacionalRepository extends JpaRepository<IntervencaoOperacional, Long> {

    List<IntervencaoOperacional> findByStatus(String status);

    List<IntervencaoOperacional> findByTrechoId(Long trechoId);
}

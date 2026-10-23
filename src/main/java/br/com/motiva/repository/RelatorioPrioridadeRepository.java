package br.com.motiva.repository;

import br.com.motiva.model.RelatorioPrioridade;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelatorioPrioridadeRepository extends JpaRepository<RelatorioPrioridade, Long> {

    List<RelatorioPrioridade> findByDataGeracaoBetween(LocalDateTime inicio, LocalDateTime fim);
}

package br.com.motiva.repository;

import br.com.motiva.model.TrechoRodovia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrechoRodoviaRepository extends JpaRepository<TrechoRodovia, Long> {

    List<TrechoRodovia> findByAlturaGramaCmGreaterThanEqual(Double minimo);

    List<TrechoRodovia> findByDensidadeVegetacao(String densidadeVegetacao);

    List<TrechoRodovia> findByRodovia(String rodovia);
}

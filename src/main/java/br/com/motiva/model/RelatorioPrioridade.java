package br.com.motiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "RELATORIO_PRIORIDADE")
public class RelatorioPrioridade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataGeracao;
    private Integer qtUrgente;
    private Integer qtCritico;
    private Integer qtAtencao;
    private Integer qtNormal;

    @Lob
    @Column(nullable = false)
    private String resumo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDataGeracao() { return dataGeracao; }
    public void setDataGeracao(LocalDateTime dataGeracao) { this.dataGeracao = dataGeracao; }
    public Integer getQtUrgente() { return qtUrgente; }
    public void setQtUrgente(Integer qtUrgente) { this.qtUrgente = qtUrgente; }
    public Integer getQtCritico() { return qtCritico; }
    public void setQtCritico(Integer qtCritico) { this.qtCritico = qtCritico; }
    public Integer getQtAtencao() { return qtAtencao; }
    public void setQtAtencao(Integer qtAtencao) { this.qtAtencao = qtAtencao; }
    public Integer getQtNormal() { return qtNormal; }
    public void setQtNormal(Integer qtNormal) { this.qtNormal = qtNormal; }
    public String getResumo() { return resumo; }
    public void setResumo(String resumo) { this.resumo = resumo; }
}

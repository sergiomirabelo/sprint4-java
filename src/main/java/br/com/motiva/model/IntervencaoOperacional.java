package br.com.motiva.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "INTERVENCAO_OPERACIONAL")
public class IntervencaoOperacional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long trechoId;
    private Long equipeId;
    private String descricao;
    private String tipo;
    private LocalDate dataIntervencao;
    private String status;
    private BigDecimal custo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTrechoId() { return trechoId; }
    public void setTrechoId(Long trechoId) { this.trechoId = trechoId; }
    public Long getEquipeId() { return equipeId; }
    public void setEquipeId(Long equipeId) { this.equipeId = equipeId; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public LocalDate getDataIntervencao() { return dataIntervencao; }
    public void setDataIntervencao(LocalDate dataIntervencao) { this.dataIntervencao = dataIntervencao; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getCusto() { return custo; }
    public void setCusto(BigDecimal custo) { this.custo = custo; }
}

package br.com.caelum.agiletickets.models;

import static com.google.common.collect.Lists.newArrayList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

@Entity
public class Espetaculo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private String descricao;

	@Enumerated(EnumType.STRING)
	private TipoDeEspetaculo tipo;

	@ManyToOne
	private Estabelecimento estabelecimento;
	
	@OneToMany(mappedBy="espetaculo")
	private List<Sessao> sessoes = newArrayList();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoDeEspetaculo getTipo() {
		return tipo;
	}

	public void setTipo(TipoDeEspetaculo tipo) {
		this.tipo = tipo;
	}

	public void setEstabelecimento(Estabelecimento estabelecimento) {
		this.estabelecimento = estabelecimento;
	}

	public Estabelecimento getEstabelecimento() {
		return estabelecimento;
	}
	
	public List<Sessao> getSessoes() {
		return sessoes;
	}
	
	/**
     * Esse metodo eh responsavel por criar sessoes para
     * o respectivo espetaculo, dado o intervalo de inicio e fim,
     * mais a periodicidade.
     * 
     * O algoritmo funciona da seguinte forma:
     * - Caso a data de inicio seja 01/01/2010, a data de fim seja 03/01/2010,
     * e a periodicidade seja DIARIA, o algoritmo cria 3 sessoes, uma 
     * para cada dia: 01/01, 02/01 e 03/01.
     * 
     * - Caso a data de inicio seja 01/01/2010, a data fim seja 31/01/2010,
     * e a periodicidade seja SEMANAL, o algoritmo cria 5 sessoes, uma
     * a cada 7 dias: 01/01, 08/01, 15/01, 22/01 e 29/01.
     * 
     * Repare que a data da primeira sessao é sempre a data inicial.
     */
	public List<Sessao> criaSessoes(LocalDate inicio, LocalDate fim, LocalTime horario, Periodicidade periodicidade) {
		// ALUNO: Não apague esse metodo. Esse sim será usado no futuro! ;)
		List<Sessao> lista = new ArrayList<Sessao>();
		if(inicio.isAfter(fim)){
			//inicio antes do fim
			return lista;
		}
		LocalDate data = inicio; 
		while(!data.isAfter(fim)){
			
			Sessao sessao = new Sessao();
			sessao.setDuracaoEmMinutos(60);
			sessao.setEspetaculo(this);
			sessao.setIngressosReservados(0);
			sessao.setInicio(data.toDateTime(horario));
			sessao.setPreco(BigDecimal.TEN);
			sessao.setTotalIngressos(100);
			
			lista.add(sessao);
			data = data.plusDays(periodicidade.getDias());
		}
		return lista;
	}
	
	public boolean Vagas(int qtd, int min)
   {
       // ALUNO: Não apague esse metodo. Esse sim será usado no futuro! ;)
       int totDisp = 0;

       for (Sessao s : sessoes)
       {
           if (s.getIngressosDisponiveis() < min) return false;
           totDisp += s.getIngressosDisponiveis();
       }

       if (totDisp >= qtd) return true;
       else return false;
   }

   public boolean Vagas(int qtd)
   {
       // ALUNO: Não apague esse metodo. Esse sim será usado no futuro! ;)
       int totDisp = 0;

       for (Sessao s : sessoes)
       {
           totDisp += s.getIngressosDisponiveis();
       }

       if (totDisp >= qtd) return true;
       else return false;
   }

	public BigDecimal calcularPreco(Sessao sessao) {
		BigDecimal preco;
		preco = calcularPrecoPorTipoEspetaculo(sessao);
		return preco;
	}

	private BigDecimal calcularPrecoPorTipoEspetaculo(Sessao sessao) {
		BigDecimal preco;
		TipoDeEspetaculo tipo = getTipo();
		if(tipo.equals(TipoDeEspetaculo.CINEMA) || tipo.equals(TipoDeEspetaculo.SHOW)) {
			preco = sessao.getEspetaculo().calculaPrecoCinemaShow(sessao);
		} else if(tipo.equals(TipoDeEspetaculo.BALLET) || tipo.equals(TipoDeEspetaculo.ORQUESTRA)) {
			preco = sessao.getEspetaculo().calculaPrecoBalletOrquestra(sessao);
		} else {
			//nao aplica aumento para teatro (quem vai é pobretão)
			preco = sessao.getPreco();
		}
		return preco;
	}

	private BigDecimal calculaPrecoCinemaShow(Sessao sessao) {
		Integer totalIngressos = sessao.getTotalIngressos();
		Integer ingressosReservados = sessao.getIngressosReservados(); 
		BigDecimal precoSessao = sessao.getPreco();
		BigDecimal preco;
		//quando estiver acabando os ingressos... 
		if((totalIngressos - ingressosReservados) / totalIngressos.doubleValue() <= 0.05) { 
			preco = precoSessao.add(precoSessao.multiply(BigDecimal.valueOf(0.10)));
		} else {
			preco = precoSessao;
		}
		return preco;
	}
	
	private BigDecimal calculaPrecoBalletOrquestra(Sessao sessao) {
		Integer totalIngressos = sessao.getTotalIngressos();
		Integer ingressosReservados = sessao.getIngressosReservados(); 
		BigDecimal precoSessao = sessao.getPreco();
		BigDecimal preco;
		if((totalIngressos - ingressosReservados) / totalIngressos.doubleValue() <= 0.50) { 
			preco = precoSessao.add(precoSessao.multiply(BigDecimal.valueOf(0.20)));
		} else {
			preco = precoSessao;
		}
		
		if(sessao.getDuracaoEmMinutos() > 60){
			preco = preco.add(precoSessao.multiply(BigDecimal.valueOf(0.10)));
		}
		return preco;
	}
}

package br.com.caelum.agiletickets.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

public class EspetaculoTest {

	@Test
	public void deveInformarSeEhPossivelReservarAQuantidadeDeIngressosDentroDeQualquerDasSessoes() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(1));
		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(2));

		assertTrue(ivete.Vagas(5));
	}

	@Test
	public void deveInformarSeEhPossivelReservarAQuantidadeExataDeIngressosDentroDeQualquerDasSessoes() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(1));
		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(2));

		assertTrue(ivete.Vagas(6));
	}

	@Test
	public void DeveInformarSeNaoEhPossivelReservarAQuantidadeDeIngressosDentroDeQualquerDasSessoes() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(1));
		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(2));

		assertFalse(ivete.Vagas(15));
	}

	@Test
	public void DeveInformarSeEhPossivelReservarAQuantidadeDeIngressosDentroDeQualquerDasSessoesComUmMinimoPorSessao() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(4));

		assertTrue(ivete.Vagas(5, 3));
	}

	@Test
	public void DeveInformarSeEhPossivelReservarAQuantidadeExataDeIngressosDentroDeQualquerDasSessoesComUmMinimoPorSessao() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(3));
		ivete.getSessoes().add(sessaoComIngressosSobrando(4));

		assertTrue(ivete.Vagas(10, 3));
	}

	@Test
	public void DeveInformarSeNaoEhPossivelReservarAQuantidadeDeIngressosDentroDeQualquerDasSessoesComUmMinimoPorSessao() {
		Espetaculo ivete = new Espetaculo();

		ivete.getSessoes().add(sessaoComIngressosSobrando(2));
		ivete.getSessoes().add(sessaoComIngressosSobrando(2));
		ivete.getSessoes().add(sessaoComIngressosSobrando(2));

		assertFalse(ivete.Vagas(5, 3));
	}

	private Sessao sessaoComIngressosSobrando(int quantidade) {
		Sessao sessao = new Sessao();
		sessao.setTotalIngressos(quantidade * 2);
		sessao.setIngressosReservados(quantidade);

		return sessao;
	}
	
	@Test
	public void cadastrarSessaoComDataInicioMaiorQueDataFim(){ 
		Espetaculo iveteEspetaculo = new Espetaculo();
		LocalDate dataInicio = new LocalDate(2016, 12, 10);
		LocalDate dataFim = new LocalDate(2016, 9, 10);
		LocalTime horario = new LocalTime(17, 0);
		List<Sessao> sessoes = iveteEspetaculo.criaSessoes(dataInicio, dataFim, horario, Periodicidade.SEMANAL);
		
		Assert.assertEquals(0, sessoes.size());
	}
	
	@Test
	public void cadastrarSessaoComDataInicioMenorQueDataFimEPeriodoSemanal(){ 
		Espetaculo iveteEspetaculo = new Espetaculo();
		LocalDate dataInicio = new LocalDate(2016, 12, 10);
		LocalDate dataFim = dataInicio.plusDays(30);
		
		LocalTime horario = new LocalTime(17, 0);
		List<Sessao> sessoes = iveteEspetaculo.criaSessoes(dataInicio, dataFim, horario, Periodicidade.SEMANAL);
		
		Assert.assertEquals(1+30/7,sessoes.size());
	}
	
	@Test
	public void cadastrarSessaoComDataInicioMenorQueDataFimEPeriodoDiario(){ 
		Espetaculo iveteEspetaculo = new Espetaculo();
		LocalDate dataInicio = new LocalDate(2016, 11, 10);
		LocalDate dataFim = dataInicio.plusDays(30);
		LocalTime horario = new LocalTime(17, 0);
		List<Sessao> sessoes = iveteEspetaculo.criaSessoes(dataInicio, dataFim, horario, Periodicidade.DIARIA);
		
		Assert.assertEquals(31,sessoes.size());
	}
	
}

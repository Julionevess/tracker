package com.jns.user;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Usuario implements Serializable{
	
	private Long id;
	private String login;
	private String nome;
	private String senha;
	private String permissao;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setPermissao(String permissao) {
		this.permissao = permissao;
	}

	public String getPermissao() {
		return permissao;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
	
	
	
	

}

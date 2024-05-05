package controller;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.DAO;
import model.JavaBeans;

@WebServlet(urlPatterns = { "/Controller", "/main", "/insert", "/select", "/update"})
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	DAO dao = new DAO();
	JavaBeans contato = new JavaBeans();

	public Controller() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		// Mapeamento
		String action = request.getServletPath();
		System.out.println(action);

		if (action.equals("/main")) {
			contatos(request, response);
		
		} else if (action.equals("/insert")) {
			novoContato(request, response);
		
		} else if (action.equals("/select")) {
			selecionarContato(request,response);
			
		} else if (action.equals("/update")){
			editarContato(request,response);
			
		}
		else {
			response.sendRedirect("index.html");
		}

	}
	
	// Listar Contatos
	protected void contatos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<JavaBeans> lista = dao.listarContatos();
		/*
		for (int i = 0; i < lista.size(); i++) {
			System.out.println(lista.get(i).getIdcon());
			System.out.println(lista.get(i).getNome());
			System.out.println(lista.get(i).getFone());
			System.out.println(lista.get(i).getEmail());
		}*/
		// Encaminhar a lista ao documento agenda.jsp
		request.setAttribute("contatos", lista);
		RequestDispatcher rd = request.getRequestDispatcher("agenda.jsp");
		rd.forward(request, response);
		
	}
	
	// Novo Contato
	protected void novoContato(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		
		//JavaBeans
		contato.setNome(request.getParameter("nome").trim());
		contato.setFone(request.getParameter("fone").trim());
		contato.setEmail(request.getParameter("email").trim());
		
		// invocar o método inserirContato() passando o objeto contato
		dao.inserirContato(contato);
		response.sendRedirect("main");
		
	}
	
	// Selecionar Contato
	protected void selecionarContato (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		
		contato.setIdcon(request.getParameter("idcon"));
		// Selectionar contato no banco de dados e transferindo para javabeans
		dao.selecionarContato(contato);				
		
		// Setar atributo do formulário 
		request.setAttribute("idcon", contato.getIdcon());
		request.setAttribute("nome", contato.getNome());
		request.setAttribute("fone", contato.getFone());
		request.setAttribute("email", contato.getEmail());

		//Encaminha daods para edição
		RequestDispatcher rd = request.getRequestDispatcher("editarcontato.jsp");
		rd.forward(request, response);
		
	}
	
	//Atualizar Contato
	protected void editarContato(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		contato.setNome(request.getParameter("nome"));
		contato.setEmail(request.getParameter("email"));
		contato.setFone(request.getParameter("fone"));
		
		dao.editarContato(contato);
		response.sendRedirect("main");
		
	}
	
	

}

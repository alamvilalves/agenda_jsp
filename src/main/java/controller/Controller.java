package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import model.DAO;
import model.JavaBeans;

@WebServlet(urlPatterns = { "/Controller", "/main", "/insert", "/select", "/update", "/delete","/report"})
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	DAO dao = new DAO();
	JavaBeans contato = new JavaBeans();

	public Controller() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		// Mapeamento
		String action = request.getServletPath();
		System.out.println(action);

		if (action.equals("/main"))
			contatos(request, response);
		else if (action.equals("/insert"))
			novoContato(request, response);
		else if (action.equals("/select"))
			selecionarContato(request,response);
		else if (action.equals("/update"))
			editarContato(request,response);
		else if (action.equals("/delete"))
			deletarContato(request,response);
		else if (action.equals("/report"))
			gerarRelatorio(request,response);
		else 
			response.sendRedirect("index.html");

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
	
	//Deletar Contato
	protected void deletarContato(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException{
		System.out.println(contato.getIdcon());
		System.out.println(contato.getNome());
		System.out.println(contato.getFone());
		System.out.println(contato.getEmail());
		
		contato.reset();
		contato.setIdcon(request.getParameter("idcon"));
		
		dao.deletarContato(request.getParameter("idcon"));
		response.sendRedirect("main");
	}
	
	//Gerar Relatório
	protected void gerarRelatorio(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		
		Document documento = new Document();
		try {
			//tipo de conteúdo
			response.setContentType("application/pdf");
			// Nome do documento
			response.addHeader("Content-Disposition", "inline; filename=contatos.pdf");
			//Criar documento
			PdfWriter.getInstance(documento, response.getOutputStream());
			// Abrir o documento -> conteúdo
			documento.open();
			documento.add(new Paragraph("Lista de contatos:"));
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			documento.close();
		}
	}
	

}

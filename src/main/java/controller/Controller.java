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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
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
		String action = request.getServletPath();
		System.out.println(action);

		if (action.equals("/main"))
			contatos(request, response);
		else if (action.equals("/insert"))
			adicionarContato(request, response);
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
	
	protected void contatos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ArrayList<JavaBeans> lista = dao.listarContatos();
		
		request.setAttribute("contatos", lista);
		RequestDispatcher rd = request.getRequestDispatcher("agenda.jsp");
		rd.forward(request, response);
	}
	
	protected void adicionarContato(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		
		contato.setNome(request.getParameter("nome").trim());
		contato.setFone(request.getParameter("fone").trim());
		contato.setEmail(request.getParameter("email").trim());
		
		dao.inserirContato(contato);
		response.sendRedirect("main");
		
	}
	
	protected void selecionarContato (HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		
		contato.setIdcon(request.getParameter("idcon"));
		dao.selecionarContato(contato);				
		
		request.setAttribute("idcon", contato.getIdcon());
		request.setAttribute("nome", contato.getNome());
		request.setAttribute("fone", contato.getFone());
		request.setAttribute("email", contato.getEmail());

		RequestDispatcher rd = request.getRequestDispatcher("editarcontato.jsp");
		rd.forward(request, response);
	}
	
	protected void editarContato(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		contato.setNome(request.getParameter("nome"));
		contato.setEmail(request.getParameter("email"));
		contato.setFone(request.getParameter("fone"));
		
		dao.editarContato(contato);
		response.sendRedirect("main");
	}
	
	protected void deletarContato(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException{
		contato.reset();
		contato.setIdcon(request.getParameter("idcon"));
		
		dao.deletarContato(request.getParameter("idcon"));
		response.sendRedirect("main");
	}
	
	protected void gerarRelatorio(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		
		Document documento = new Document();
		try {
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "inline; filename=contatos.pdf");

			PdfWriter.getInstance(documento, response.getOutputStream());
			documento.open();
			documento.add(new Paragraph("Lista de contatos:"));
			documento.add(new Paragraph(" "));

			PdfPTable tabela = new PdfPTable(3);
			
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome"));
			PdfPCell col2 = new PdfPCell(new Paragraph("Fone"));
			PdfPCell col3 = new PdfPCell(new Paragraph("Email"));
			
			tabela.addCell(col1);
			tabela.addCell(col2);
			tabela.addCell(col3);

			ArrayList<JavaBeans> lista = dao.listarContatos();
			for (int i = 0; i<lista.size();i++) {
				tabela.addCell(lista.get(i).getNome());
				tabela.addCell(lista.get(i).getFone());
				tabela.addCell(lista.get(i).getEmail());
			}
			documento.add(tabela);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			documento.close();
		}
	}
}

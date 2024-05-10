package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class DAO.
 */
public class DAO {
	
	/** The driver. */
	private String driver = "com.mysql.cj.jdbc.Driver";
	
	/** The url. */
	private String url = "jdbc:mysql://127.0.0.1:3306/dbagenda?useTimezone=true&serverTimezone=UTC";
	
	/** The user. */
	private String user = "root";
	
	/** The password. */
	private String password = "root";

	/**
	 * Conectar.
	 *
	 * @return the connection
	 */
	private Connection conectar() {
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			return con;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	/**
	 * Teste conexao.
	 */
	public void testeConexao() {
		try {
			Connection con = conectar();
			System.out.println(con);
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Inserir contato.
	 *
	 * @param contato the contato
	 */
	public void inserirContato(JavaBeans contato) {

		String create = "INSERT INTO contatos (nome,fone,email) VALUES (?,?,?)";
		try {
			Connection con = conectar();

			PreparedStatement pst = con.prepareStatement(create);
			pst.setString(1, contato.getNome());
			pst.setString(2, contato.getFone());
			pst.setString(3, contato.getEmail());

			pst.executeUpdate();
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Listar contatos.
	 *
	 * @return the array list
	 */
	public ArrayList<JavaBeans> listarContatos(){
		ArrayList<JavaBeans> contatos = new ArrayList<>();
		String read = "SELECT * FROM contatos ORDER BY nome";
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(read);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String idcon = rs.getString("idcon");
				String nome  = rs.getString("nome");
				String fone = rs.getString("fone");
				String email = rs.getString("email");

				contatos.add(new JavaBeans(idcon, nome, fone, email));
			}
			con.close();
			return contatos;
			
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	/**
	 * Selecionar contato.
	 *
	 * @param contato the contato
	 */
	public void selecionarContato(JavaBeans contato) {
		
		String select = "SELECT * FROM contatos WHERE idcon = ?";
		
		try {
			Connection con = conectar();
			
			PreparedStatement pst = con.prepareStatement(select);
			pst.setString(1, contato.getIdcon());
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				contato.setIdcon(rs.getString(1));
				contato.setNome(rs.getString(2));
				contato.setFone(rs.getString(3));
				contato.setEmail(rs.getString(4));
			}
			con.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}		
		
	}
	
	/**
	 * Editar contato.
	 *
	 * @param contato the contato
	 */
	public void editarContato(JavaBeans contato) {
		String update = "UPDATE contatos SET nome = ?, fone = ?, email = ? WHERE idcon = ?";
		try {
			Connection con = conectar();
			
			PreparedStatement pst = con.prepareStatement(update);
			
			pst.setString(1, contato.getNome());
			pst.setString(2, contato.getFone());
			pst.setString(3, contato.getEmail());
			pst.setString(4, contato.getIdcon());
			
			pst.executeUpdate();
			con.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Deletar contato.
	 *
	 * @param idcon the idcon
	 */
	public void deletarContato(String idcon) {
		String deletar = "DELETE FROM contatos WHERE idcon = ?";
		
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(deletar);
			pst.setString(1, idcon);
			pst.executeUpdate();
			con.close();
		}catch (Exception e) {
			System.out.println(e);
		}
	}
}

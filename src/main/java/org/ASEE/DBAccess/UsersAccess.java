package org.ASEE.DBAccess;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openapitools.model.User;

public class UsersAccess {

	 //SE DECLARA LA CONEXIÓN
    private static Connection conexion = null ; 
    /* ------------------------------------------------------------------ */
    /* --------------METODO PARA REALIZAR LA CONEXION-------------------- */
    /* ------------------------------------------------------------------ */
    public boolean dbConectar() {
        
        System.out.println("---dbConectar---");
        // Crear la conexion a la base de datos 
        String driver = "org.postgresql.Driver";
        String numdep = "localhost"; // Direccion IP
        String puerto = "5432";
        String database = "ASEE";
        String url = "jdbc:postgresql://" + numdep + ":" + puerto + "/" + database;
        String usuario = "postgres";
        String contrasena = "12345";

        try { 
             System.out.println("---Conectando a PostgreSQL---");
                Class.forName (driver); // Cargar el driver JDBC para PostgreSQL
             conexion = DriverManager.getConnection (url, usuario, contrasena); 
             System.out.println ("Conexion realizada a la base de datos " + conexion); 
             return true; 
         } catch (ClassNotFoundException e) { 
             // Error. No se ha encontrado el driver de la base de datos 
             e.printStackTrace(); 
             return false; 
         } catch (SQLException e) { 
             // Error. No se ha podido conectar a la BD 
             e.printStackTrace(); 
             return false; 
         } 
    }
    
    public boolean dbDesconectar() {
        System.out.println("---dbDesconectar---");

        try {
            //conexion.commit();// conexion.setAutoCommit(false); // en dbConectar()
            conexion.close();
            System.out.println("Desconexión realizada correctamente");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
    }
    
    public void dbConsultarUsers() {

        Statement st;
        
        System.out.println("---dbConsultarVideo---"); 
        
        try {
            st = conexion.createStatement();
            // Obtener todos los videos
            String result = "SELECT * FROM Users";
            
            ResultSet rset = st.executeQuery(result);
            
            System.out.println(result);
            System.out.println(" ");
            
            while (rset.next()) {
                System.out.println("ID: " + rset.getInt(1));
                System.out.println("Username: " + rset.getString(2));
                System.out.println("First Name: " + rset.getString(3));
                System.out.println("Last Name: " + rset.getString(4));
                System.out.println("Email: " + rset.getString(5));
                System.out.println("Password: " + rset.getString(6));
                System.out.println("Bio: " + rset.getString(7));
                System.out.println("Role: " + rset.getString(8));
                System.out.println("Country: " + rset.getString(9));
                System.out.println("Profile Picture: " + rset.getString(10));
                System.out.println("Birthdate: " + rset.getDate(11));
                System.out.println("Uploaded Videos: " + rset.getArray(12));
                System.out.println("Watched Videos: " + rset.getArray(13));
                System.out.println("Followers: " + rset.getArray(14));
                System.out.println("Following: " + rset.getArray(15));
                
                System.out.println("---------------------------------------");
            }
            rset.close();
        }catch (SQLException e) {
            e.printStackTrace();
        } 
    }
    
    public void dbAddUser(User user) {
    	PreparedStatement ps;
    	
    	System.out.println("---dbAddUser---");
    	
    	try {
    		String insertQuery = "INSERT INTO Users (username, email, password) VALUES (?,?,?)";
    		ps = conexion.prepareStatement(insertQuery);
    		
    		String username = user.getUsername();
    		String email = user.getEmail();
    		String password = user.getPassword();
    		
    		ps.setString(1, username);
    		ps.setString(2, email);
    		ps.setString(3, password);
    		
    		int rowsInserted = ps.executeUpdate();
    		// Comprobar si la inserción fue exitosa
    		
            if (rowsInserted > 0) {
                System.out.println("¡El Usuario fue insertado exitosamente!");
            }else {
                System.out.println("ERROR: No se ha insertado el usuario");
            }
            
            ps.close();
            
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
    }
    
    public List<Long> convertintoList(Array lista) throws SQLException{
    	
    	Long[] array = (Long[]) lista.getArray();
    	
    	return Arrays.asList(array);
    }
    
    public User dbUserbyUsername(String username) {
    	PreparedStatement ps;
    	User user = new User();
    	
    	System.out.println("---dbUserbyUsername---");
    	
    	try {
    		String selectQuery = "SELECT * FROM Users WHERE username = ?";
    		ps = conexion.prepareStatement(selectQuery);
    		ps.clearParameters();
    		
    		ps.setString(1, username);
    		
    		ResultSet rset = ps.executeQuery();
    		// Comprobar si la inserción fue exitosa
    		while (rset.next()) {
                user.setId(rset.getLong(1));
                user.setUsername(rset.getString(2));
                user.setFirstName(rset.getString(3));
                user.setLastName(rset.getString(4));
                user.setEmail(rset.getString(5));
                user.setPassword(rset.getString(6));
                user.setBio(rset.getString(7));
                user.setRole(rset.getString(8));
                user.setCountry(rset.getString(9));
                user.setProfilePicture(rset.getString(10));
                if(rset.getDate(11) != null)
                user.setBirthdate(rset.getDate(11).toLocalDate());
                if(rset.getArray(12) != null)
                user.setUploadedVideos(convertintoList(rset.getArray(12)));
                if(rset.getArray(13) != null)
                user.setWatchedVideos(convertintoList(rset.getArray(13)));
                if(rset.getArray(14) != null)
                user.setFollowers(convertintoList(rset.getArray(14)));
                if(rset.getArray(15) != null)
                user.setFollowing(convertintoList(rset.getArray(15)));
                
                
           }
           rset.close();
         
            
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
    	return user;
    }
    
    public boolean updateDbUser(User user) {
        PreparedStatement ps;
        boolean isUpdated = false;
        
        System.out.println("---updateDbUser---");
        
        try {
            String updateQuery = "UPDATE Users SET firstName = ?, lastName = ?, password = ?, bio = ?, country = ?, birthdate = ? WHERE username = ?";
            ps = conexion.prepareStatement(updateQuery);
            ps.clearParameters();

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getBio());
            ps.setString(5, user.getCountry());
            //ps.setString(7, user.getProfilePicture());
            ps.setDate(6, java.sql.Date.valueOf(user.getBirthdate()));

            ps.setString(7, user.getUsername());

            // Ejecuta la actualización
            int rowsAffected = ps.executeUpdate();
            isUpdated = rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return isUpdated;
    }

}

	

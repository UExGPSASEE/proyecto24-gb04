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
        String database = "ASEE_Users";
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
                System.out.println("Watched Videos: " + rset.getArray(12));
                System.out.println("Followers: " + rset.getArray(13));
                System.out.println("Following: " + rset.getArray(14));
                
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
            String insertQuery = "INSERT INTO Users (username, email, password, watchedvideos, followers, following) VALUES (?,?,?,?,?,?)";
            ps = conexion.prepareStatement(insertQuery);
            
            String username = user.getUsername();
            String email = user.getEmail();
            String password = user.getPassword();
            
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            
            // Inicializar los campos como arrays de enteros vacíos
            Array emptyArray = conexion.createArrayOf("integer", new Integer[0]);
            ps.setArray(4, emptyArray); // watchedvideos
            ps.setArray(5, emptyArray); // followers
            ps.setArray(6, emptyArray); // following
            
            int rowsInserted = ps.executeUpdate();
            
            if (rowsInserted > 0) {
                System.out.println("¡El Usuario fue insertado exitosamente!");
            } else {
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
    
    /*
    public List<Integer> convertintoListInt(Array lista) throws SQLException{
    	
    	Integer[] array = (Integer[]) lista.getArray();
    	
    	return Arrays.asList(array);
    }
    */
    
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
                user.setWatchedVideos(convertintoList(rset.getArray(12)));
                if(rset.getArray(13) != null)
                user.setFollowers(convertintoList(rset.getArray(13)));
                if(rset.getArray(14) != null)
                user.setFollowing(convertintoList(rset.getArray(14)));
                
                
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
    
    public List<User> dbGetAllUsers() {
        PreparedStatement ps;
        List<User> userList = new ArrayList<>();

        System.out.println("---dbGetAllUsers---");

        try {
            String selectQuery = "SELECT Username, firstname, lastname, email, password, bio, role, country, profilepicture, birthdate FROM Users";
            ps = conexion.prepareStatement(selectQuery);

            ResultSet rset = ps.executeQuery();

            // Recorre el ResultSet y crea un objeto User para cada fila
            while (rset.next()) {
                User user = new User();
                user.setUsername(rset.getString("Username"));
                user.setFirstName(rset.getString("firstname"));
                user.setLastName(rset.getString("lastname"));
                user.setEmail(rset.getString("email"));
                user.setPassword(rset.getString("password"));
                user.setBio(rset.getString("bio"));
                user.setRole(rset.getString("role"));
                user.setCountry(rset.getString("country"));
                user.setProfilePicture(rset.getString("profilepicture"));
                user.setBirthdate(rset.getDate("birthdate").toLocalDate());

                userList.add(user); // Agrega el usuario a la lista
            }
            rset.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList; // Devuelve la lista de usuarios
    }
    
    public List<Long> dbGetFollowers(String username){
        PreparedStatement ps;
    	List<Long> followers = new ArrayList<>();
    	
    	 System.out.println("---dbGetFollowers---");
    	 
         try {
             String selectQuery = "SELECT following FROM Users WHERE username = ?";
             ps = conexion.prepareStatement(selectQuery);
             ps.clearParameters();
             
             ps.setString(1, username);

             ResultSet rset = ps.executeQuery();

             // Recorre el ResultSet y crea un objeto User para cada fila
             while (rset.next()) {
                 followers = convertintoList(rset.getArray(1));
             }
             rset.close();

         } catch (SQLException e) {
             e.printStackTrace();
         }
    	
    	return followers;
    }
    
    //Username = usuario al que seguimos
    //id = usuario de la sesión
    public void dbFollow(String username, Long id) {
    	PreparedStatement ps;
    	
    	 System.out.println("---dbFollow---");
    	 
         try {
             String updateQuery = "UPDATE Users SET followers = array_append(followers, ?) WHERE username = ? AND NOT (? = ANY(followers))";
             ps = conexion.prepareStatement(updateQuery);
             ps.clearParameters();
             
             ps.setLong(1, id);
             ps.setString(2, username);
             ps.setLong(3, id);

             Integer updateRows = ps.executeUpdate();
            
             if(updateRows > 0)
            	 System.out.println("Consulta ejecutada correctamente");
             else
            	 System.out.println("Consulta fallida");

         } catch (SQLException e) {
             e.printStackTrace();
         }
         
         //dbFollowing(username, id);
    	
    }
    
    //Username = usuario de la sesión
    //id = usuario al que seguimos
    public void dbFollowing(String username, Long id) {
    	PreparedStatement ps;
    	
    	 System.out.println("---dbFollowing---");
    	 
         try {
             String updateQuery = "UPDATE Users SET following = array_append(following, ?) WHERE username = ? AND NOT (? = ANY(following))";
             ps = conexion.prepareStatement(updateQuery);
             ps.clearParameters();
             
             ps.setLong(1, id);
             ps.setString(2, username);
             ps.setLong(3, id);

             Integer updateRows = ps.executeUpdate();
            
             if(updateRows > 0)
            	 System.out.println("Consulta ejecutada correctamente");
             else
            	 System.out.println("Consulta fallida");

         } catch (SQLException e) {
             e.printStackTrace();
         }
         
         
    	
    }
    
 // Username = usuario al que dejamos de seguir
 // id = usuario de la sesión
 public void dbUnfollow(String username, Long id) {
     PreparedStatement ps;

     System.out.println("---dbUnfollow---");

     try {
         String updateQuery = "UPDATE Users SET followers = array_remove(followers, ?) WHERE username = ?";
         ps = conexion.prepareStatement(updateQuery);
         ps.clearParameters();

         ps.setLong(1, id);
         ps.setString(2, username);

         Integer updateRows = ps.executeUpdate();

         if (updateRows > 0)
             System.out.println("Consulta ejecutada correctamente");
         else
             System.out.println("Consulta fallida");

     } catch (SQLException e) {
         e.printStackTrace();
     }
 }

 // Username = usuario de la sesión
 // id = usuario al que dejamos de seguir
 public void dbRemoveFollowing(String username, Long id) {
     PreparedStatement ps;

     System.out.println("---dbRemoveFollowing---");

     try {
         String updateQuery = "UPDATE Users SET following = array_remove(following, ?) WHERE username = ?";
         ps = conexion.prepareStatement(updateQuery);
         ps.clearParameters();

         ps.setLong(1, id);
         ps.setString(2, username);

         Integer updateRows = ps.executeUpdate();

         if (updateRows > 0)
             System.out.println("Consulta ejecutada correctamente");
         else
             System.out.println("Consulta fallida");

     } catch (SQLException e) {
         e.printStackTrace();
     }
 }
 
	//Username = usuario a eliminar
	public void dbRemoveUser(String username) {
	  PreparedStatement ps;
	
	  System.out.println("---dbRemoveUser---");
	
	  try {
	      String deleteQuery = "DELETE FROM Users WHERE username = ?";
	      ps = conexion.prepareStatement(deleteQuery);
	      ps.clearParameters();
	
	      ps.setString(1, username);
	
	      Integer updateRows = ps.executeUpdate();
	
	      if (updateRows > 0)
	          System.out.println("Usuario eliminado correctamente");
	      else
	          System.out.println("No se encontró el usuario para eliminar");
	
	  } catch (SQLException e) {
	      e.printStackTrace();
	  }
	}

}

	

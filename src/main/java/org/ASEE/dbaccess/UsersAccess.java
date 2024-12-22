package org.ASEE.dbaccess;

import java.sql.Array;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.openapitools.model.User;

public class UsersAccess {

	private static final Logger LOGGER = Logger.getLogger(UsersAccess.class.getName());
	 //SE DECLARA LA CONEXIÓN
    private static Connection conexion = null ; 
    /* ------------------------------------------------------------------ */
    /* --------------METODO PARA REALIZAR LA CONEXION-------------------- */
    /* ------------------------------------------------------------------ */
    public boolean dbConectar() {
        
    	   LOGGER.info("---dbConectar---");
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
            String insertQuery = "INSERT INTO Users (username, email, password, uploadedvideos, watchedvideos, followers, following, liked_video_ids) VALUES (?,?,?,?,?,?,?,?)";
            ps = conexion.prepareStatement(insertQuery);
            
            String username = user.getUsername();
            String email = user.getEmail();
            String password = user.getPassword();
            
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            
            // Inicializar los campos como arrays de enteros vacíos
            Array emptyArray = conexion.createArrayOf("integer", new Integer[0]);
            ps.setArray(4, emptyArray); // uploadedvideos
            ps.setArray(5, emptyArray); // watchedvideos
            ps.setArray(6, emptyArray); // followers
            ps.setArray(7, emptyArray); // following
            ps.setArray(8, emptyArray); // liked_video_ids
            
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
                if(rset.getDate(11) != null) {
                user.setBirthdate(rset.getDate(11).toLocalDate());
                }
                if(rset.getArray(12) != null) {
                user.setUploadedVideos(convertintoList(rset.getArray(12)));
                }
                if(rset.getArray(13) != null) {
                user.setWatchedVideos(convertintoList(rset.getArray(13)));
                }
                if(rset.getArray(14) != null) {
                user.setFollowers(convertintoList(rset.getArray(14)));
                }
                if(rset.getArray(15) != null) {
                user.setFollowing(convertintoList(rset.getArray(15)));
                }
                
           }
           rset.close();
         
            
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
    	return user;
    }
    
    public User dbUserbyId(Long iduser) {
    	PreparedStatement ps;
    	User user = new User();
    	
    	System.out.println("---dbUserbyUsername---");
    	
    	try {
    		String selectQuery = "SELECT * FROM Users WHERE id = ?";
    		ps = conexion.prepareStatement(selectQuery);
    		ps.clearParameters();
    		
    		ps.setLong(1, iduser);
    		
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
                if(rset.getArray(16) != null)
                user.setLikedVideos(convertintoList(rset.getArray(16)));;
                
                
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

                // Maneja el campo `birthdate`, asignando null si es NULL en la base de datos
                Date birthdate = rset.getDate("birthdate");
                if (birthdate != null) {
                    user.setBirthdate(birthdate.toLocalDate());
                } else {
                    user.setBirthdate(null); // O cualquier valor predeterminado
                }

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
             String selectQuery = "SELECT followers FROM Users WHERE username = ?";
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
    	 System.out.println("---Username: " + username + "---");
    	 System.out.println("---ID: " + id + "---");
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
    	
    }
    
    //Username = usuario de la sesión
    //id = usuario al que seguimos
    public void dbFollowing(Long idUser, Long idUserToFollow) {
    	PreparedStatement ps;
    	
    	 System.out.println("---dbFollowing---");
    	 
         try {
             String updateQuery = "UPDATE Users SET following = array_append(following, ?) WHERE id = ? AND NOT (? = ANY(following))";
             ps = conexion.prepareStatement(updateQuery);
             ps.clearParameters();
             
             ps.setLong(1, idUserToFollow);
             ps.setLong(2, idUser);
             ps.setLong(3, idUserToFollow);

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
 public void dbRemoveFollowing(Long idUser, Long idUserToUnfollow) {
     PreparedStatement ps;

     System.out.println("---dbRemoveFollowing---");

     try {
         String updateQuery = "UPDATE Users SET following = array_remove(following, ?) WHERE id = ?";
         ps = conexion.prepareStatement(updateQuery);
         ps.clearParameters();

         ps.setLong(1, idUserToUnfollow);
         ps.setLong(2, idUser);

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
	
	//------------------------------------------------------------------------------------------------
	
	// idUser = usuario de la sesión
	// idVideo = id del video al que se le quita el like
	public void dbRemoveLikedVideo(Long idUser, Integer idVideo) {
	    PreparedStatement ps;

	    System.out.println("---dbRemoveLikedVideo---");

	    try {
	        String updateQuery = "UPDATE Users SET liked_video_ids = array_remove(liked_video_ids, ?) WHERE id = ?";
	        ps = conexion.prepareStatement(updateQuery);
	        ps.clearParameters();

	        ps.setInt(1, idVideo);
	        ps.setLong(2, idUser);

	        Integer updateRows = ps.executeUpdate();

	        if (updateRows > 0)
	            System.out.println("Consulta ejecutada correctamente");
	        else
	            System.out.println("Consulta fallida");

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// idUser = usuario de la sesión
	// idVideo = id del video al que se le da like
	public void dbAddLikedVideo(Long idUser, Long idVideo) {
	    System.out.println("---dbAddLikedVideo---");
	    System.out.println("idUser: " + idUser + ", idVideo: " + idVideo);

	    String updateQuery = "UPDATE users SET liked_video_ids = array_append(liked_video_ids, ?) WHERE id = ?";

	    try (PreparedStatement ps = conexion.prepareStatement(updateQuery)) {
	        ps.setLong(1, idVideo);
	        ps.setLong(2, idUser);

	        int updateRows = ps.executeUpdate();

	        if (updateRows > 0) {
	            System.out.println("Consulta ejecutada correctamente");
	        } else {
	            System.out.println("Consulta fallida: No se afectaron filas. Verifica que idUser existe en la base de datos y liked_video_ids es del tipo adecuado.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

    public boolean dbAddVideoWatched(Long videoId, Long userId) {
    	PreparedStatement ps;
        boolean isUpdated = false;

        System.out.println("---bdAddVideoWatched---");

        try {
            // Verifica si el video ya está en la lista watchedVideos
            String checkQuery = "SELECT ? = ANY(watchedVideos) FROM Users WHERE id = ?";
            ps = conexion.prepareStatement(checkQuery);
            ps.setLong(1, videoId);
            ps.setLong(2, userId);

            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getBoolean(1)) {
                // El video ya está en la lista, devuelve true
                return true;
            }

            // Si no está en la lista, añade el video
            String updateQuery = "UPDATE Users SET watchedVideos = array_append(watchedVideos, ?) WHERE id = ?";
            ps = conexion.prepareStatement(updateQuery);
            ps.setLong(1, videoId);
            ps.setLong(2, userId);

            // Ejecuta la actualización
            int rowsAffected = ps.executeUpdate();
            isUpdated = rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    }
    
    public boolean dbAddLike(Long videoId, Long userId) {
    	PreparedStatement ps;
        boolean isUpdated = false;

        System.out.println("---dbAddLike---");

        try {

            // Si no está en la lista, añade el video
            String updateQuery = "UPDATE Users SET liked_video_ids = array_append(liked_video_ids, ?) WHERE id = ?";
            ps = conexion.prepareStatement(updateQuery);
            ps.setLong(1, videoId);
            ps.setLong(2, userId);

            // Ejecuta la actualización
            int rowsAffected = ps.executeUpdate();
            isUpdated = rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    }
    
    public boolean dbAddUnlike(Long videoId, Long userId) {
    	PreparedStatement ps;
        boolean isUpdated = false;

        System.out.println("---dbAddLike---");

        try {

            // Si no está en la lista, añade el video
            String updateQuery = "UPDATE Users SET liked_video_ids = array_remove(liked_video_ids, ?) WHERE id = ?";
            ps = conexion.prepareStatement(updateQuery);
            ps.setLong(1, videoId);
            ps.setLong(2, userId);

            // Ejecuta la actualización
            int rowsAffected = ps.executeUpdate();
            isUpdated = rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    // ---------------------------- NUEVOS METODOS RECOMENDACIONES ---------------------------//
    public List<Long> dbCheckWatchedVideosbyUsername(String username) {
        List<Long> videoIds = new ArrayList<>();
        System.out.println("---dbConsultarHistorialIdsPorUsuario---");

        String query = "SELECT watchedvideos FROM Users WHERE username = ?";

        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            pst.setString(1, username);

            try (ResultSet rset = pst.executeQuery()) {
                if (rset.next()) {
                    Array sqlArray = rset.getArray(1); // Obtiene el array de watchedvideos
                    if (sqlArray != null) {
                        Long[] ids = (Long[]) sqlArray.getArray(); // Convierte el array SQL a Java
                        videoIds = Arrays.asList(ids); // Lo transforma en una lista
                    } else {
                        System.out.println("No se encontraron videos vistos para el usuario: " + username);
                    }
                } else {
                    System.out.println("Usuario no encontrado: " + username);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return videoIds;
    }
    
    public List<Long> dbCheckFollowedUsersByUsername(String username) {
        List<Long> followedUserIds = new ArrayList<>();
        System.out.println("---dbConsultarUsuariosSeguidosPorUsuario---");

        String query = "SELECT following FROM Users WHERE username = ?";

        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            pst.setString(1, username);

            try (ResultSet rset = pst.executeQuery()) {
                if (rset.next()) {
                    Array sqlArray = rset.getArray(1); // Obtiene el array de following
                    if (sqlArray != null) {
                        Long[] ids = (Long[]) sqlArray.getArray(); // Convierte el array SQL a Java
                        followedUserIds = Arrays.asList(ids); // Lo transforma en una lista
                    } else {
                        System.out.println("No se encontraron usuarios seguidos para el usuario: " + username);
                    }
                } else {
                    System.out.println("Usuario no encontrado: " + username);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return followedUserIds;
    }
    
    public User dbUserById(Long id) {
        PreparedStatement ps;
        User user = new User();
        
        System.out.println("---dbUserById---");
        
        try {
            String selectQuery = "SELECT * FROM Users WHERE id = ?";
            ps = conexion.prepareStatement(selectQuery);
            ps.clearParameters();
            
            ps.setLong(1, id);  // Usar el ID en lugar del nombre de usuario
            
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

    public void dbDeleteHistory(Long videoId) {
        PreparedStatement psWatched = null;
        PreparedStatement psLiked = null;

        System.out.println("---dbDeleteHistory---");

        String queryWatched = "UPDATE users SET watchedvideos = array_remove(watchedvideos, ?)";
        String queryLiked = "UPDATE users SET liked_video_ids = array_remove(liked_video_ids, ?)";

        try {
            // Actualizar la lista 'watchedvideos'
            psWatched = conexion.prepareStatement(queryWatched);
            psWatched.setLong(1, videoId);
            int rowsUpdatedWatched = psWatched.executeUpdate();
            System.out.println("Filas actualizadas en watchedvideos: " + rowsUpdatedWatched);

            // Actualizar la lista 'liked_video_ids'
            psLiked = conexion.prepareStatement(queryLiked);
            psLiked.setLong(1, videoId);
            int rowsUpdatedLiked = psLiked.executeUpdate();
            System.out.println("Filas actualizadas en liked_video_ids: " + rowsUpdatedLiked);

        } catch (SQLException e) {
            System.err.println("Error al ejecutar las consultas: " + e.getMessage());
        } finally {
            // Cerrar los PreparedStatements para evitar fugas de recursos
            if (psWatched != null) {
                try {
                    psWatched.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar psWatched: " + e.getMessage());
                }
            }
            if (psLiked != null) {
                try {
                    psLiked.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar psLiked: " + e.getMessage());
                }
            }
        }
    }

    public List<User> dbGetFollowingUsers(String username) {
        PreparedStatement ps;
        List<User> userList = new ArrayList<>();

        System.out.println("---dbGetFollowingUsers---");

        try {
            String selectQuery = "SELECT * FROM users WHERE id = ANY (SELECT UNNEST(following) FROM users WHERE username = ?)";
            ps = conexion.prepareStatement(selectQuery);
            
            ps.setString(1, username);

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

                // Maneja el campo `birthdate`, asignando null si es NULL en la base de datos
                Date birthdate = rset.getDate("birthdate");
                if (birthdate != null) {
                    user.setBirthdate(birthdate.toLocalDate());
                } else {
                    user.setBirthdate(null); // O cualquier valor predeterminado
                }

                userList.add(user); // Agrega el usuario a la lista
            }
            rset.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList; // Devuelve la lista de usuarios
    }
    
    public List<User> dbGetNotFollowingUsers(String username) {
        PreparedStatement ps;
        List<User> userList = new ArrayList<>();

        System.out.println("---dbGetNotFollowingUsers---");

        try {
            String selectQuery = "SELECT * FROM users  WHERE id NOT IN (SELECT UNNEST(following) FROM users WHERE username = ?) AND username != ?";
            ps = conexion.prepareStatement(selectQuery);
            
            ps.setString(1, username);
            ps.setString(2, username);

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

                // Maneja el campo `birthdate`, asignando null si es NULL en la base de datos
                Date birthdate = rset.getDate("birthdate");
                if (birthdate != null) {
                    user.setBirthdate(birthdate.toLocalDate());
                } else {
                    user.setBirthdate(null); // O cualquier valor predeterminado
                }

                userList.add(user); // Agrega el usuario a la lista
            }
            rset.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList; // Devuelve la lista de usuarios
    }
    
    public void dbDeleteFollowersAndFollowing(Long id) {
        PreparedStatement psFollowers;
        PreparedStatement psFollowing;

        System.out.println("---dbUnfollow (eliminar de followers y following)---");

        try {
            // Consulta para eliminar el ID del array de 'followers' en todos los usuarios
            String updateFollowersQuery = "UPDATE Users SET followers = array_remove(followers, ?)";
            psFollowers = conexion.prepareStatement(updateFollowersQuery);
            psFollowers.clearParameters();
            psFollowers.setLong(1, id);
            int updateFollowersRows = psFollowers.executeUpdate();

            if (updateFollowersRows > 0) {
                System.out.println("ID eliminado de 'followers' correctamente en " + updateFollowersRows + " filas.");
            } else {
                System.out.println("No se encontraron 'followers' para eliminar.");
            }

            // Consulta para eliminar el ID del array de 'following' en todos los usuarios
            String updateFollowingQuery = "UPDATE Users SET following = array_remove(following, ?)";
            psFollowing = conexion.prepareStatement(updateFollowingQuery);
            psFollowing.clearParameters();
            psFollowing.setLong(1, id);
            int updateFollowingRows = psFollowing.executeUpdate();

            if (updateFollowingRows > 0) {
                System.out.println("ID eliminado de 'following' correctamente en " + updateFollowingRows + " filas.");
            } else {
                System.out.println("No se encontraron 'following' para eliminar.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al ejecutar las consultas de eliminación.");
        }
    }

}

	

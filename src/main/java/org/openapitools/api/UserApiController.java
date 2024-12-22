package org.openapitools.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.ASEE.dbaccess.UsersAccess;
import org.openapitools.model.LikeVideoRequest;
import org.openapitools.model.LoginRequest;
import org.openapitools.model.User;
import org.openapitools.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.RedirectView;

import videoService.VideoService;

@Controller
@RequestMapping("${openapi.tubeFlixGestionDeUsuariosOpenAPI30.base-path:}")
public class UserApiController implements UserApi {
	
	@Autowired
	private VideoService videoservice;

    private final NativeWebRequest request;

    public UserApiController(NativeWebRequest request) {
        this.request = request;
    }

    // Endpoint de prueba
    @GetMapping("/health")
    public String healthCheck() {
        return "health";
    }
    
    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<User> createUser(@RequestBody User user) {
        
    	System.out.println("------------ UserApiController -> createUser() ------------");

        System.out.println("Datos del usuario recibido:");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Password: " + user.getPassword());

    	UsersAccess cliente = new UsersAccess();
    	System.out.println("Iniciando programa");
    	if(!cliente.dbConectar()) {
    		System.out.println("Conexion fallida");
    	}
    	
        //Guardar user
        cliente.dbAddUser(user);
        User userregistered = cliente.dbUserbyUsername(user.getUsername());
        
    	
    	if(!cliente.dbDesconectar()) {
    		System.out.println("Desconexión fallida");
    	} 	

        // Retorna el usuario recibido como respuesta JSON
        return new ResponseEntity<>(userregistered, HttpStatus.CREATED);
    }
    
    @Override
    public ResponseEntity<User> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        
    	System.out.println("------------ UserApiController -> loginUser() ------------");
    	
    	// Se guardan los parametros en variables
    	String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        
        // Se accede a la BD 
        UsersAccess cliente = new UsersAccess();
        if(!cliente.dbConectar()) {
            System.out.println("Conexion fallida");
        }

        // Buscar usuario
        User user = cliente.dbUserbyUsername(username);

        if(!cliente.dbDesconectar()) {
            System.out.println("Desconexión fallida");
        }

        System.out.println("Usuario obtenido: " + user.getUsername() + " Contraseña:" + user.getPassword());
        
        
        // Validar usuario y contraseña
        if (user != null && user.getUsername().equals(username) && user.getPassword().equals(password)) {
            System.out.println("Inicio de sesión correcto");
            return ResponseEntity.ok(user); // Devolver el objeto User;
        } else {
            // Si las credenciales no son válidas, devuelve un error
            System.out.println("Inicio de sesión fallido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Error de autenticación
        }
    }
    
    @GetMapping("/user/profile")
    public String userProfile(HttpSession session, Model model) {

        // Obtiene el objeto user desde la sesión
        User user = (User) session.getAttribute("user");
        if (user != null) {
            System.out.println("Username del modelo: " + user.getUsername());
            model.addAttribute("user", user); // Añadir al modelo para la vista
            
         // Aquí determinamos si el perfil es del usuario actual
            boolean esPropioPerfil = true; 
            model.addAttribute("propio", esPropioPerfil); // Agrega la variable 'propio' al modelo
            model.addAttribute("siguiendo", false); // Agrega la variable 'propio' al modelo
            
        } else {
            System.out.println("El usuario no está en sesión.");
        }
        

        return "profile";  // Se refiere a un archivo HTML llamado `profile.html` en `src/main/resources/templates`
    }
    
    @Override
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody @Valid User user) {
        System.out.println("------------ UserApiController -> updateUser() ------------");
        
        UsersAccess cliente = new UsersAccess();
        System.out.println("Iniciando programa de actualización de usuario");

        // Conectar a la base de datos
        if (!cliente.dbConectar()) {
            System.out.println("Conexion fallida");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Respuesta 500 si falla la conexión
        }

        try {
            // Actualizar el usuario en la base de datos
            cliente.updateDbUser(user);
            System.out.println("Usuario actualizado: " + user.getUsername());

            return ResponseEntity.ok(user); // Responde con el usuario actualizado y status 200 OK
        } catch (Exception e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Maneja excepciones y devuelve 500
        } finally {
            // Desconectar de la base de datos
            if (!cliente.dbDesconectar()) {
                System.out.println("Desconexión fallida");
            }
        }
    }

    
    @Override
    public ResponseEntity<List<User>> getUserList() {
        System.out.println("------------ UserApiController -> getUserList() ------------");

        UsersAccess cliente = new UsersAccess();

        // Conexión a la base de datos
        if (!cliente.dbConectar()) {
            System.out.println("Conexión fallida");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Obtener lista de usuarios
        List<User> users = cliente.dbGetAllUsers();

        // Desconexión de la base de datos
        if (!cliente.dbDesconectar()) {
            System.out.println("Desconexión fallida");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Si la lista de usuarios está vacía, devuelve un estado HTTP 204 (sin contenido)
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Devuelve la lista de usuarios con estado 200
        return ResponseEntity.ok(users);
    }
    
    
    public boolean isFollowing(List<Long> followers, Long id) {
        boolean isFollowing = false;

        for (Long followerId : followers) {
            if (followerId.equals(id)) {
                isFollowing = true;
                break;
            }
        }

        return isFollowing;
    }
    
    @Override
    public ResponseEntity<User> getUserByName(@PathVariable String username) {
        System.out.println("------------ UserApiController -> getUserByName() ------------");
        UsersAccess cliente = new UsersAccess();
        User user = null;

        // Conectar a la base de datos
        if (!cliente.dbConectar()) {
            System.out.println("Conexión fallida");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Devuelve un error 500 si falla la conexión
        }

        try {
            // Obtener usuario por nombre
            user = cliente.dbUserbyUsername(username);
            
            if (user == null) {
                System.out.println("Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Devuelve un error 404 si el usuario no existe
            }

        } finally {
            // Asegurarse de desconectar la base de datos
            if (!cliente.dbDesconectar()) {
                System.out.println("Desconexión fallida");
            }
        }

        // Devuelve el usuario en la respuesta
        return ResponseEntity.ok(user);
    }
    
    @Override
    public ResponseEntity<Boolean> isFollowing(Long userId,String profileUsername) {
        System.out.println("------------ UserApiController -> isFollowing() ------------");
        UsersAccess cliente = new UsersAccess();

        if (!cliente.dbConectar()) {
            System.out.println("Conexión fallida");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false); // Error 500 si falla la conexión
        }

        try {
            // Obtener lista de seguidores del perfil
            List<Long> followers = cliente.dbGetFollowers(profileUsername);
            
            // Verificar si el ID del usuario de la sesión está en la lista de seguidores
            boolean isFollowing = followers.contains(userId);
            
            return ResponseEntity.ok(isFollowing);
            
        } finally {
            if (!cliente.dbDesconectar()) {
                System.out.println("Desconexión fallida");
            }
        }
    }
    
    @Override
    public ResponseEntity<Void> followUser(@RequestBody Map<String, Object> request) {
        System.out.println("------------ UserApiController -> followUser() ------------");

        Long userId = ((Number) request.get("userId")).longValue();
        String usernameToFollow = ((String) request.get("usernameToFollow")).replace("\"", "");

        UsersAccess cliente = new UsersAccess();

        if (!cliente.dbConectar()) {
            System.out.println("Fallo en la conexión a la base de datos.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        try {
            // Añadir el usuario a la lista de seguidores del perfil a seguir
            cliente.dbFollow(usernameToFollow, userId);
            
            // Obtener el usuario al que queremos seguir y añadir su ID a la lista de seguidos de userId
            System.out.println("BUSCAMOS LOS DATOS DEL USUARIO: " + usernameToFollow);
            System.out.println("ID DEL USUARIO EN SESION: " + userId);
            
            User userToFollow = cliente.dbUserbyUsername(usernameToFollow);
            
            System.out.println("USUARIO AL QUE SEGUIR: " + userToFollow.getUsername());
            System.out.println("ID USUARIO AL QUE SEGUIR: " + userToFollow.getId());
            
            cliente.dbFollowing(userId, userToFollow.getId());

            return ResponseEntity.ok().build(); // Éxito

        } catch (Exception e) {
            System.err.println("Error al seguir al usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            if (!cliente.dbDesconectar()) {
                System.out.println("Error al desconectar de la base de datos.");
            }
        }
    }

    @Override
    public ResponseEntity<Void> unfollowUser(@RequestBody Map<String, Object> request) {
        System.out.println("------------ UserApiController -> unfollowUser() ------------");

        Long userId = ((Number) request.get("userId")).longValue();
        String usernameToUnfollow = ((String) request.get("usernameToFollow")).replace("\"", "");

        UsersAccess cliente = new UsersAccess();

        if (!cliente.dbConectar()) {
            System.out.println("Fallo en la conexión a la base de datos.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        try {
            // Añadir el usuario a la lista de seguidores del perfil a seguir
            cliente.dbUnfollow(usernameToUnfollow, userId);
            
            // Obtener el usuario al que queremos seguir y añadir su ID a la lista de seguidos de userId
            System.out.println("BUSCAMOS LOS DATOS DEL USUARIO: " + usernameToUnfollow);
            System.out.println("ID DEL USUARIO EN SESION: " + userId);
            
            User userToUnfollow = cliente.dbUserbyUsername(usernameToUnfollow);
            
            System.out.println("USUARIO AL QUE SEGUIR: " + userToUnfollow.getUsername());
            System.out.println("ID USUARIO AL QUE SEGUIR: " + userToUnfollow.getId());
            
            cliente.dbRemoveFollowing(userId, userToUnfollow.getId());

            return ResponseEntity.ok().build(); // Éxito

        } catch (Exception e) {
            System.err.println("Error al seguir al usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } finally {
            if (!cliente.dbDesconectar()) {
                System.out.println("Error al desconectar de la base de datos.");
            }
        }
    }

    

    @Override
    public RedirectView logoutUser(HttpSession session) {
    	
    	System.out.println("------logoutUser-------");
    	session.removeAttribute("user");
    	
    	return new RedirectView("/login.html");

    }
    
    
    @Override
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
    	
    	System.out.println("--------- UserApiController -> deleteUser() ---------");
    	
    	UsersAccess cliente = new UsersAccess();


    	if(!cliente.dbConectar()) {
            System.out.println("Conexion fallida");
        }
    	
    	

    	User user = cliente.dbUserbyUsername(username);
    	System.out.println("Service: Borrando videos del usuario");
    	videoservice.deleteVideos(user.getId());
    	System.out.println("Service: Borrando comentarios del usuario");
    	videoservice.deleteComments(user.getId());
    	System.out.println("Cliente: Borrando followers del usuario");
    	cliente.dbDeleteFollowersAndFollowing(user.getId());
        // borrar usuario
    	cliente.dbRemoveUser(username);
   	 

        if(!cliente.dbDesconectar()) {
            System.out.println("Desconexión fallida");
           
        }
       
    	
    	return new ResponseEntity<>(HttpStatus.OK);

        }
    
    //-------------------------------------------------------------
    @PostMapping("/user/likeVideo")
    public ResponseEntity<Video> likeVideo(@RequestBody LikeVideoRequest request) {
    	
    	UsersAccess cliente = new UsersAccess();
    	
    	if(!cliente.dbConectar()) {
            System.out.println("Conexion fallida");
        }
    	
    	System.out.println(request.getUsername());
    	System.out.println(request.getVideotitle());
    	System.out.println(request.getIdUserSession());
    	
    	User user = cliente.dbUserbyUsername(request.getUsername());
    	
    	System.out.println("Service: Servicio a video para dar like al video");
    	Video videoliked = videoservice.likeVideo(request.getVideotitle(), user.getId());
    	
    	System.out.println("video liked: "+ videoliked.getTitle() + " - " + videoliked.getDescription());
    	
    	cliente.dbAddLikedVideo(request.getIdUserSession(), videoliked.getId());
   	 

        if(!cliente.dbDesconectar()) {
            System.out.println("Desconexión fallida");
           
        }
    	
		return null;
    	
    }
    
    @Override
    public ResponseEntity<User> getUserById(@PathVariable Long userid) {
        System.out.println("------------ UserApiController -> getUserById() ------------");
        UsersAccess cliente = new UsersAccess();
        User user = null;

        // Conectar a la base de datos
        if (!cliente.dbConectar()) {
            System.out.println("Conexión fallida");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Devuelve un error 500 si falla la conexión
        }

        try {
            // Obtener usuario por nombre
            user = cliente.dbUserbyId(userid);
            
            if (user == null) {
                System.out.println("Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Devuelve un error 404 si el usuario no existe
            }

        } finally {
            // Asegurarse de desconectar la base de datos
            if (!cliente.dbDesconectar()) {
                System.out.println("Desconexión fallida");
            }
        }

        // Devuelve el usuario en la respuesta
        return ResponseEntity.ok(user);
    }
    
    @Override
    public ResponseEntity<Void> addView(@RequestBody Map<String, Long> payload) {
        Long videoId = payload.get("videoId");
        Long userId = payload.get("userId");
        UsersAccess cliente = new UsersAccess();
        
        System.out.println("Registrando vista para el video con ID: " + videoId + " y usuario con ID: " + userId);
        if(!cliente.dbConectar()) {
            System.out.println("Conexion fallida");
        }
    
    	
    	boolean exito = cliente.dbAddVideoWatched(videoId, userId);
   	 

        if(!cliente.dbDesconectar()) {
            System.out.println("Desconexión fallida");
           
        }
        
        if (exito) {
            return ResponseEntity.ok().build();
        } else {
            System.out.println("Fallo al registrar la vista en la base de datos");
            return ResponseEntity.status(400).build();
        }
    }
    
    @Override
    public ResponseEntity<Void> addLike(@RequestBody Map<String, Long> payload) {
        Long videoId = payload.get("videoId");
        Long userId = payload.get("userId");
        UsersAccess cliente = new UsersAccess();
        
        System.out.println("Registrando vista para el video con ID: " + videoId + " y usuario con ID: " + userId);
        if(!cliente.dbConectar()) {
            System.out.println("Conexion fallida");
        }
    
    	
    	boolean exito = cliente.dbAddLike(videoId, userId);
   	 

        if(!cliente.dbDesconectar()) {
            System.out.println("Desconexión fallida");
           
        }
        
        if (exito) {
            return ResponseEntity.ok().build();
        } else {
            System.out.println("Fallo al registrar la vista en la base de datos");
            return ResponseEntity.status(400).build();
        }
    }
    
    @Override
    public ResponseEntity<Void> addUnlike(@RequestBody Map<String, Long> payload) {
        Long videoId = payload.get("videoId");
        Long userId = payload.get("userId");
        UsersAccess cliente = new UsersAccess();
        
        System.out.println("Registrando vista para el video con ID: " + videoId + " y usuario con ID: " + userId);
        if(!cliente.dbConectar()) {
            System.out.println("Conexion fallida");
        }
    
    	
    	boolean exito = cliente.dbAddUnlike(videoId, userId);
   	 

        if(!cliente.dbDesconectar()) {
            System.out.println("Desconexión fallida");
           
        }
        
        if (exito) {
            return ResponseEntity.ok().build();
        } else {
            System.out.println("Fallo al registrar la vista en la base de datos");
            return ResponseEntity.status(400).build();
        }
    }
    
    // ---------------------------- NUEVOS METODOS RECOMENDACIONES ---------------------------//
    
    @Override
    public ResponseEntity<List<Video>> getUserVideoHistory(@PathVariable String username) {
        System.out.println("---getUserVideoHistory API---");

        try {
            // Validar que el nombre de usuario no sea nulo o vacío
            if (username == null || username.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el nombre de usuario es inválido
            }

            // Instancia para acceder a la base de datos
            UsersAccess cliente = new UsersAccess();

            // Conectar a la base de datos
            if (!cliente.dbConectar()) {
                System.out.println("Conexión fallida");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 si no se pudo conectar
            }

            // Consultar el historial de IDs de videos por usuario
            List<Long> videoIds = cliente.dbCheckWatchedVideosbyUsername(username);
            if (videoIds.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 si no se encuentran videos
            }

            // Lista para almacenar los videos obtenidos
            List<Video> videoList = new ArrayList<>();

            // Obtener cada video por su ID usando el método getVideoById
            for (Long videoId : videoIds) {
            	System.out.println("Service: Servicio para obtener el video con su id");
                Video video = videoservice.getVideoById(videoId);
                if (video != null) {
                    videoList.add(video); // Agregar video a la lista si fue encontrado
                } else {
                    System.out.println("No se pudo obtener el video con ID: " + videoId);
                }
            }

            // Desconectar la base de datos
            if (!cliente.dbDesconectar()) {
                System.out.println("Desconexión fallida");
            }

            // Si la lista de videos está vacía, devolver 404
            if (videoList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 si no se encuentran videos
            }

            System.out.println("Devolviendo historial de videos");
            return new ResponseEntity<>(videoList, HttpStatus.OK); // 200 con la lista de videos

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 en caso de error
        }
    }

    @Override
    public ResponseEntity<List<Long>> getUserFollowing(@PathVariable String username) {
        System.out.println("---getUserFollowing API---");

        try {
            // Validar que el nombre de usuario no sea nulo o vacío
            if (username == null || username.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el nombre de usuario es inválido
            }

            // Instancia para acceder a la base de datos
            UsersAccess cliente = new UsersAccess();

            // Conectar a la base de datos
            if (!cliente.dbConectar()) {
                System.out.println("Conexión fallida");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 si no se pudo conectar
            }

            // Consultar los IDs de usuarios seguidos por el usuario
            List<Long> followedUserIds = cliente.dbCheckFollowedUsersByUsername(username);
            if (followedUserIds.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 si no se encuentran usuarios seguidos
            }

            // Desconectar la base de datos
            if (!cliente.dbDesconectar()) {
                System.out.println("Desconexión fallida");
            }

            System.out.println("Devolviendo lista de usuarios seguidos");
            return new ResponseEntity<>(followedUserIds, HttpStatus.OK); // 200 con la lista de usuarios seguidos

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 en caso de error
        }
    }
    
    @Override
    public ResponseEntity<Void> deleteHistory(@RequestBody Long videoId) {
        try {
        	// Instancia para acceder a la base de datos
            UsersAccess cliente = new UsersAccess();
            
            // Conectar a la base de datos
            if (!cliente.dbConectar()) {
                System.out.println("Conexión fallida");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 si no se pudo conectar
            }

            
            cliente.dbDeleteHistory(videoId);
            

            // Desconectar la base de datos
            if (!cliente.dbDesconectar()) {
                System.out.println("Desconexión fallida");
            }
            
            System.out.println("Video eliminado del historial de todos los usuarios.");
        } catch (Exception e) {
            System.err.println("Error al eliminar el video del historial: " + e.getMessage());
            // Podrías también devolver un código de error HTTP adecuado si usas ResponseEntity
        }
        return new ResponseEntity<>(HttpStatus.OK); // 200 con la lista de usuarios seguidos
    }
    
    @Override
    public ResponseEntity<List<User>> getFollowingUsers(@PathVariable String username ) {
        System.out.println("------------ UserApiController -> getFollowingUsers() ------------");

        UsersAccess cliente = new UsersAccess();

        // Conexión a la base de datos
        if (!cliente.dbConectar()) {
            System.out.println("Conexión fallida");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Obtener lista de usuarios
        List<User> users = cliente.dbGetFollowingUsers(username);

        // Desconexión de la base de datos
        if (!cliente.dbDesconectar()) {
            System.out.println("Desconexión fallida");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Si la lista de usuarios está vacía, devuelve un estado HTTP 204 (sin contenido)
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Devuelve la lista de usuarios con estado 200
        return ResponseEntity.ok(users);
    }
    
    @Override
    public ResponseEntity<List<User>> getNotFollowingUsers(@PathVariable String username ) {
        System.out.println("------------ UserApiController -> getNotFollowingUsers() ------------");


        UsersAccess cliente = new UsersAccess();

        // Conexión a la base de datos
        if (!cliente.dbConectar()) {
            System.out.println("Conexión fallida");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Obtener lista de usuarios
        List<User> users = cliente.dbGetNotFollowingUsers(username);

      
        // Desconexión de la base de datos
        if (!cliente.dbDesconectar()) {
            System.out.println("Desconexión fallida");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Si la lista de usuarios está vacía, devuelve un estado HTTP 204 (sin contenido)
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Devuelve la lista de usuarios con estado 200
        return ResponseEntity.ok(users);
    }
}

package videoService;

import org.openapitools.model.LikeRequest;
import org.openapitools.model.Video;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class VideoService {
	
	private final RestTemplate restTemplate = new RestTemplate();
    private final String videoServiceUrl = "http://localhost:8082"; // URL del microservicio de videos

    public Video likeVideo(String videotitle, Long userId) {
        LikeRequest likeRequest = new LikeRequest(videotitle, userId);

        try {
            // Envía una solicitud POST al microservicio de videos
            return restTemplate.postForObject(videoServiceUrl + "/video/like", likeRequest, Video.class);
        } catch (HttpClientErrorException e) {
            // Maneja errores, como un 400 o 404, si el video no existe o el ID no es válido
            return null; // Puedes lanzar una excepción personalizada si prefieres
        }
    }
    public Video getVideoById(Long videoId) {
        try {
            // Envía una solicitud GET al microservicio de videos
            String url = String.format("%s/video/%d", videoServiceUrl, videoId);
            return restTemplate.getForObject(url, Video.class);
        } catch (HttpClientErrorException e) {
            // Maneja errores, como un 400 o 404, si el video no existe o el ID no es válido
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                System.out.println("Video no encontrado para el ID: " + videoId);
            } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                System.out.println("ID de video inválido: " + videoId);
            } else {
                System.out.println("Error al obtener el video: " + e.getMessage());
            }
            return null; // Puedes lanzar una excepción personalizada si prefieres
        } catch (Exception e) {
            // Maneja otros errores, como problemas de conexión
            System.out.println("Error inesperado: " + e.getMessage());
            return null;
        }
    }
    
    public ResponseEntity<Void> deleteVideos(Long userId) {
        try {
            // Envía una solicitud POST al microservicio de videos
            return restTemplate.postForEntity(videoServiceUrl + "/video/deleteById", userId, Void.class);
        } catch (HttpClientErrorException e) {
            // Maneja errores, como un 400 o 404, si el video no existe o el ID no es válido
            return null; // Puedes lanzar una excepción personalizada si prefieres
        }
    }
    
    public ResponseEntity<Void> deleteComments(Long userId) {
        try {
            // Envía una solicitud POST al microservicio de videos
            return restTemplate.postForEntity(videoServiceUrl + "/comments/deleteById", userId, Void.class);
        } catch (HttpClientErrorException e) {
            // Maneja errores, como un 400 o 404, si el video no existe o el ID no es válido
            return null; // Puedes lanzar una excepción personalizada si prefieres
        }
    }
}

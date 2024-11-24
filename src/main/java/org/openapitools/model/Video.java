package org.openapitools.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;
import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Video
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-15T15:54:30.961535300+02:00[Europe/Madrid]", comments = "Generator version: 7.9.0")
public class Video {

  private Long id;

  private String duration;

  private String title;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private Timestamp uploadDate;

  private String description;

  private String genre;

  private Integer likes;

  private Boolean ageRestricted;

  @Valid
  private ArrayList<String> countryRestricted = new ArrayList<>();

  private Integer video_user;

  public Video id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * ID único del video (clave primaria).
   * @return id
   */
  
  @Schema(name = "id", description = "ID único del video (clave primaria).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Video duration(String duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Duración del video en formato HH:MM:SS.
   * @return duration
   */
  
  @Schema(name = "duration", description = "Duración del video en formato HH:MM:SS.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration")
  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public Video title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Título del video.
   * @return title
   */
  
  @Schema(name = "title", description = "Título del video.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Video uploadDate(Timestamp uploadDate) {
    this.uploadDate = uploadDate;
    return this;
  }

  /**
   * Fecha y hora de subida del video.
   * @return uploadDate
   */
  @Valid 
  @Schema(name = "uploadDate", description = "Fecha y hora de subida del video.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uploadDate")
  public Timestamp getUploadDate() {
    return uploadDate;
  }

  public void setUploadDate(Timestamp uploadDate) {
    this.uploadDate = uploadDate;
  }

  public Video description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Descripción del video.
   * @return description
   */
  
  @Schema(name = "description", description = "Descripción del video.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Video genre(String genre) {
    this.genre = genre;
    return this;
  }

  /**
   * Género del video.
   * @return genre
   */
  
  @Schema(name = "genre", description = "Género del video.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("genre")
  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public Video likes(Integer likes) {
    this.likes = likes;
    return this;
  }

  /**
   * Número de \"me gustas\" para el video.
   * @return likes
   */
  
  @Schema(name = "likes", description = "Número de \"me gustas\" para el video.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("likes")
  public Integer getLikes() {
    return likes;
  }

  public void setLikes(Integer likes) {
    this.likes = likes;
  }

  public Video ageRestricted(Boolean ageRestricted) {
    this.ageRestricted = ageRestricted;
    return this;
  }

  /**
   * Indica si el contenido está restringido para usuarios mayores de edad.
   * @return ageRestricted
   */
  
  @Schema(name = "ageRestricted", description = "Indica si el contenido está restringido para usuarios mayores de edad.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ageRestricted")
  public Boolean getAgeRestricted() {
    return ageRestricted;
  }

  public void setAgeRestricted(Boolean ageRestricted) {
    this.ageRestricted = ageRestricted;
  }

  public Video countryRestricted(ArrayList<String> countryRestricted) {
    this.countryRestricted = countryRestricted;
    return this;
  }

  public Video addCountryRestrictedItem(String countryRestrictedItem) {
    if (this.countryRestricted == null) {
      this.countryRestricted = new ArrayList<>();
    }
    this.countryRestricted.add(countryRestrictedItem);
    return this;
  }

  /**
   * Lista de países a los que se restringe el acceso al video.
   * @return countryRestricted
   */
  
  @Schema(name = "countryRestricted", description = "Lista de países a los que se restringe el acceso al video.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("countryRestricted")
  public List<String> getCountryRestricted() {
    return countryRestricted;
  }

  public void setCountryRestricted(ArrayList<String> countryRestricted) {
    this.countryRestricted = countryRestricted;
  }

  public Video video_user(Integer video_user) {
    this.video_user = video_user;
    return this;
  }

  /**
   * Clave que referencia al usuario que ha subido el video.
   * @return video_user
   */
  
  @Schema(name = "video_user", description = "Clave que referencia al usuario que ha subido el video.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("video_user")
  public Integer getUser() {
    return video_user;
  }

  public void setUser(Integer video_user) {
    this.video_user = video_user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Video video = (Video) o;
    return Objects.equals(this.id, video.id) &&
        Objects.equals(this.duration, video.duration) &&
        Objects.equals(this.title, video.title) &&
        Objects.equals(this.uploadDate, video.uploadDate) &&
        Objects.equals(this.description, video.description) &&
        Objects.equals(this.genre, video.genre) &&
        Objects.equals(this.likes, video.likes) &&
        Objects.equals(this.ageRestricted, video.ageRestricted) &&
        Objects.equals(this.countryRestricted, video.countryRestricted) &&
        Objects.equals(this.video_user, video.video_user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, duration, title, uploadDate, description, genre, likes, ageRestricted, countryRestricted, video_user);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Video {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    uploadDate: ").append(toIndentedString(uploadDate)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    genre: ").append(toIndentedString(genre)).append("\n");
    sb.append("    likes: ").append(toIndentedString(likes)).append("\n");
    sb.append("    ageRestricted: ").append(toIndentedString(ageRestricted)).append("\n");
    sb.append("    countryRestricted: ").append(toIndentedString(countryRestricted)).append("\n");
    sb.append("    video_user: ").append(toIndentedString(video_user)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


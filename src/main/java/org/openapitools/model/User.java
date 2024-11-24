package org.openapitools.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;
import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * User
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-15T15:55:01.292135700+02:00[Europe/Madrid]", comments = "Generator version: 7.9.0")
public class User {

  private Long id;

  private String username;

  private String firstName;

  private String lastName;

  private String email;

  private String password;

  private String bio;

  private String role;

  private String country;

  private String profilePicture;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate birthdate;

  @Valid
  private List<Long> uploadedVideos = new ArrayList<>();

  @Valid
  private List<Long> watchedVideos = new ArrayList<>();

  @Valid
  private List<Long> followers = new ArrayList<>();

  @Valid
  private List<Long> following = new ArrayList<>();
  
  @Valid
  private List<Long> liked_video_ids = new ArrayList<>();

  public User id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  
  @Schema(name = "id", example = "10", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
   */
  
  @Schema(name = "username", example = "user1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public User firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
   */
  
  @Schema(name = "firstName", example = "John", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("firstName")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public User lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
   */
  
  @Schema(name = "lastName", example = "James", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public User email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  
  @Schema(name = "email", example = "john@email.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public User password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
   */
  
  @Schema(name = "password", example = "12345", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public User bio(String bio) {
    this.bio = bio;
    return this;
  }

  /**
   * Descripcion resumida del perfil
   * @return bio
   */
  
  @Schema(name = "bio", example = "Esta es la descripcion de user1", description = "Descripcion resumida del perfil", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bio")
  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public User role(String role) {
    this.role = role;
    return this;
  }

  /**
   * Indica si el usuario es administrador
   * @return role
   */
  
  @Schema(name = "role", example = "User", description = "Indica si el usuario es administrador", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public User country(String country) {
    this.country = country;
    return this;
  }

  /**
   * Pais del usuario
   * @return country
   */
  
  @Schema(name = "country", example = "Spain", description = "Pais del usuario", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("country")
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public User profilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
    return this;
  }

  /**
   * URL de la foto de perfil del usuario
   * @return profilePicture
   */
  @Valid 
  @Schema(name = "profilePicture", example = "https://example.com/images/user1.jpg", description = "URL de la foto de perfil del usuario", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("profilePicture")
  public String getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
  }

  public User birthdate(LocalDate birthdate) {
    this.birthdate = birthdate;
    return this;
  }

  /**
   * Fecha de nacimiento del usuario
   * @return birthdate
   */
  @Valid 
  @Schema(name = "birthdate", example = "Mon Jan 01 01:00:00 CET 1990", description = "Fecha de nacimiento del usuario", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("birthdate")
  public LocalDate getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(LocalDate birthdate) {
    this.birthdate = birthdate;
  }

  public User uploadedVideos(List<Long> uploadedVideos) {
    this.uploadedVideos = uploadedVideos;
    return this;
  }

  public User addUploadedVideosItem(Long uploadedVideosItem) {
    if (this.uploadedVideos == null) {
      this.uploadedVideos = new ArrayList<>();
    }
    this.uploadedVideos.add(uploadedVideosItem);
    return this;
  }

  /**
   * Lista de IDs de videos subidos por el usuario
   * @return uploadedVideos
   */
  
  @Schema(name = "uploadedVideos", description = "Lista de IDs de videos subidos por el usuario", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uploadedVideos")
  public List<Long> getUploadedVideos() {
    return uploadedVideos;
  }

  public void setUploadedVideos(List<Long> uploadedVideos) {
    this.uploadedVideos = uploadedVideos;
  }

  public User watchedVideos(List<Long> watchedVideos) {
    this.watchedVideos = watchedVideos;
    return this;
  }

  public User addWatchedVideosItem(Long watchedVideosItem) {
    if (this.watchedVideos == null) {
      this.watchedVideos = new ArrayList<>();
    }
    this.watchedVideos.add(watchedVideosItem);
    return this;
  }
  
  @JsonProperty("liked_video_ids")
  public List<Long> getLikedVideos() {
    return liked_video_ids;
  }

  public void setLikedVideos(List<Long> liked_video_ids) {
    this.liked_video_ids = liked_video_ids;
  }

  /**
   * Historial de videos vistos por el usuario
   * @return watchedVideos
   */
  
  @Schema(name = "watchedVideos", description = "Historial de videos vistos por el usuario", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("watchedVideos")
  public List<Long> getWatchedVideos() {
    return watchedVideos;
  }

  public void setWatchedVideos(List<Long> watchedVideos) {
    this.watchedVideos = watchedVideos;
  }

  public User followers(List<Long> followers) {
    this.followers = followers;
    return this;
  }

  public User addFollowersItem(Long followersItem) {
    if (this.followers == null) {
      this.followers = new ArrayList<>();
    }
    this.followers.add(followersItem);
    return this;
  }

  /**
   * Lista de IDs de usuarios que siguen a este usuario
   * @return followers
   */
  
  @Schema(name = "followers", description = "Lista de IDs de usuarios que siguen a este usuario", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("followers")
  public List<Long> getFollowers() {
    return followers;
  }

  public void setFollowers(List<Long> followers) {
    this.followers = followers;
  }

  public User following(List<Long> following) {
    this.following = following;
    return this;
  }

  public User addFollowingItem(Long followingItem) {
    if (this.following == null) {
      this.following = new ArrayList<>();
    }
    this.following.add(followingItem);
    return this;
  }

  /**
   * Lista de IDs de usuarios a los que este usuario sigue
   * @return following
   */
  
  @Schema(name = "following", description = "Lista de IDs de usuarios a los que este usuario sigue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("following")
  public List<Long> getFollowing() {
    return following;
  }

  public void setFollowing(List<Long> following) {
    this.following = following;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this.id, user.id) &&
        Objects.equals(this.username, user.username) &&
        Objects.equals(this.firstName, user.firstName) &&
        Objects.equals(this.lastName, user.lastName) &&
        Objects.equals(this.email, user.email) &&
        Objects.equals(this.password, user.password) &&
        Objects.equals(this.bio, user.bio) &&
        Objects.equals(this.role, user.role) &&
        Objects.equals(this.country, user.country) &&
        Objects.equals(this.profilePicture, user.profilePicture) &&
        Objects.equals(this.birthdate, user.birthdate) &&
        Objects.equals(this.uploadedVideos, user.uploadedVideos) &&
        Objects.equals(this.watchedVideos, user.watchedVideos) &&
        Objects.equals(this.followers, user.followers) &&
        Objects.equals(this.following, user.following);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, firstName, lastName, email, password, bio, role, country, profilePicture, birthdate, uploadedVideos, watchedVideos, followers, following);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    bio: ").append(toIndentedString(bio)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    profilePicture: ").append(toIndentedString(profilePicture)).append("\n");
    sb.append("    birthdate: ").append(toIndentedString(birthdate)).append("\n");
    sb.append("    uploadedVideos: ").append(toIndentedString(uploadedVideos)).append("\n");
    sb.append("    watchedVideos: ").append(toIndentedString(watchedVideos)).append("\n");
    sb.append("    followers: ").append(toIndentedString(followers)).append("\n");
    sb.append("    following: ").append(toIndentedString(following)).append("\n");
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


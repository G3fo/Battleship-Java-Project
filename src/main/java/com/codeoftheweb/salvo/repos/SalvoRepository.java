package com.codeoftheweb.salvo.repos;

import com.codeoftheweb.salvo.Salvo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SalvoRepository extends JpaRepository<Salvo, Long> {
  //public String getPlayerByuserName(String userName);
}
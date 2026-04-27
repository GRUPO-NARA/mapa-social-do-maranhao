package mpma.mapasocial.backend.repository.demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.demograficos.IndiceDeDesenvolvimentoHumanoEntity;

@Repository
public interface IndiceDeDesenvolvimentoHumanoRepository extends JpaRepository<IndiceDeDesenvolvimentoHumanoEntity, Long> {

}

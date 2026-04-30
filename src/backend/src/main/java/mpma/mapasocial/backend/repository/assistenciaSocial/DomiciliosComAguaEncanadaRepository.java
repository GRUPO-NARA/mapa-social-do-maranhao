package mpma.mapasocial.backend.repository.assistenciaSocial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.assistenciaSocial.domiciliosComAguaEncanadaEntity;

@Repository
public interface DomiciliosComAguaEncanadaRepository extends JpaRepository<domiciliosComAguaEncanadaEntity, Long> {

}

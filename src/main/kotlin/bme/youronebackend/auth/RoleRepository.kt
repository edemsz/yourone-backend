package bme.youronebackend.auth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional


@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface RoleRepository  : JpaRepository<Role, Long>
package ru.thelper.services.models_sevices.role

import org.springframework.stereotype.Service
import ru.thelper.exceptions.BadRequestException
import ru.thelper.models.users.ERole
import ru.thelper.models.users.Role
import ru.thelper.repository.RoleRepository

private const val NOT_FOUND_ROLE = "Error, Role {} is not found"

@Service
class RoleServiceImpl(private val repository: RoleRepository) : RoleService {

    /**
     * Get Role by Enum Erole
     */
    override fun getRoleByERole(eRole: ERole): Role {
        return repository.findByName(eRole.getName())
            .orElseThrow { throw BadRequestException(
                NOT_FOUND_ROLE.format(
                    eRole.getName()
                )
            )
            }
    }
}
package ru.mhelper.services.models_sevices.role

import org.springframework.stereotype.Service
import ru.mhelper.controllers.exeptions.BadRequestException
import ru.mhelper.models.users.ERole
import ru.mhelper.models.users.Role
import ru.mhelper.repository.RoleRepository

private const val NOT_FOUND_ROLE = "Error, Role {} is not found"

@Service
class RoleServiceImpl(private val repository: RoleRepository) : RoleService {

    /**
     * Get Role by Enum Erole
     */
    override fun getRoleByERole(eRole: ERole): Role {
        return repository.findByName(eRole.getName())
            .orElseThrow { throw BadRequestException(NOT_FOUND_ROLE.format(eRole.getName())) }
    }
}
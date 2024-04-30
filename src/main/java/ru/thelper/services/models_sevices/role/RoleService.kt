package ru.thelper.services.models_sevices.role

import ru.thelper.models.users.ERole
import ru.thelper.models.users.Role

interface RoleService {

    fun getRoleByERole(eRole: ERole): Role
}
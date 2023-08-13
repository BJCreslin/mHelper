package ru.mhelper.services.models_sevices.role

import ru.mhelper.models.users.ERole
import ru.mhelper.models.users.Role

interface RoleService {

    fun getRoleByERole(eRole: ERole): Role
}